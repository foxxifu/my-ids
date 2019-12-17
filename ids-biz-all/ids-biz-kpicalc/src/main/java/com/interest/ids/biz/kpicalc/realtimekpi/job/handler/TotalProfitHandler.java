package com.interest.ids.biz.kpicalc.realtimekpi.job.handler;

import com.interest.ids.commoninterface.constant.realtimekpi.KpiItem;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unchecked","rawtypes"})
public class TotalProfitHandler extends JobHandler{
	@Override
    public Map excuteJob(Map<String, Object> params) throws Exception {
        Map<String,Double>  yearKpiMap = (Map<String,Double>) params.get(TOTALINCOMENOWDAY);
        List<String> sids = (List<String>)params.get("STATIONCODES");
        Double yearIncome = 0D;
        BigDecimal dayincome = BigDecimal.ZERO;
        Map<String,BigDecimal> result = new HashMap<String, BigDecimal>();
        Map<String,BigDecimal> dayIncomeMap = (Map<String,BigDecimal>)params.get(KpiItem.DAYINCOME.getVal());
        for (String stationCode : sids){
            yearIncome = (null != yearKpiMap && yearKpiMap.get(stationCode) != null) ? yearKpiMap.get(stationCode): 0D;
            dayincome = dayIncomeMap.get(stationCode);
            result.put(stationCode,dayincome.add(new BigDecimal(yearIncome)));
        }
        return result;
    }

    public TotalProfitHandler() {
    }

    public TotalProfitHandler(String handlerprefix) {
        super(handlerprefix);
    }
}
