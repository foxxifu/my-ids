package com.interest.ids.biz.kpicalc.realtimekpi.job.handler;

import com.interest.ids.biz.kpicalc.realtimekpi.constant.KpiRealTimeConstant;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.constant.StationInfoConstant;

import java.util.*;

/**
 * 当日上网电量取电表的上网电量
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DayActiveCapacityHandler extends JobHandler {

    @Override
    public Map excuteJob(Map<String, Object> params) {
         Map<String, String> stationSignalMap = new HashMap();
        Set<Integer> devTypes = new HashSet<>();
        devTypes.add(DevTypeConstant.GATEWAYMETER_DEV_TYPE_ID);
        devTypes.add(DevTypeConstant.HOUSEHOLD_METER);
        List<StationInfoM> stationInfoMList = (List<StationInfoM>) params.get("STATIONINFOS");

        // 不统计户用电表
        for (StationInfoM stationInfoM : stationInfoMList) {
            String stationCode = stationInfoM.getStationCode();
            if (StationInfoConstant.COMBINE_TYPE_HUYONG.equals(stationInfoM.getOnlineType())) {
                continue;
            }
            stationSignalMap.put(stationCode, KpiRealTimeConstant.ACTIVECAP);
        }
        List<String> sids = (List<String>) params.get("STATIONCODES");

        // 获取最近有效上网电量
        Map<String, Map<String, Double>> realCaptialMap = getDeviceRealTimeKpi(stationSignalMap, devTypes);
        Map<String, Double> dayCapacity = (Map<String, Double>) params.get(REALDAYCAPACITY);
        
        Long endTime = (Long) params.get("CURRENT_TIME");
        Long beginTime = (Long) params.get("ZERO_TIME");
        List<Integer> types = new ArrayList<>();
        types.addAll(devTypes);

        // 获取今日上网电量的第一个点有效的点
        //TODO 可将今日上网电量第一个点值放入缓存中
        Map<String, Double> firstMap = getKpiService().queryMeterDataByField(sids, types, KpiRealTimeConstant.ACTIVECAP,
                beginTime, endTime);
        boolean isFirstEmpt = false;
        if (null == firstMap || firstMap.size() == 0){
            isFirstEmpt = true;
        }
        
        Map<String, Double> result = new HashMap<>();
        Set<String> keys = realCaptialMap.keySet();
        Double val;
        Double firstVal;
        Map<String, Double> tempMap;
        Set<String> deviceIdSet;
        Double tempTotal;
        for (String stationCode : keys) {
            tempTotal = 0d;
            tempMap = realCaptialMap.get(stationCode);
            if (null != tempMap && tempMap.size() > 0) {
                deviceIdSet = tempMap.keySet();
                for (String deviceId : deviceIdSet) {
                    val = tempMap.get(deviceId) == null ? 0d : tempMap.get(deviceId);
                    if (isFirstEmpt) {
                        firstVal = val;
                    } else {
                        firstVal = firstMap.get(deviceId) == null ? val : firstMap.get(deviceId);
                    }
                    tempTotal += (val - firstVal);
                }
            }
            
            if (tempTotal > 0) {
                result.put(stationCode, tempTotal);
            }else {
                //日上网电量<=0 时取逆变器发电量
                result.put(stationCode, dayCapacity.get(stationCode));
            }
        }
        params.put(ACTIVECAPACITY, result);
        return result;
    }

    public DayActiveCapacityHandler() {

    }

    public DayActiveCapacityHandler(String prex) {
        super(prex);
    }

}
