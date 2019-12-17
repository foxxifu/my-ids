package com.interest.ids.biz.kpicalc.realtimekpi.job.handler;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.commoninterface.constant.realtimekpi.KpiItem;
import com.interest.ids.redis.caches.StationPowerPriceCache;

/**
 * 当前小时收益计算
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class HourProfitHandler extends JobHandler {
    private Logger log = LoggerFactory.getLogger(HourProfitHandler.class);

    @Override
    public Map excuteJob(Map<String, Object> params) throws Exception {

        Map<String, Double> currentHourCaptial = (Map<String, Double>) params.get(KpiItem.HOURCAPACITY.getVal());
        // 结果Map
        Map<String, BigDecimal> currentInComeMap = new HashMap<String, BigDecimal>();
        if(currentHourCaptial != null){
            // 当前小时电价
            Long currentTime = params.get("CURRENT_TIME") == null ? System.currentTimeMillis() : 
                (Long)params.get("CURRENT_TIME");
            
            // 电站编号KeySet
            Set<String> stationKeySet = currentHourCaptial.keySet();
            boolean flag;
            BigDecimal price;
            for (String stationCode : stationKeySet){
                log.info("HourProfitHandler stationId: "+stationCode);
                log.info("HourProfitHandler currentHourCaptial: "+currentHourCaptial.get(stationCode));
                flag = true;
                // 获取发电量
                Double captial = (null == currentHourCaptial.get(stationCode)) ? 0D
                        : currentHourCaptial.get(stationCode);
                // 获取电价设置
                price = new BigDecimal(StationPowerPriceCache.getStationPowerPrice(stationCode, currentTime));
                
                // 单位
                // String currency;
                if(captial < 0){
                    flag = false;
                }
                // 收益计算 ：发电量*电价
                log.info("HourProfitHandler captial is: "+captial.toString());
                log.info("HourProfitHandler price is: "+price.toString());
                captial = Math.abs(captial);
                BigDecimal hourIncome = price.multiply(new BigDecimal(captial));
                log.info("HourProfitHandler hourIncome is: "+hourIncome.toString());
                if(flag){
                    currentInComeMap.put(stationCode, hourIncome);
                }
                else{
                    currentInComeMap.put(stationCode, hourIncome.negate());
                }
            }
        }else{
            log.info("currentHourCaptial is null");
        }
        return currentInComeMap;
    }

    public HourProfitHandler() {

    }

    public HourProfitHandler(String prex) {
        super(prex);
    }

}
