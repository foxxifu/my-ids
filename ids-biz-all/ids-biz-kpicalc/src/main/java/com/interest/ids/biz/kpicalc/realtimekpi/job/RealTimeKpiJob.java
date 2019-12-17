package com.interest.ids.biz.kpicalc.realtimekpi.job;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import com.interest.ids.biz.kpicalc.realtimekpi.constant.KpiRealTimeConstant;
import com.interest.ids.biz.kpicalc.realtimekpi.job.handler.ActivePowerHandler;
import com.interest.ids.biz.kpicalc.realtimekpi.job.handler.DayCapacityHandler;
import com.interest.ids.biz.kpicalc.realtimekpi.job.handler.DayActiveCapacityHandler;
import com.interest.ids.biz.kpicalc.realtimekpi.job.handler.DayProfitHandler;
import com.interest.ids.biz.kpicalc.realtimekpi.job.handler.HourCapacityHandler;
import com.interest.ids.biz.kpicalc.realtimekpi.job.handler.HourProfitHandler;
import com.interest.ids.biz.kpicalc.realtimekpi.job.handler.JobHandler;
import com.interest.ids.biz.kpicalc.realtimekpi.job.handler.TotalCapacityHandler;
import com.interest.ids.biz.kpicalc.realtimekpi.job.handler.TotalMonthJobHandler;
import com.interest.ids.biz.kpicalc.realtimekpi.job.handler.TotalProfitHandler;
import com.interest.ids.biz.kpicalc.realtimekpi.job.handler.YearCapacityHandler;
import com.interest.ids.common.project.bean.kpi.KpiRealTimeM;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.common.project.utils.DateUtil;
import com.interest.ids.common.project.utils.MathUtil;
import com.interest.ids.commoninterface.constant.realtimekpi.KpiItem;
import com.interest.ids.commoninterface.service.kpi.IKpiCommonService;
import com.interest.ids.commoninterface.service.station.StationInfoMService;
import com.interest.ids.redis.caches.RealTimeKpiCache;
import com.interest.ids.redis.caches.StationCache;
import com.interest.ids.redis.constants.CacheKeyEnum;
import com.interest.ids.redis.utils.RedisUtil;

@Component("realkpiJob")
@SuppressWarnings({ "rawtypes", "unused" })
public class RealTimeKpiJob {

    private static Logger log = LoggerFactory.getLogger(RealTimeKpiJob.class);
    private static JobHandler handler;

    @Resource(name = "stationInfoMService")
    private StationInfoMService stationService;

    @Autowired
    private IKpiCommonService kpiCommonService;

    /**
     * 实时数据缓存清空任务：每日凌晨1点触发
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void clearKpiRealTimeCache() {
        log.info("start clearKpiRealTimeCache job.");
        // 电站列表
        List<StationInfoM> stationInfoMList = StationCache.getAllstations();
        if (CommonUtil.isNotEmpty(stationInfoMList)) {
            StringBuffer strb = new StringBuffer();
            strb.append(stationInfoMList.toString());
            log.info("clearKpiRealTimeCache stations:" + strb.toString());
            clearKpiRealTimeCache(stationInfoMList);
        } else {
            log.info("[clearKpiRealTimeCache] has no station in cache.");
        }
        log.info("[clearKpiRealTimeCache] kpi real time cache cleared.");
    }

    private void clearKpiRealTimeCache(List<StationInfoM> stationInfoMList) {
        if (CommonUtil.isNotEmpty(stationInfoMList)) {
            for (StationInfoM stationInfoM : stationInfoMList) {
                if (CommonUtil.isNotEmpty(stationInfoM.getStationCode())) {
                	 Jedis jedis = null;
                	try {
	                    jedis = RedisUtil.getJedis();
	                    Map<String, String> map = new HashMap<>();
	                    map.put(KpiItem.DAYINCOME.getVal(), "0");
	                    map.put(KpiItem.DAYCAPACITY.getVal(), "0");
	                    map.put(KpiItem.ACTIVEPOWER.getVal(), "0");
	                    String hmset = jedis.hmset(
	                            CacheKeyEnum.REALTIMEKPI.getCacheKey() + ":" + stationInfoM.getStationCode(), map);
                	} catch(Exception e) {
                		log.error("clear has excption", e);
                	} finally {
                		RedisUtil.closeJeids(jedis);
                	}
                }
            }
        }
    }

    /**
     * 实时计算任务：每分钟计算一次
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void caculateRealTimeKpi() {
        log.info("KPI real time calculation job started.");
        
        List<StationInfoM> stationInfoMList = StationCache.getAllstations();
        caculateRealTimeKpi(stationInfoMList);
        
        log.info("KPI real time calculation job finished.");
    }

    /**
     * 电站实时KPI计算
     * 
     * @param stationInfoMList
     */
    @SuppressWarnings("unchecked")
    public void caculateRealTimeKpi(List<StationInfoM> stationInfoMList) {
        // 更新电站状态(正常、故障、断连)
        stationService.updateStationStata();

        Calendar calendar = Calendar.getInstance();
        Long updateTime = System.currentTimeMillis();
        Map<String, Object> params = new HashMap<>();
        params.put("HOUR_OF_DAY", calendar.get(Calendar.HOUR_OF_DAY));
        Long times = calendar.getTimeInMillis();
        params.put("CURRENT_TIME", times);
        Long zerotime = DateUtil.getBeginOfDayTimeByMill(updateTime);
        params.put("ZERO_TIME", zerotime);

        if (null == stationInfoMList || stationInfoMList.size() == 0) {
            log.warn("Caculate stationList is null");
            return;
        }

        List<String> stationCodes = new ArrayList<>();
        for (StationInfoM stationInfoM : stationInfoMList) {
            String stationcode = stationInfoM.getStationCode();
            stationCodes.add(stationcode);
        }
        params.put("STATIONINFOS", stationInfoMList);
        params.put("STATIONCODES", stationCodes);
        params.put(JobHandler.SIGNAL, archSignalMap(stationCodes));
        
        // 设备类型
        Integer devTypeId = DevTypeConstant.INVERTER_DEV_TYPE;
        
        Map<String, Map<String, Object>> resultMap;
        Map<String, Map<String, Object>> tempResultMap;

        try {
            buildHandler();
            tempResultMap = handler.handleRequest(params);
            if (null == tempResultMap) {
                log.warn("KPI real time calculation failed.");
                return;
            }
            
            resultMap = new HashMap();
            Map<String, Object> stringObjectMap;
            String stationCode;
            TimeZone timeZone;
            for (StationInfoM stationInfoM : stationInfoMList) {
                stationCode = stationInfoM.getStationCode();
                if (CommonUtil.isEmpty(stationCode)) {
                    continue;
                }
                timeZone = TimeZone.getTimeZone("GMT" + stationInfoM.getTimeZone());
                stringObjectMap = new HashMap<>();
                stringObjectMap.put(KpiItem.DAYCAPACITY.getVal(),
                        tempResultMap.get(KpiItem.DAYCAPACITY.getVal()).get(stationCode));
                stringObjectMap.put(KpiItem.HOURCAPACITY.getVal(),
                        tempResultMap.get(KpiItem.HOURCAPACITY.getVal()).get(stationCode));
                stringObjectMap.put(KpiItem.HOURINCOME.getVal(),
                        tempResultMap.get(KpiItem.HOURINCOME.getVal()).get(stationCode));
                stringObjectMap.put(KpiItem.DAYINCOME.getVal(),
                        tempResultMap.get(KpiItem.DAYINCOME.getVal()).get(stationCode));
                stringObjectMap.put(KpiItem.ACTIVEPOWER.getVal(), tempResultMap.get(KpiItem.ACTIVEPOWER.getVal()).get(stationCode));
                stringObjectMap.put(KpiItem.TOTALCAPACITY.getVal(), tempResultMap.get(KpiItem.TOTALCAPACITY.getVal())
                        .get(stationCode));
                stringObjectMap.put(KpiItem.TOTALINCOME.getVal(),
                        tempResultMap.get(KpiItem.TOTALINCOME.getVal()).get(stationCode));
                stringObjectMap.put(KpiItem.TOTALYEARCAPACITY.getVal(),
                        tempResultMap.get(KpiItem.TOTALYEARCAPACITY.getVal()).get(stationCode));
                stringObjectMap.put(KpiItem.TOTALMONTHCAPACITY.getVal(),
                        tempResultMap.get(KpiItem.TOTALMONTHCAPACITY.getVal()).get(stationCode));

                stringObjectMap.put("UPDATE_TIME", updateTime);
                stringObjectMap
                        .put("UPDATE_DATE", DateUtil.getTimeStrByMill(updateTime, timeZone, "yyyy-MM-dd:HH"));
                resultMap.put(stationCode, stringObjectMap);
            }
            
            RealTimeKpiCache.updateRealTimeKpiCache(resultMap);
            log.info("caculateRealKpi end");

            // 存库
            storeActivePowerPer5Minuts(stationInfoMList, resultMap, updateTime);
        } catch (Exception e) {
            log.error("Caculate realTimeKpi error:", e);
        }
    }

    /**
     * 每隔5分钟将最新的实时数据进行入库，时间粒度为整5分钟点
     */
    public void storeActivePowerPer5Minuts(List<StationInfoM> stationInfoMList,
            Map<String, Map<String, Object>> realTimeDataMap, Long collectTime) {
        log.info("Start to store realtime kpi data to ids_station_realtime_data_t");

        if (stationInfoMList != null && realTimeDataMap != null) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(collectTime);
            int minute = calendar.get(Calendar.MINUTE);

            // 非整5分钟点， 不进行入库
            if (minute % 5 != 0) {
                return;
            }

            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            collectTime = calendar.getTimeInMillis();
            
            List<StationInfoM> existDataStationList = new ArrayList<>();
            for (StationInfoM stationInfo : stationInfoMList) {
                if (realTimeDataMap.containsKey(stationInfo.getStationCode())) {
                    existDataStationList.add(stationInfo);
                }
            }

            Set<String> stationCodesSet = realTimeDataMap.keySet() == null ? new HashSet<String>() : realTimeDataMap
                    .keySet();
            List<KpiRealTimeM> kpiRealTimeMList = new ArrayList<>();

            for (StationInfoM stationInfoM : existDataStationList) {
                String stationCode = stationInfoM.getStationCode();
                KpiRealTimeM kpiRealTimeM = new KpiRealTimeM();

                Map<String, Object> dataMap = realTimeDataMap.get(stationCode);
                try {

                    Double activePower = MathUtil.formatDouble(dataMap.get(KpiItem.ACTIVEPOWER.getVal()));
                    Double productPower = MathUtil.formatDouble(dataMap.get(KpiItem.TOTALCAPACITY.getVal()));
                    Double dayCap = MathUtil.formatDouble(dataMap.get(KpiItem.DAYCAPACITY.getVal()));
                    BigDecimal dayIncome = MathUtil.formatDecimal(dataMap.get(KpiItem.DAYINCOME.getVal()));
                    BigDecimal totalIncome = MathUtil.formatDecimal(dataMap.get(KpiItem.TOTALINCOME.getVal()));

                    kpiRealTimeM.setCollectTime(collectTime);
                    kpiRealTimeM.setEnterpriseId(stationInfoM.getEnterpriseId());
                    kpiRealTimeM.setStationCode(stationCode);
                    kpiRealTimeM.setDayCap(dayCap);
                    kpiRealTimeM.setProductPower(productPower);
                    kpiRealTimeM.setDayIncome(dayIncome);
                    kpiRealTimeM.setTotalIncome(totalIncome);
                    kpiRealTimeM.setActivePower(activePower);

                    kpiRealTimeMList.add(kpiRealTimeM);

                } catch (Exception e) {
                    log.error("When wire KpiRealTime object get error.", e);
                    continue;
                }
            }

            // 入库
            kpiCommonService.saveKpiRealTimeData(kpiRealTimeMList);
            log.info("KpiRealTime data save success.");

        }
    }


    public static Map<String, Map<String, String>> archSignalMap(List<String> stationCodes) {

        if (CommonUtil.isEmpty(stationCodes)) {
            return null;
        }

        Map<String, Map<String, String>> resultMap = new HashMap<>();

        Map<String, String> signalMap = new HashMap<>();
        signalMap.put(KpiRealTimeConstant.DAYCAPTIAL, KpiRealTimeConstant.DAYCAPTIAL);
        signalMap.put(KpiRealTimeConstant.ACTIVEPOWER, KpiRealTimeConstant.ACTIVEPOWER);
        signalMap.put(KpiRealTimeConstant.TOTALCAPACITY, KpiRealTimeConstant.TOTALCAPACITY);
        signalMap.put(KpiRealTimeConstant.ACTIVECAP, KpiRealTimeConstant.ACTIVECAP);
        signalMap.put(KpiRealTimeConstant.REVERSEACTIVECAP, KpiRealTimeConstant.REVERSEACTIVECAP);

        for (String stationCode : stationCodes) {
            resultMap.put(stationCode, signalMap);
        }

        return resultMap;
    }
    
    /**
     * 构建实时计算处理器
     */
    private synchronized void buildHandler(){
        if (handler == null) {
            
            // 当日逆变器发电量
            handler = new DayCapacityHandler(KpiItem.DAYCAPACITY.getVal());
            //当日上网电量
            JobHandler dayActiveCapacityHandler = new DayActiveCapacityHandler(KpiItem.ONGRIDPOWER.getVal());
            // 当前小时发电量
            JobHandler hourCapacity = new HourCapacityHandler(KpiItem.HOURCAPACITY.getVal());
            // 当前小时收益
            JobHandler hourProfitHandler = new HourProfitHandler(KpiItem.HOURINCOME.getVal());
            // 当日收益
            JobHandler dayProfitHandler = new DayProfitHandler(KpiItem.DAYINCOME.getVal());
            // 功率计算
            JobHandler activePowerHandler = new ActivePowerHandler(KpiItem.ACTIVEPOWER.getVal());
            // 总发电量
            JobHandler totalCapacityHandler = new TotalCapacityHandler(KpiItem.TOTALCAPACITY.getVal());
            // 总收益
            JobHandler totalProfitHandler = new TotalProfitHandler(KpiItem.TOTALINCOME.getVal());
            // 年发电量
            JobHandler yearCapacityHandler = new YearCapacityHandler(KpiItem.TOTALYEARCAPACITY.getVal());
            //月各实时指标累计
            JobHandler totalMonthJobHandler = new TotalMonthJobHandler(KpiItem.TOTALMONTHCAPACITY.getVal());
            
            handler.setNextJobHandler(dayActiveCapacityHandler);
            dayActiveCapacityHandler.setNextJobHandler(hourCapacity);
            hourCapacity.setNextJobHandler(hourProfitHandler);
            hourProfitHandler.setNextJobHandler(dayProfitHandler);
            dayProfitHandler.setNextJobHandler(activePowerHandler);
            activePowerHandler.setNextJobHandler(totalCapacityHandler);
            totalCapacityHandler.setNextJobHandler(totalProfitHandler);
            totalProfitHandler.setNextJobHandler(yearCapacityHandler);
            yearCapacityHandler.setNextJobHandler(totalMonthJobHandler);
        } 
    }

}
