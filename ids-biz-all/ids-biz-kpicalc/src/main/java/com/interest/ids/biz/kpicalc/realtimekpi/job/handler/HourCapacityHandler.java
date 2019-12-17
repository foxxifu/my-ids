package com.interest.ids.biz.kpicalc.realtimekpi.job.handler;


import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.commoninterface.constant.realtimekpi.KpiItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 当前小时发电量计算
 */
@SuppressWarnings({ "rawtypes", "unchecked","unused" })
public class HourCapacityHandler extends JobHandler {
    private Logger log = LoggerFactory.getLogger(HourProfitHandler.class);
    @Override
    public Map excuteJob(Map<String, Object> params) {

        Map<String, Double> realCaptialMap = (Map<String, Double>) params
                .get(KpiItem.DAYCAPACITY.getVal());

        List<StationInfoM> stationInfoMList = (List<StationInfoM>) params
                .get("STATIONINFOS");
        Map<String, Double> capacityPower = (Map<String, Double>) params
                .get(SUMHOURCAPACITY);
        // 判断最新的值与前一个小时发电量值
        // 如果最新值大于 前一小时发电量 则 当前小时发电量=最新值-当前小时发电量。
        // 如果最新值小于前一小时发电量 则当前小时发电量 = 历史当前小时发电量 + 最新值
        Double now;
        Double history;
        Map<String, Double> resultMap = new HashMap<>();
        Set<String> sids = realCaptialMap.keySet();
        for (String sid : sids) {
            log.info("HourCapacityHandler sid is: "+sid);
            now = realCaptialMap.containsKey(sid) ? realCaptialMap.get(sid)
                    : 0D;
            history = capacityPower.containsKey(sid) ? capacityPower.get(sid)
                    : 0D;
            log.info("HourCapacityHandler now is: "+now);
            log.info("HourCapacityHandler history is: "+history);
            if (now >= history) {
                resultMap.put(sid, (now - history));
            } else {
                resultMap.put(sid, 0D);
            }

        }
        return resultMap;
    }

    public HourCapacityHandler() {

    }

    public HourCapacityHandler(String prex) {
        super(prex);
    }
}
