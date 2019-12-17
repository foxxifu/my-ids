package com.interest.ids.dev.alarm.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.interest.ids.common.project.bean.analysis.AnalysisMatrixDayM;
import com.interest.ids.common.project.bean.analysis.AnalysisMatrixMonthM;
import com.interest.ids.common.project.bean.analysis.AnalysisMatrixYearM;
import com.interest.ids.common.project.bean.analysis.AnalysisPvM;
import com.interest.ids.common.project.bean.analysis.AnalysisPvMonthM;
import com.interest.ids.common.project.bean.device.PvCapacityM;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.constant.AnalysisState;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.dto.AnalysisDayGroupDto;
import com.interest.ids.common.project.mapper.signal.DevInfoMapper;
import com.interest.ids.common.project.threadpool.NamedThreadFactory;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.common.project.utils.DateUtil;
import com.interest.ids.common.project.utils.MathUtil;
import com.interest.ids.commoninterface.dao.device.DevPvCapacityMMapper;
import com.interest.ids.commoninterface.dao.station.StationInfoMMapper;
import com.interest.ids.commoninterface.service.analysis.IAnalysisPvService;
import com.interest.ids.redis.caches.StationCache;

@Component
public class PvAnalysisStatsticJob {

    private static final Logger logger = LoggerFactory.getLogger(PvAnalysisStatsticJob.class);

    private static final ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 5, 1, TimeUnit.MINUTES,
            new LinkedBlockingQueue<Runnable>(200), new NamedThreadFactory("pvAnalysisStatistic"));

    @Autowired
    private StationInfoMMapper stationMapper;

    @Autowired
    private IAnalysisPvService analysisPvService;

    @Autowired
    private DevInfoMapper devInfoMapper;

    @Autowired
    private DevPvCapacityMMapper devPvCapacityMapper;

    // 每天凌晨3点开始进行汇总统计
    @Scheduled(cron = "0 0 3 * * ?")
    public void execute() {
        logger.info("start pv analysis job: " + new Date());

        // 1.准备统计条件
        Long lastDay = System.currentTimeMillis() - 24 * 3600 * 1000;
        Long startTime = DateUtil.getBeginOfMonthTimeByMill(lastDay);
        Long endTime = DateUtil.getLastOfMonthTimeByMill(lastDay);

        List<StationInfoM> stations = StationCache.getAllstations();
        if (stations == null || stations.size() == 0) {
            stations = stationMapper.selectAllStations();
        }

        if (stations == null || stations.size() == 0) {
            return;
        }

        logger.info("static staions: " + stations);

        for (StationInfoM station : stations) {
            PvAnalysisStatisticThread calcThread = new PvAnalysisStatisticThread(station, startTime, endTime);
            try {
                pool.execute(calcThread);
            } catch (Exception e) {
                logger.error("when statistic station: {} get error: {}", station.getStationCode(), e);
            }

        }

        logger.info("finish pv analysis job: " + new Date());

    }

    /**
     * 统计计算线程
     */
    private class PvAnalysisStatisticThread implements Runnable {

        private StationInfoM station;

        private Long startTime; // 昨天所在月的开始时间

        private Long endTime; // 昨天所在月的最后一天时间
        // 昨天的开始时间
        private Long dayBegin = DateUtil.getBeginOfDayTimeByMill(System.currentTimeMillis() - 24 * 3600 * 1000);

        public PvAnalysisStatisticThread(StationInfoM station, Long startTime, Long endTime) {
            this.station = station;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        @Override
        public void run() {
            // 2. 归档统计
            if (station != null && station.getStationCode() != null && startTime > 0 && endTime > 0) {
                String stationCode = station.getStationCode();
                // 查询当日组串级分析数据
                List<AnalysisPvM> analysisPvDays = analysisPvService.getPvAnalysisDay(stationCode, startTime, endTime);
                if (analysisPvDays != null && analysisPvDays.size() > 0) {
                    // 对组串日数据进行分组，将同一个设备的同一串集中到一起
                    Map<String, List<AnalysisPvM>> sameDevPvMap = new HashMap<>();
                    Map<String, List<AnalysisPvM>> sameMatrixMap = new HashMap<>();
                    List<AnalysisPvM> sameDevPvList = null;
                    List<AnalysisPvM> sameMatrixList = null;

                    for (AnalysisPvM analysisPvM : analysisPvDays) {
                        String key = analysisPvM.getDevId() + "@" + analysisPvM.getPvCode();
                        sameDevPvList = sameDevPvMap.get(key);
                        if (sameDevPvList == null) {
                            sameDevPvList = new ArrayList<>();
                            sameDevPvMap.put(key, sameDevPvList);
                        }

                        sameDevPvList.add(analysisPvM);

                        if (dayBegin.equals(analysisPvM.getAnalysisTime())) {
                            String maxtrixKey = analysisPvM.getStationCode() + "@" + analysisPvM.getMatrixId();
                            sameMatrixList = sameMatrixMap.get(maxtrixKey);
                            if (sameMatrixList == null) {
                                sameMatrixList = new ArrayList<>();
                                sameMatrixMap.put(maxtrixKey, sameMatrixList);
                            }

                            sameMatrixList.add(analysisPvM);
                        }
                    }

                    // 2.1 归档组串诊断月数据
                    archivePvMonthData(sameDevPvMap);

                    // 2.2 归档组串诊断子阵日数据,如果日已经存在了就会失败,后面的都不会去继续执行
                    try {
                    	// <stationCode@子阵id，组串容量信息>子阵内的所有组串的数量信息,这个内容后面最好去做缓存reids，先测试就不做reids的存储
                    	Map<String, CapAndZuChuan> matrixIdToCapMap = new HashMap<String, CapAndZuChuan>();
//	                    List<AnalysisMatrixDayM> analysisMatrixDays = archiveMatrixDayData(sameMatrixMap, matrixIdToCapMap);
	                    archiveMatrixDayData(sameMatrixMap, matrixIdToCapMap);
	                    if(matrixIdToCapMap.isEmpty()) { // 如果没有任何的数据，计算就没有意义了，就不去做计算了
	                    	logger.warn("no matrix info of installedCapacity, stationCode = {}", stationCode);
	                    	return;
	                    }
	                    // 2.3 归档子阵月数据
	                    archiveMatrixMonthData(matrixIdToCapMap);
//	                    archiveMatrixMonthData(analysisMatrixDays);
	
	                    // 2.4 归档子阵年数据
	                    archiveMatrixYearData(matrixIdToCapMap);
                    }catch (Exception e) { // 计算错误出现异常还是打印日志，方便排错
                    	logger.error("save data exception::", e);
                    }
                }
            }
        }

        // 归档组串月度数据
        private void archivePvMonthData(Map<String, List<AnalysisPvM>> sameDevPvMap) {
            if (sameDevPvMap == null || sameDevPvMap.size() == 0) {
                return;
            }

            List<AnalysisPvMonthM> oneMonthPvAnalysis = new ArrayList<>();
            AnalysisPvMonthM analysisPvMonthM = null;
            for (String key : sameDevPvMap.keySet()) {
                Integer troubleLastTime = 0, ineffLastTime = 0, hidLastTime = 0;
                Double troubleLostPower = 0d, ineffLostPower = 0d, hidLostPower = 0d;
                List<AnalysisPvM> sameDevPvCodes = sameDevPvMap.get(key);
                for (AnalysisPvM analysisPvM : sameDevPvCodes) {
                    Long lastEndTime = analysisPvM.getLastEndTime() == null ? System.currentTimeMillis() : analysisPvM
                            .getLastEndTime();
                    Integer lastTime = (int) ((lastEndTime - analysisPvM.getLastStartTime()) / (60 * 60 * 1000));
                    lastTime = lastTime < 0 ? 0 : lastTime;

                    Byte analysisState = analysisPvM.getAnalysisState();
                    if (analysisState == AnalysisState.TROUBLE) {
                        troubleLastTime += lastTime;
                        troubleLostPower += analysisPvM.getLostPower();
                    } else if (analysisState == AnalysisState.LOW_EFFICENCE) {
                        ineffLastTime += lastTime;
                        ineffLostPower += analysisPvM.getLostPower();
                    } else if (analysisState == AnalysisState.HIDDEND) {
                        hidLastTime += lastTime;
                        hidLostPower += analysisPvM.getLostPower();
                    }
                }

                // 构造月度数据
                analysisPvMonthM = new AnalysisPvMonthM();
                analysisPvMonthM.setStationCode(sameDevPvCodes.get(0).getStationCode());
                analysisPvMonthM.setDevId(sameDevPvCodes.get(0).getDevId());
                analysisPvMonthM.setPvCode(sameDevPvCodes.get(0).getPvCode());
                analysisPvMonthM.setAnalysisTime(startTime);
                analysisPvMonthM.setDevAlias(sameDevPvCodes.get(0).getDevAlias());
                analysisPvMonthM.setMatrixId(sameDevPvCodes.get(0).getMatrixId());
                analysisPvMonthM.setMatrixName(sameDevPvCodes.get(0).getMatrixName());
                analysisPvMonthM.setPvCapacity(sameDevPvCodes.get(0).getPvCapacity());
                analysisPvMonthM.setTroubleLastTime(troubleLastTime);
                analysisPvMonthM.setTroubleLostPower(troubleLostPower);
                analysisPvMonthM.setIneffLastTime(ineffLastTime);
                analysisPvMonthM.setIneffLostPower(ineffLostPower);
                analysisPvMonthM.setHidLastTime(hidLastTime);
                analysisPvMonthM.setHidLostPower(hidLostPower);

                oneMonthPvAnalysis.add(analysisPvMonthM);
            }

            // 如果已存在月数据，则更新
            List<AnalysisPvMonthM> existedPvMonths = analysisPvService.getPvAnalysisMonth(station.getStationCode(),
            		startTime);
            List<AnalysisPvMonthM> needUpdatePvMonths = new ArrayList<>();
            if (existedPvMonths != null && existedPvMonths.size() > 0) {
                for (AnalysisPvMonthM existPvMonth : existedPvMonths) {
                    for (AnalysisPvMonthM pvMonth : oneMonthPvAnalysis) {
                        if (pvMonth.getStationCode().equals(existPvMonth.getStationCode())
                                && isLongEq(pvMonth.getDevId(), existPvMonth.getDevId())
                                && isIntEq(pvMonth.getPvCode(), existPvMonth.getPvCode())
                                // && isLongEq(pvMonth.getAnalysisTime(), existPvMonth.getAnalysisTime()) // 时间可以不用，因为查询的时候使用了这个时间
                                ) {

                            existPvMonth.setTroubleLastTime(pvMonth.getTroubleLastTime());
                            existPvMonth.setTroubleLostPower(pvMonth.getTroubleLostPower());
                            existPvMonth.setIneffLastTime(pvMonth.getIneffLastTime());
                            existPvMonth.setIneffLostPower(pvMonth.getIneffLostPower());
                            existPvMonth.setHidLastTime(pvMonth.getHidLastTime());
                            existPvMonth.setHidLostPower(pvMonth.getHidLostPower());

                            needUpdatePvMonths.add(pvMonth);
                        }
                    }
                }

                analysisPvService.upateAnalysisPvMonthM(existedPvMonths);
                logger.info("update pv analysis month data: {}", existedPvMonths);
            }

            oneMonthPvAnalysis.removeAll(needUpdatePvMonths);
            if (oneMonthPvAnalysis.size() > 0) {
                analysisPvService.saveAnalysisPvMonthM(oneMonthPvAnalysis);
                logger.info("insert pv analysis month data: {}", oneMonthPvAnalysis);
            }
        }

        // 归档子阵日数据
        private List<AnalysisMatrixDayM> archiveMatrixDayData(Map<String, List<AnalysisPvM>> sameMatrixMap, Map<String, CapAndZuChuan> matrixIdToCapMap) {
            if (sameMatrixMap == null || sameMatrixMap.size() == 0) {
                return null;
            }

            // TODO 户用未加入
            List<Integer> inverterTypes = CommonUtil.createListWithElements(DevTypeConstant.INVERTER_DEV_TYPE,
                    DevTypeConstant.CENTER_INVERT_DEV_TYPE);

            List<AnalysisMatrixDayM> analysisMatrixDays = new ArrayList<>();
            for (String matrixKey : sameMatrixMap.keySet()) {
                Integer troublePvNum = 0, ineffPvNum = 0, hidPvNum = 0;
                Double troubleLostPower = 0d, ineffLostPower = 0d, hidLostPower = 0d;
                List<AnalysisPvM> sameMatrixPvs = sameMatrixMap.get(matrixKey);

                for (AnalysisPvM analysisPvM : sameMatrixPvs) {
                    Byte analysisState = analysisPvM.getAnalysisState();
                    if (analysisState == AnalysisState.TROUBLE) {
                        troublePvNum += 1;
                        troubleLostPower += analysisPvM.getLostPower();
                    } else if (analysisState == AnalysisState.LOW_EFFICENCE) {
                        ineffPvNum += 1;
                        ineffLostPower += analysisPvM.getLostPower();
                    } else if (analysisState == AnalysisState.HIDDEND) {
                        hidPvNum += 1;
                        hidLostPower += analysisPvM.getLostPower();
                    }
                }

                // 获取子阵总接入组串数，以及子阵当日发电量
                List<Long> devIds = devInfoMapper.queryDevIdsByMatrixId(sameMatrixPvs.get(0).getMatrixId(),
                        inverterTypes);
                if (devIds == null || devIds.size() == 0) {
                    return null;
                }

                // 注：后面加入缓存，从缓存获取
                int pvNum = 0;
                Double installedCapacity = 0d;

                List<PvCapacityM> pvCapacitys = devPvCapacityMapper.selectPvCapByDevIds(devIds);
                if (pvCapacitys != null && pvCapacitys.size() > 0) {
                    for (PvCapacityM pvCapacity : pvCapacitys) {
                        int devPvNum = pvCapacity.getNum() == null ? 0 : pvCapacity.getNum();
                        pvNum += devPvNum;
                        for (int i = 1; i <= devPvNum; i++) {
                            try {
                                installedCapacity += MathUtil.formatDouble(
                                        PropertyUtils.getProperty(pvCapacity, "pv" + i), 0d);
                            } catch (Exception e) {
                                logger.error("get dev pv capacity error.", e);
                            }
                        }
                    }
                    // 保存当前子阵对应的组串总数和（集中式逆变器+组串式逆变器设备的容量）
                    matrixIdToCapMap.put(matrixKey, new CapAndZuChuan(pvNum, installedCapacity));
                }

                // 获取子阵发电量
                Double productPower = analysisPvService.getMatrixDayProductPower(devIds, dayBegin);
                productPower = MathUtil.formatDouble(productPower, 0d);// MathUtil.formatDouble(productPower, 0d) / (3.6 * 1000000); // 1kwh = 3.6 * 10^6J

                AnalysisMatrixDayM analysisMatrixDayM = new AnalysisMatrixDayM();
                analysisMatrixDayM.setStationCode(sameMatrixPvs.get(0).getStationCode());
                analysisMatrixDayM.setMatrixId(sameMatrixPvs.get(0).getMatrixId());
                analysisMatrixDayM.setAnalysisTime(dayBegin);
                analysisMatrixDayM.setTroublePvNum(troublePvNum);
                analysisMatrixDayM.setTroubleLostPower(troubleLostPower);
                analysisMatrixDayM.setIneffPvNum(ineffPvNum);
                analysisMatrixDayM.setIneffLostPower(ineffLostPower);
                analysisMatrixDayM.setHidLostPower(hidLostPower);
                analysisMatrixDayM.setHidPvNum(hidPvNum);
                analysisMatrixDayM.setMatrixName(sameMatrixPvs.get(0).getMatrixName());
                analysisMatrixDayM.setProductPower(productPower);
                analysisMatrixDayM.setPvNum(pvNum);
                analysisMatrixDayM.setInstalledCapacity(installedCapacity / 1000);

                analysisMatrixDays.add(analysisMatrixDayM);
            }

            // 保存子阵日级数据
            analysisPvService.saveAnalysisMatrixDay(analysisMatrixDays);

            return analysisMatrixDays;
        }

        /**
         * 归档子阵月的数据
         * @param matrixIdToCapMap 子阵与对应（子阵总容量+子阵总串数）
         */
        private void archiveMatrixMonthData(Map<String, CapAndZuChuan> matrixIdToCapMap){
        	// 1.准备数据
        	String stationCode = station.getStationCode(); // 电站编号
        	Long beginDate = DateUtil.getBeginOfMonthTimeByMill(startTime); // 获取当前月的开始时间
        	Long endDate = DateUtil.getLastOfMonthTimeByMill(startTime); // 获取当前月的结束时间
        	
        	// 2.查询数据，数据(根据电站查询根据子阵分组的数据)
        	List<AnalysisDayGroupDto> list = analysisPvService.getPvAnalysisOfGroupByMatrix(stationCode, beginDate, endDate);
        	if(CollectionUtils.isEmpty(list)) { // 没有查询到任何的数据
        		logger.warn("no day info exist,stationCode={}, startTime = {}, endTime = {}", stationCode, beginDate, endDate);
        		return;
        	}
        	Map<String, Set<String>> pvInfoMap = new HashMap<>(); // <子阵id + '@' + stateType + '=' + devId，对应的组串的信息>
        	Map<String, Double> dtoDataMap = new HashMap<>(); // <子阵id+'@'+stateType, 值>
        	Map<Long, String> idToNameMap = new HashMap<>(); // <子阵id，子阵名称>
        	Map<Long, Set<Long>> mIdToDevIdMap = new HashMap<>(); // <子阵id， 设备id的集合>
        	for(AnalysisDayGroupDto dto : list){
        		Long mId = dto.getMatrixId(); // 子阵id
        		String pvKey = mId + "@" + dto.getAnalysisState() + "=" + dto.getDevId();
        		if(!pvInfoMap.containsKey(pvKey)){
        			pvInfoMap.put(pvKey, new HashSet<String>());
        		}
        		// 组装pv的块数
        		if(!StringUtils.isEmpty(dto.getPvs())){ // pvs是pv的编号，编号之间使用英文逗号隔开的
        			
        			pvInfoMap.get(pvKey).addAll(Arrays.asList(dto.getPvs().split(",")));
        		}
        		// 组装各种类型的损失电量
        		String ssKey = mId + "@" + dto.getAnalysisState(); // 子阵对应各种的损失的数据
        		if(!dtoDataMap.containsKey(ssKey)){
        			dtoDataMap.put(ssKey, 0d);
        		}
        		dtoDataMap.put(ssKey, dtoDataMap.get(ssKey) + MathUtil.formatDouble(dto.getLostPower(), 0d));
        		// 组装子阵id到子阵的转换
        		if(!idToNameMap.containsKey(mId)){
        			idToNameMap.put(mId, dto.getMatrixName());
        		}
        		// 存放子阵下的设备集合的id
        		if(!mIdToDevIdMap.containsKey(mId)){
        			mIdToDevIdMap.put(mId, new HashSet<Long>());
        		}
        		mIdToDevIdMap.get(mId).add(dto.getDevId());
        	}
        	// 组装对应的数量 <子阵id+'@'+stateType, 对应的类型的组串数量>
        	Map<String, Integer> ssNumMap = new HashMap<>();
        	for(Map.Entry<String, Set<String>> enty :pvInfoMap.entrySet()){
        		String key = enty.getKey();
        		Set<String> set = enty.getValue();
        		String tkey = key.split("=")[0];
        		Integer num = ssNumMap.get(tkey);
        		num = num == null ? 0 : num;
        		num += set.size(); // 添加每一个设备的组串数量,即是对设备组串做累加
        		ssNumMap.put(tkey, num);
        	}
        	// 3.封装需要存储的数据
        	List<AnalysisMatrixMonthM> analysisMatrixMonthList = new ArrayList<>();
        	// 根据子阵来循环
        	for(Map.Entry<Long, String> entry : idToNameMap.entrySet()){
        		// 3.0组装基本的数据
        		AnalysisMatrixMonthM ammm = new AnalysisMatrixMonthM();
        		Long mId = entry.getKey();
        		ammm.setStationCode(stationCode);
        		ammm.setAnalysisTime(beginDate);
        		ammm.setMatrixId(mId);
        		ammm.setMatrixName(entry.getValue());
        		CapAndZuChuan czc = matrixIdToCapMap.get(stationCode + "@" + mId);
        		ammm.setPvNum(czc.getPvNum()); // 子阵组串数量
        		ammm.setInstalledCapacity(MathUtil.formatDouble(czc.getCapty(), 0d) / 1000); // 子阵的装机容量，组串式逆变器容量 + 集中式逆变器的容量
        		// 获取当前的发电量
        		Double productPower = analysisPvService.getMatrixDayProductPower
        				(new ArrayList<Long>(mIdToDevIdMap.get(mId)), beginDate);
                ammm.setProductPower(MathUtil.formatDouble(productPower, 0d));
        		// 3.1故障的数据
            	// 3.1.1故障的组串数量
                String key = mId + "@1";
                ammm.setTroublePvNum(MathUtil.formatInteger(ssNumMap.get(key), 0));
            	// 3.1.2故障的损失电量
                ammm.setTroubleLostPower(MathUtil.formatDouble(dtoDataMap.get(key), 0d));
                
            	// 3.2低效的数据
            	// 3.2.1低效的组串数量
                key = mId + "@2";
                ammm.setIneffPvNum(MathUtil.formatInteger(ssNumMap.get(key), 0));
                // 3.2.2低效的损失电量
                ammm.setIneffLostPower(MathUtil.formatDouble(dtoDataMap.get(key), 0d));
                
            	// 3.3遮挡的数据
                // 3.2.1遮挡的组串数量
                key = mId + "@3";
                ammm.setHidPvNum(MathUtil.formatInteger(ssNumMap.get(key), 0));
                // 3.2.2遮挡的损失电量
                ammm.setHidLostPower(MathUtil.formatDouble(dtoDataMap.get(key), 0d));
                analysisMatrixMonthList.add(ammm);
        	}
        	// 已经存在的数据
        	List<AnalysisMatrixMonthM> analysisMatrixMonths = analysisPvService.getAnalysisMatrixMonth(
                    station.getStationCode(), startTime);
        	if(!CollectionUtils.isEmpty(analysisMatrixMonths)) { // 如果当前没有任何的数据，就直接新增
        		List<AnalysisMatrixMonthM> updateDatas = new ArrayList<>(); // 需要做修改的数据
            	for(AnalysisMatrixMonthM data: analysisMatrixMonthList) {
            		if(analysisMatrixMonths.contains(data)){
            			updateDatas.add(data);
            		}
            	}
            	if(!CollectionUtils.isEmpty(updateDatas)){ // 有数据需要修改
            		analysisPvService.updateAnalysisMatrixMonth(updateDatas);
            		analysisMatrixMonthList.removeAll(updateDatas); // 只保留新增的数据
            	}
        	}
        	if(!CollectionUtils.isEmpty(analysisMatrixMonthList)) { // 如果有数据需要新增
        		analysisPvService.saveAnalysisMatrixMonth(analysisMatrixMonthList);
        	}
        }
        
       
        /**
         * 归档子阵年的数据
         * @param matrixIdToCapMap 子阵与对应（子阵总容量+子阵总串数）
         */
        private void archiveMatrixYearData(Map<String, CapAndZuChuan> matrixIdToCapMap){
        	// 1.准备数据
        	String stationCode = station.getStationCode(); // 电站编号
        	Long beginDate = DateUtil.getBeginOfYearTimeByMill(startTime); // 获取当前月的开始时间
        	Long endDate = DateUtil.getLastOfYearTimeByMill(startTime); // 获取当前月的结束时间
        	
        	// 2.查询数据，数据(根据电站查询根据子阵分组的数据)
        	List<AnalysisDayGroupDto> list = analysisPvService.getPvAnalysisOfGroupByMatrix(stationCode, beginDate, endDate);
        	if(CollectionUtils.isEmpty(list)) { // 没有查询到任何的数据
        		logger.warn("no day info exist,stationCode={}, startTime = {}, endTime = {}", stationCode, beginDate, endDate);
        		return;
        	}
        	Map<String, Set<String>> pvInfoMap = new HashMap<>(); // <子阵id + '@' + stateType + '=' + devId，对应的组串的信息>
        	Map<String, Double> dtoDataMap = new HashMap<>(); // <子阵id+'@'+stateType, 值>
        	Map<Long, String> idToNameMap = new HashMap<>(); // <子阵id，子阵名称>
        	Map<Long, Set<Long>> mIdToDevIdMap = new HashMap<>(); // <子阵id， 设备id的集合>
        	for(AnalysisDayGroupDto dto : list){
        		Long mId = dto.getMatrixId(); // 子阵id
        		String pvKey = mId + "@" + dto.getAnalysisState() + "=" + dto.getDevId();
        		if(!pvInfoMap.containsKey(pvKey)){
        			pvInfoMap.put(pvKey, new HashSet<String>());
        		}
        		// 组装pv的块数
        		if(!StringUtils.isEmpty(dto.getPvs())){ // pvs是pv的编号，编号之间使用英文逗号隔开的
        			
        			pvInfoMap.get(pvKey).addAll(Arrays.asList(dto.getPvs().split(",")));
        		}
        		// 组装各种类型的损失电量
        		String ssKey = mId + "@" + dto.getAnalysisState(); // 子阵对应各种的损失的数据
        		if(!dtoDataMap.containsKey(ssKey)){
        			dtoDataMap.put(ssKey, 0d);
        		}
        		dtoDataMap.put(ssKey, dtoDataMap.get(ssKey) + MathUtil.formatDouble(dto.getLostPower(), 0d));
        		// 组装子阵id到子阵的转换
        		if(!idToNameMap.containsKey(mId)){
        			idToNameMap.put(mId, dto.getMatrixName());
        		}
        		// 存放子阵下的设备集合的id
        		if(!mIdToDevIdMap.containsKey(mId)){
        			mIdToDevIdMap.put(mId, new HashSet<Long>());
        		}
        		mIdToDevIdMap.get(mId).add(dto.getDevId());
        	}
        	// 组装对应的数量 <子阵id+'@'+stateType, 对应的类型的组串数量>
        	Map<String, Integer> ssNumMap = new HashMap<>();
        	for(Map.Entry<String, Set<String>> enty :pvInfoMap.entrySet()){
        		String key = enty.getKey();
        		Set<String> set = enty.getValue();
        		String tkey = key.split("=")[0];
        		Integer num = ssNumMap.get(tkey);
        		num = num == null ? 0 : num;
        		num += set.size(); // 添加每一个设备的组串数量,即是对设备组串做累加
        		ssNumMap.put(tkey, num);
        	}
        	// 3.封装需要存储的数据
        	List<AnalysisMatrixYearM> analysisMatrixMonthList = new ArrayList<>();
        	// 根据子阵来循环
        	for(Map.Entry<Long, String> entry : idToNameMap.entrySet()){
        		// 3.0组装基本的数据
        		AnalysisMatrixYearM ammm = new AnalysisMatrixYearM();
        		Long mId = entry.getKey();
        		ammm.setStationCode(stationCode);
        		ammm.setAnalysisTime(beginDate);
        		ammm.setMatrixId(mId);
        		ammm.setMatrixName(entry.getValue());
        		CapAndZuChuan czc = matrixIdToCapMap.get(stationCode + "@" + mId);
        		ammm.setPvNum(czc.getPvNum()); // 子阵组串数量
        		ammm.setInstalledCapacity(MathUtil.formatDouble(czc.getCapty(), 0d) / 1000); // 子阵的装机容量，组串式逆变器容量 + 集中式逆变器的容量
        		// 获取当前的发电量
        		Double productPower = analysisPvService.getMatrixDayProductPower
        				(new ArrayList<Long>(mIdToDevIdMap.get(mId)), beginDate);
                ammm.setProductPower(MathUtil.formatDouble(productPower, 0d));
        		// 3.1故障的数据
            	// 3.1.1故障的组串数量
                String key = mId + "@1";
                ammm.setTroublePvNum(MathUtil.formatInteger(ssNumMap.get(key), 0));
            	// 3.1.2故障的损失电量
                ammm.setTroubleLostPower(MathUtil.formatDouble(dtoDataMap.get(key), 0d));
                
            	// 3.2低效的数据
            	// 3.2.1低效的组串数量
                key = mId + "@2";
                ammm.setIneffPvNum(MathUtil.formatInteger(ssNumMap.get(key), 0));
                // 3.2.2低效的损失电量
                ammm.setIneffLostPower(MathUtil.formatDouble(dtoDataMap.get(key), 0d));
                
            	// 3.3遮挡的数据
                // 3.2.1遮挡的组串数量
                key = mId + "@3";
                ammm.setHidPvNum(MathUtil.formatInteger(ssNumMap.get(key), 0));
                // 3.2.2遮挡的损失电量
                ammm.setHidLostPower(MathUtil.formatDouble(dtoDataMap.get(key), 0d));
                analysisMatrixMonthList.add(ammm);
        	}
        	// 已经存在的数据
        	List<AnalysisMatrixYearM> analysisMatrixMonths = analysisPvService.getAnalysisMatrixYear(
                    station.getStationCode(), beginDate);
        	if(!CollectionUtils.isEmpty(analysisMatrixMonths)) { // 如果当前没有任何的数据，就直接新增
        		List<AnalysisMatrixYearM> updateDatas = new ArrayList<>(); // 需要做修改的数据
            	for(AnalysisMatrixYearM data: analysisMatrixMonthList) {
            		if(analysisMatrixMonths.contains(data)){
            			updateDatas.add(data);
            		}
            	}
            	if(!CollectionUtils.isEmpty(updateDatas)){ // 有数据需要修改
            		analysisPvService.updateAnalysisMatrixYear(updateDatas);
            		analysisMatrixMonthList.removeAll(updateDatas); // 只保留新增的数据
            	}
        	}
        	if(!CollectionUtils.isEmpty(analysisMatrixMonthList)) { // 如果有数据需要新增
        		analysisPvService.saveAnalysisMatrixYear(analysisMatrixMonthList);
        	}
        }
        
        // 判断连个长整型是否相等
        private boolean isLongEq (Long time1, Long time2) {
        	if (time1 == null) {
        		return time2 == null;
        	}
        	return time1.equals(time2);
        }
        // 判断两个整数是否相同
        private boolean isIntEq (Integer time1, Integer time2) {
        	if (time1 == null) {
        		return time2 == null;
        	}
        	return time1.equals(time2);
        }

    }
    
    /**
     * 子阵内部的组串的容量和组串之间的数据信息
     * @author wq
     *
     */
    private class CapAndZuChuan{
    	private Integer pvNum; // 子阵的组串数量
    	private Double capty; // 子阵的组串容量
		public Integer getPvNum() {
			return pvNum;
		}
		public Double getCapty() {
			return capty;
		}
		public CapAndZuChuan(Integer pvNum, Double capty) {
			super();
			this.pvNum = pvNum;
			this.capty = capty;
		}
		@Override
		public String toString() {
			return "CapAndZuChuan [pvNum=" + pvNum + ", capty=" + capty + "]";
		}
    	
    }
}
