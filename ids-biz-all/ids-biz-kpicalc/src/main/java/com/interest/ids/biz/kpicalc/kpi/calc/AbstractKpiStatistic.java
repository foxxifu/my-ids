package com.interest.ids.biz.kpicalc.kpi.calc;

import com.interest.ids.biz.kpicalc.kpi.calc.KpiStatistics;
import com.interest.ids.biz.kpicalc.kpi.constant.KpiStatiticDeviceType;
import com.interest.ids.biz.kpicalc.kpi.service.cache.IKpiCacheService;
import com.interest.ids.biz.kpicalc.kpi.service.kpiquery.IKpiCommonStatService;
import com.interest.ids.biz.kpicalc.kpi.util.KpiStatisticFlowVar;
import com.interest.ids.biz.kpicalc.kpi.util.KpiJedisCacheUtil;
import com.interest.ids.biz.kpicalc.kpi.util.KpiCacheSet;
import com.interest.ids.biz.kpicalc.kpi.util.KpiStatisticUtil;
import com.interest.ids.common.project.bean.KpiBaseModel;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.constant.KpiConstants;
import com.interest.ids.common.project.spring.context.SystemContext;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.commoninterface.service.cache.IDevCacheService;
import com.interest.ids.commoninterface.service.device.IDevPVCapacityService;
import com.interest.ids.commoninterface.service.kpi.IKpiCommonService;

import com.interest.ids.commoninterface.service.station.StationInfoMService;
import com.interest.ids.commoninterface.service.station.StationParamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.*;

public abstract class AbstractKpiStatistic implements KpiStatistics {
    // 日志记录
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    protected IKpiCommonStatService kpiCommonStatService;

    @Autowired
    protected IKpiCacheService cacheService;
    //参数设置service
    @Resource(name = "stationParamService")
    protected StationParamService paramService;
    //组串容量servcie
    @Resource
    protected IDevPVCapacityService devPVCapacityService;

    //kpi 通用service，获取计算数据
    @Autowired
    protected IKpiCommonService kpiCommonService;

    @Override
    public KpiStatisticFlowVar kpiCalculate(final List<String> stationCodes, final Long startTime) {
        try{
            // 设置redis key
            setRedisKey();
            // 处理时间
            Long sNowTime = dealTime(startTime);
            // 统计范围结束时间点
            Long eTime = nextTime(sNowTime); 
            // 取基础数据存入缓存
            loadDataToCache(sNowTime, eTime, stationCodes);
            printInfoMsg("load data to cache success.");
            // 取数据计算
            final List<? extends KpiBaseModel> list = kpiCalculate(stationCodes, sNowTime, eTime);
            printInfoMsg("kpi calculation success.");
            // 数据存库
            storeKpiData(list);
            printInfoMsg("store kpi calculation result success.");
            // 清空当前缓存
            clearAllRedisKey();
            printInfoMsg("all temp redis cache cleared.");
            // 结束时间
            KpiStatisticUtil.getCurrentTheadKpiStatFlowVar(this).setEndTime(System.currentTimeMillis());

            // 如果有下一个计算节点，进行下一个节点计算
            if(needTransferData()){
                transferDataToNext("data", list);
            }

            final KpiStatistics nextCalcNode = getNextCaculator();
            if(null != nextCalcNode && nextCalcNode != this){
                return nextCalcNode.kpiCalculate(stationCodes, startTime);
            }

            // 设置结果
            KpiStatisticUtil.getCurrentTheadKpiStatFlowVar(this).setStatStatus(true);
        } catch(Exception e){
            printErrMsg("kpi calculation got error, statisticTime=" + startTime, e);
            KpiStatisticUtil.getCurrentTheadKpiStatFlowVar(this).setStatStatus(false);
        }
        
        return KpiStatisticUtil.getCurrentTheadKpiStatFlowVar(this);
    }

    @Override
    public void storeKpiData(List<? extends KpiBaseModel> list) {
        kpiCommonStatService.saveOrUpdate(list);
    }

    // 处理时间
    public Long dealTime(Long time) {
        return time;
    }

    /**
     * 清空所有key
     */
    @Override
    public void clearAllRedisKey() {
        KpiJedisCacheUtil.deleteByScan(this.getRedisKey() + "*");
    }

    @Override
    public void setNextCaculator(KpiStatistics nextNode) {
        KpiStatisticUtil.getCurrentTheadKpiStatFlowVar(this).setNextCalculator(nextNode);
    }

    @Override
    public KpiStatistics getNextCaculator() {
        return KpiStatisticUtil.getCurrentTheadKpiStatFlowVar(this).getNextCalculator();
    }

    @Override
    public Object getTransferedData(String key) {
        return KpiStatisticUtil.getCurrentTheadKpiStatFlowVar(this).getDataListPool().get(key);
    }

    @Override
    public void transferDataToNext(String key, Object val) {
        KpiStatisticUtil.getCurrentTheadKpiStatFlowVar(this).getDataListPool().put(key, val);
    }

    @Override
    public Map<String, StationInfoM> getStationMap(Collection<String> statSIds) {
        List<StationInfoM> list = getStationService().listStationsByStationCodes(statSIds);
        Map<String, StationInfoM> r = new HashMap<>();
        if(null != list && list.size() > 0){
            for (StationInfoM st : list){
                r.put(st.getStationCode(), st);
            }
        }
        return r;
    }

    @Override
    public Map<String, List<DeviceInfo>> getStationDeviceMap(Collection<String> sIds,
            Collection<Integer> devTypes) {
        //如果是环境监测仪单独处理
        Map<String, List<DeviceInfo>> r = new HashMap<>();
        List<DeviceInfo> list = null;
        if(CommonUtil.isNotEmpty(devTypes) && devTypes.size() == 1
                && devTypes.iterator().next() == KpiStatiticDeviceType.ENVIRONMENT.getCode()){
            Collection<Long> dIds = getStationService().getShareEmiByStationCodes(sIds).values();
            list = getDevCacheService().getDevicesFromDB(dIds);
        }else{
            list = getDevCacheService().getDevicesFromDB(sIds, devTypes);
        }
        if(CommonUtil.isNotEmpty(list)){
            for (DeviceInfo st : list){
                List<DeviceInfo> t = r.get(st.getStationCode());
                if(null == t){
                    t = new ArrayList<>();
                }
                t.add(st);
                r.put(st.getStationCode(), t);
            }
        }
        return r;
    }

    @Override
    public boolean needTransferData() {
        return false;
    }

    @Override
    public TimeZone getTimeZone() {
        return KpiStatisticUtil.getCurrentTheadKpiStatFlowVar(this).getTimeZone();
    }

    @Override
    public void setTimeZone(TimeZone timeZone) {
        KpiStatisticUtil.getCurrentTheadKpiStatFlowVar(this).setTimeZone(timeZone);
    }

    @Override
    public void printWarnMsg(String msg) {
        
        logger.warn(getPrintMessage(msg));
    }

    @Override
    public void printInfoMsg(String msg) {
        logger.info(getPrintMessage(msg));
    }

    @Override
    public void printErrMsg(String msg, Throwable e) {
        logger.error(getPrintMessage(msg), e);
    }

    private String getPrintMessage(String msg){
        StringBuffer message = new StringBuffer();
        message.append(getRedisKey()).append("(").append(getTimeZone().getID())
        .append(") : ").append(msg);
        
        return message.toString();
    }
    
    @Override
    public void setRedisKey() {
        String className = this.getClass().getName().replace(this.getClass().getPackage().getName() + ".", "");
        KpiStatisticUtil.getCurrentTheadKpiStatFlowVar(this)
                .setRedisKey(UUID.randomUUID().toString().replaceAll("-", "") + "-" + className);
    }

    @Override
    public String getRedisKey() {
        return KpiStatisticUtil.getCurrentTheadKpiStatFlowVar(this).getRedisKey();
    }

    @Override
    public KpiCacheSet getCacheSetting(String key) {
        return KpiStatisticUtil.getCurrentTheadKpiStatFlowVar(this).getCacheSetting(key);
    }

    @Override
    public void addCacheSetting(String key, KpiCacheSet setting) {
        KpiStatisticUtil.getCurrentTheadKpiStatFlowVar(this).setCacheSetting(key, setting);
    }

    @Override
    public KpiCacheSet getDefaultCacheSetting() {
        return getCacheSetting(KpiConstants.CACHE_SETTING_DEFAULT);
    }

    @Override
    public void setDefaultCacheSetting(KpiCacheSet setting) {
        addCacheSetting(KpiConstants.CACHE_SETTING_DEFAULT, setting);
    }

    public StationInfoMService getStationService() {
        return (StationInfoMService)SystemContext.getBean("stationInfoMService");
    }
    
    @Override
    public List<Integer> getStatDevTypeIds(){
        return null;
    }

    public IDevCacheService getDevCacheService() {
        return (IDevCacheService)SystemContext.getBean("deviceCacheService");
    }

    public StationParamService getParamsService() {
         if(null == paramService){
             paramService = (StationParamService)SystemContext.getBean("stationParamService");
         }
         return paramService;
    }

    public IDevPVCapacityService getDevPVCapacityService(){
        if(devPVCapacityService == null){
            devPVCapacityService = (IDevPVCapacityService)SystemContext.getBean("pvCapacityService");
        }

        return devPVCapacityService;
    }
}
