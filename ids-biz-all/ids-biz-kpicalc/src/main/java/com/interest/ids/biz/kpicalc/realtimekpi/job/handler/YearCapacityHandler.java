package com.interest.ids.biz.kpicalc.realtimekpi.job.handler;

import com.interest.ids.common.project.bean.kpi.KpiStationYearM;
import com.interest.ids.commoninterface.constant.realtimekpi.KpiItem;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class YearCapacityHandler extends JobHandler {

    @Override
    public Map excuteJob(Map<String, Object> params) throws Exception {
        List<String> stationCodes = (List<String>) params.get("STATIONCODES");
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_YEAR, 1);
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.set(Calendar.MINUTE, 0);
        ca.set(Calendar.SECOND, 0);
        ca.set(Calendar.MILLISECOND, 0);
        Long startTime = ca.getTime().getTime();
        List<KpiStationYearM> yearKpiList = getKpiService().queryStationYearKpi(stationCodes, startTime);
        // 关口电表数据
        // Map<String,Double> dayConsumption = (Map<String,Double>)
        // params.get(POWERCONSUMPTION);
        Map<String, Double> yearKpiMap = null;
        Map<String, Double> result = new HashMap<>();
        if (yearKpiList != null && yearKpiList.size() > 0) {
            yearKpiMap = new HashMap<String, Double>();
            String stationCode;
            for (KpiStationYearM kpiStationYearM : yearKpiList) {
                stationCode = kpiStationYearM.getStationCode();
                // if(dayConsumption.containsKey(sid)){
                // yearKpiMap.put(sid,kpiStationYearM.getUsePower());
                // } else{
                yearKpiMap.put(stationCode, kpiStationYearM.getProductPower());
                // }
            }
        }
        Double yearCapacity = 0d;
        Double dayCapacity = 0d;
        Map<String, Double> dayCapacityMap = (Map<String, Double>) params.get(KpiItem.DAYCAPACITY.getVal());
        for (String stationCode : stationCodes) {
            yearCapacity = (null == yearKpiMap || yearKpiMap.get(stationCode) == null) ? 0D : yearKpiMap
                    .get(stationCode);
            dayCapacity = dayCapacityMap.get(stationCode);
            result.put(stationCode, dayCapacity + yearCapacity);
        }
        return result;
    }

    public YearCapacityHandler(String handlerprefix) {
        super(handlerprefix);
    }

    public YearCapacityHandler() {
    }
}
