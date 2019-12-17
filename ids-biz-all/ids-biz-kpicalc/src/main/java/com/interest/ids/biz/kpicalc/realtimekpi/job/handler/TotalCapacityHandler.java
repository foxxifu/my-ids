package com.interest.ids.biz.kpicalc.realtimekpi.job.handler;

import com.interest.ids.common.project.bean.kpi.KpiStationYearM;
import com.interest.ids.commoninterface.constant.realtimekpi.KpiItem;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"rawtypes","unchecked"})
public class TotalCapacityHandler extends JobHandler{
    
	@Override
    public Map excuteJob(Map<String, Object> params) throws Exception {
        List<String> sids = (List<String>)params.get("STATIONCODES");
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_YEAR, 1);
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.set(Calendar.MINUTE, 0);
        ca.set(Calendar.SECOND, 0);
        Long startTime = ca.getTime().getTime();
        List<KpiStationYearM> yearKpiList = getKpiService().queryStationYearKpi(sids, 0L,startTime);
        Map<String,Double> yearKpiIncomeMap = null;
        Map<String,Double> yearKpiProductPowerMap = null;
        Map<String,Double> result = new HashMap<>();
        //关口电表数据
//        Map<String,Double> dayConsumption = (Map<String,Double>) params.get(POWERCONSUMPTION);
        if(yearKpiList != null && yearKpiList.size() > 0){
            yearKpiIncomeMap = new HashMap<String,Double>();
            yearKpiProductPowerMap = new HashMap<String,Double>();
            String sid;
            Double oldValueIncome;
            Double newValueIncome;
            Double oldProductPower;
            Double newProductPower;
            for(KpiStationYearM kpiStationYearM :yearKpiList){
                sid = kpiStationYearM.getStationCode();
                //收益
                oldValueIncome = (null == yearKpiIncomeMap.get(sid)) ? 0D : yearKpiIncomeMap.get(sid);
                newValueIncome = (null == kpiStationYearM.getPowerProfit()) ? 0D:kpiStationYearM.getPowerProfit();
                yearKpiIncomeMap.put(sid,oldValueIncome+newValueIncome);
                //发电量
                oldProductPower = (null == yearKpiProductPowerMap.get(sid)) ? 0D : yearKpiProductPowerMap.get(sid);
//                if(dayConsumption.containsKey(sid)){
//                    newProductPower = (null == kpiStationYearM.getOnGridPower()) ? 0D:kpiStationYearM.getOnGridPower();
//                } else{
                    newProductPower = (null == kpiStationYearM.getProductPower()) ? 0D:kpiStationYearM.getProductPower();
//                }
                yearKpiProductPowerMap.put(sid,oldProductPower+newProductPower);
            }
        }

        Map<String,Double> dayCapacity = (Map<String, Double>) params
                .get(KpiItem.DAYCAPACITY.getVal());
        Double dayProductPower;
        Double totalProductPower;
        for (String sid : sids){
            dayProductPower = (null == dayCapacity || null == dayCapacity.get(sid)) ? 0d :dayCapacity.get(sid);
            totalProductPower = (null == yearKpiProductPowerMap || null == yearKpiProductPowerMap.get(sid) ) 
            		? 0d : yearKpiProductPowerMap.get(sid);
            result.put(sid,dayProductPower+totalProductPower);
        }
        params.put(TOTALINCOMENOWDAY,yearKpiIncomeMap);

        return result;
    }

    public TotalCapacityHandler() {
    }

    public TotalCapacityHandler(String handlerprefix) {
        super(handlerprefix);
    }
}
