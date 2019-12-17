package com.interest.ids.biz.kpicalc.realtimekpi.job.handler;

import com.interest.ids.biz.kpicalc.realtimekpi.constant.KpiRealTimeConstant;
import com.interest.ids.common.project.constant.DevTypeConstant;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActivePowerHandler extends JobHandler {

    @Override
    public Map excuteJob(Map<String, Object> params) throws Exception {
        Map<String, Map<String, String>> signalMap = (Map<String, Map<String, String>>) params.get(SIGNAL);
        Map map = getSignalMap(signalMap, KpiRealTimeConstant.ACTIVEPOWER);
        if (null == map) {
            return null;
        }
        Set<Integer> devTypes = new HashSet<>();
        devTypes.add(DevTypeConstant.HOUSEHOLD_INVERTER_DEV_TYPE);
        devTypes.add(DevTypeConstant.INVERTER_DEV_TYPE);
        devTypes.add(DevTypeConstant.CENTER_INVERT_DEV_TYPE);
        Map<String, Double> power = getStationPropertyData(map, devTypes);
        return power;
    }

    public ActivePowerHandler(String handlerprefix) {
        super(handlerprefix);
    }

    public ActivePowerHandler() {
        
    }
}
