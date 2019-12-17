package com.interest.ids.biz.kpicalc.realtimekpi.job.handler;

import com.interest.ids.common.project.bean.kpi.KpiStationMonthM;
import com.interest.ids.commoninterface.constant.realtimekpi.KpiItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"rawtypes","unchecked"})
public class TotalMonthJobHandler extends JobHandler{
    private static Logger log = LoggerFactory.getLogger(TotalMonthJobHandler.class);
   
	@Override
    public Map excuteJob(Map<String, Object> params) throws Exception {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, 1);
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.set(Calendar.MINUTE, 0);
        ca.set(Calendar.SECOND, 0);
        ca.set(Calendar.MILLISECOND, 0);
        Long startTime = ca.getTime().getTime();
        
		List<String> sids = (List<String>)params.get("STATIONCODES");
        //当月发电量
        log.info("queryStationMonthKpi params date:"+ startTime +",sids:"+sids);
        List<KpiStationMonthM> kpiMonth = getKpiService().queryStationMonthKpi(
                sids, startTime);
        Map<String, Double> kpiMonthMap = new HashMap<String, Double>();
        //关口电表数据
        Map<String,Double> dayConsumption = (Map<String,Double>) params.get(ACTIVECAPACITY);
        log.info("kpiMonth:"+kpiMonth);
        if (null != kpiMonth && kpiMonth.size() > 0) {
            Double oldProductPower;
            Double newProductPower;
            String sid;
            for (KpiStationMonthM kpiStationMonthM : kpiMonth) {
                sid = kpiStationMonthM.getStationCode();
                if(dayConsumption.containsKey(sid)){
                    newProductPower = (null == kpiStationMonthM.getOngridPower()) ? 0D :kpiStationMonthM.getOngridPower();
                } else{
                    newProductPower = (null == kpiStationMonthM.getProductPower()) ? 0D :kpiStationMonthM.getProductPower();
                }
                oldProductPower = (null == kpiMonthMap.get(sid)) ? 0D : kpiMonthMap.get(sid);
                kpiMonthMap.put(sid,oldProductPower+newProductPower);
            }
        }
        //
        Map<String,Double> dayProductPowerMap = (Map<String,Double>) params.get(KpiItem.DAYCAPACITY.getVal());
        Map<String,Double> result = new HashMap<String, Double>();
        Double totalMpnth;
        Double totalDay;
        for (String stationCode : sids){
            totalMpnth = (null == kpiMonthMap.get(stationCode)) ? 0D : kpiMonthMap.get(stationCode);
            totalDay = (null == dayProductPowerMap || null == dayProductPowerMap.get(stationCode)) ?
                    0D : dayProductPowerMap.get(stationCode);
            result.put(stationCode,totalMpnth+totalDay);
        }
        return result;
    }

    public TotalMonthJobHandler(){
        
    }
    
    public TotalMonthJobHandler(String handlerprefix){
        super(handlerprefix);
    }
}
