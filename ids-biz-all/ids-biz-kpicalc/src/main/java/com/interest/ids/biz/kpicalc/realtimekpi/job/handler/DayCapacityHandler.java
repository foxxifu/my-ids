package com.interest.ids.biz.kpicalc.realtimekpi.job.handler;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import com.interest.ids.biz.kpicalc.realtimekpi.constant.KpiRealTimeConstant;
import com.interest.ids.common.project.bean.kpi.KpiStationHourM;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.constant.StationInfoConstant;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.common.project.utils.MathUtil;
import com.interest.ids.commoninterface.constant.realtimekpi.KpiItem;
import com.interest.ids.redis.caches.StationCache;
import com.interest.ids.redis.constants.CacheKeyEnum;
import com.interest.ids.redis.utils.RedisUtil;

/**
 * 当日发电量计算
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DayCapacityHandler extends JobHandler {
    private static Logger log = LoggerFactory.getLogger(DayCapacityHandler.class);

    @Override
    public Map excuteJob(Map<String, Object> params) {

        Map<String, Map<String, String>> signalMap = (Map<String, Map<String, String>>) params.get(SIGNAL);

        List<StationInfoM> stationInfoMList = (List<StationInfoM>) params.get("STATIONINFOS");
        Set<Integer> devTypes = CommonUtil.createSetWithElements(true, 
                DevTypeConstant.INVERTER_DEV_TYPE,
                DevTypeConstant.HOUSEHOLD_INVERTER_DEV_TYPE,
                DevTypeConstant.CENTER_INVERT_DEV_TYPE);
        // 缓存中最新发电量值(电站下逆变器发电量累计)
        Map<String, Double> realCaptialMap = getStationPropertyData(getSignalMap(signalMap, KpiRealTimeConstant.DAYCAPTIAL), devTypes);

        // 获取截止到目前为止当天电站各小时的数据
        List<String> stationCodes = new ArrayList<String>();
        for (StationInfoM stationInfoM : stationInfoMList){
            stationCodes.add(stationInfoM.getStationCode());
        }
        Long endTime = (Long) params.get("CURRENT_TIME");
        Long beginTime = (Long) params.get("ZERO_TIME");
        List<KpiStationHourM> kpiStationHourMs = getKpiService().queryStationHourKpi(stationCodes, beginTime, endTime);
        params.put(KPISTATIONHOURMS, kpiStationHourMs);
        log.info("kpiStationHourMs" + kpiStationHourMs);
        
        // 计算电站逆变器发电量(逆变器小时发电量累加)
        Map<String, Double> capacityPower = new HashMap<String, Double>();
        for (KpiStationHourM kpiStationHourM : kpiStationHourMs){
            String stationCode = kpiStationHourM.getStationCode();
            Double oldProDuctPower = (capacityPower.get(stationCode) == null) ? 0D : capacityPower.get(stationCode);
            Double newProDuctPower = (kpiStationHourM.getProductPower() == null) ? 0D : kpiStationHourM.getProductPower();
            capacityPower.put(stationCode, oldProDuctPower + newProDuctPower);
        }
        params.put(SUMHOURCAPACITY, capacityPower);
        
        Map<String, Double> result = new HashMap<String, Double>();
        Jedis j = RedisUtil.getJedis();
        Pipeline p = j.pipelined();
        try{
            for (String stationCode : stationCodes){
                Response<String> response = p.get(StationCache.genStationStateCacheKey(stationCode));
                Response<String> daycapacityCache = p.hget(CacheKeyEnum.REALTIMEKPI.getCacheKey() + ":" + stationCode, KpiItem.DAYCAPACITY.getVal());
                p.sync();
                String state = response.get();
                // 如果电站断连则取上一个值
                if(StationInfoConstant.DISCONECTED.equals(state)){
                    result.put(stationCode, MathUtil.formatDouble(daycapacityCache.get(), 0d));
                    continue;
                }
                
                p.close();
                double realValue = realCaptialMap.containsKey(stationCode) && null != realCaptialMap.get(stationCode) ? realCaptialMap.get(stationCode) : 0D;
                double history = capacityPower.containsKey(stationCode) ? capacityPower.get(stationCode) : 0D;
                if(realValue < history){
                    result.put(stationCode, history);
                }else{
                    result.put(stationCode, realValue);
                }
            }
            
            params.put(REALDAYCAPACITY, result);

        }catch(Exception e){
            return result;
        }finally{
            RedisUtil.closeJeids(j);
        }
        
        return result;
    }

    public DayCapacityHandler() {
        
    }

    public DayCapacityHandler(String prex) {
        super(prex);
    }

}
