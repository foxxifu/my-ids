package com.interest.ids.biz.kpicalc.realtimekpi.service.impl;

import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.commoninterface.constant.realtimekpi.KpiItem;
import com.interest.ids.commoninterface.service.device.IDevPVCapacityService;
import com.interest.ids.commoninterface.service.kpi.IRealTimeKpiService;
import com.interest.ids.redis.caches.RealTimeKpiCache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service("realTimeKpiService")
public class RealTimeKpiServiceImpl implements IRealTimeKpiService{

    private static final Logger logger = LoggerFactory.getLogger(RealTimeKpiServiceImpl.class);

    @Autowired
    private IDevPVCapacityService pvCapacityService;

    @Override
    public Map<String, Map<String, Object>> getRealTimeKpi(
            List<String> stationCodes) {
        if( null == stationCodes || stationCodes.size() == 0){
            return null;
        }
        @SuppressWarnings("unchecked")
        Map<String, Map<String, Object>> kpiMap = RealTimeKpiCache
                .getRealtimeKpiCache(stationCodes);
        if(null == kpiMap || kpiMap.size() == 0){
            return null;
        }
        Map<String,Object> stationMap;
        Double capacity = 0D;
        Double installCapacity = 0D;
        Map<String,Double> installCapacityMap = pvCapacityService.getStationPVCapByList(stationCodes);
        for (String stationCode : stationCodes){
            stationMap = kpiMap.get(stationCode);
            installCapacity = null == installCapacityMap || null == installCapacityMap.get(stationCode)
                    ? 0d : installCapacityMap.get(stationCode);
            capacity = ( null != stationMap && stationMap.containsKey(KpiItem.DAYCAPACITY.getVal()))
                    ? Double.parseDouble((stationMap.get(KpiItem.DAYCAPACITY.getVal())).toString()) : 0d;
            if(0 == installCapacity){
                stationMap.put(KpiItem.EQUIVALENTNUMBEROFHOURS.getVal(),0);
            } else{
                stationMap.put(KpiItem.EQUIVALENTNUMBEROFHOURS.getVal(),capacity/installCapacity);
            }
            kpiMap.put(stationCode,stationMap);
        }
        return kpiMap;
    }

    @Override
    public Map<String, Object> getRealTimeKpiTotal(List<String> stationCodes) {
        Map<String, Map<String, Object>> realTimeKpi = this
                .getRealTimeKpi(stationCodes);
        if (null == realTimeKpi || realTimeKpi.size() == 0) {
            return new HashMap<>();
        }
        Double power = 0D;
        Double dayCapacity = 0D;
        Double totalCapacity = 0D;
        BigDecimal dayIncome = BigDecimal.ZERO;
        BigDecimal totalIncome = BigDecimal.ZERO;
        Set<String> sids = realTimeKpi.keySet();
        Map<String, Object> kpimap;

        for (String stationCode : sids) {
            kpimap = realTimeKpi.get(stationCode);
            if (null == kpimap) {
                continue;
            }
            power += Double
                    .parseDouble(kpimap.get(KpiItem.ACTIVEPOWER.getVal()) == null ? "0"
                            : kpimap.get(KpiItem.ACTIVEPOWER.getVal()).toString());
            dayCapacity += Double.parseDouble(kpimap.get(KpiItem.DAYCAPACITY
                    .getVal()) == null ? "0" : kpimap.get(
                    KpiItem.DAYCAPACITY.getVal()).toString());
            totalCapacity += Double.parseDouble(kpimap
                    .get(KpiItem.TOTALCAPACITY.getVal()) == null ? "0" : kpimap
                    .get(KpiItem.TOTALCAPACITY.getVal()).toString());
            dayIncome = dayIncome.add(new BigDecimal(kpimap
                    .get(KpiItem.DAYINCOME.getVal()) == null ? "0" : kpimap
                    .get(KpiItem.DAYINCOME.getVal()).toString()));
            totalIncome = totalIncome.add(new BigDecimal(kpimap
                    .get(KpiItem.TOTALINCOME.getVal()) == null ? "0" : kpimap
                    .get(KpiItem.TOTALINCOME.getVal()).toString()));
        }
        kpimap = new HashMap<>();
        kpimap.put(KpiItem.ACTIVEPOWER.getVal(), power);
        kpimap.put(KpiItem.DAYCAPACITY.getVal(), dayCapacity);
        kpimap.put(KpiItem.TOTALCAPACITY.getVal(), totalCapacity);
        kpimap.put(KpiItem.DAYINCOME.getVal(), dayIncome);
        kpimap.put(KpiItem.TOTALINCOME.getVal(), totalIncome);
        return kpimap;
    }

    @Override
    public Map<String, Double> getEquivalentUserFulHour(
            List<StationInfoM> stationInfoMs) {
        if (null == stationInfoMs || stationInfoMs.size() == 0) {
            logger.warn("no station for query.");
            return null;
        }
        Map<String, Object> map = this.getKpiByItem(stationInfoMs,
                KpiItem.EQUIVALENTNUMBEROFHOURS);
        Map<String, Double> sortMap = new HashMap<>();
        Double dayCapacity = 0d;
        if(null != map && 0 != map.size() ){
            String stationCode;
            String stationName;
            for(StationInfoM stationInfoM : stationInfoMs){
                stationCode = stationInfoM.getStationCode();
                dayCapacity = null == map.get(stationCode)?0d:Double.parseDouble(map.get(stationCode).toString());
                stationName = stationInfoM.getStationName();
                sortMap.put(stationName,dayCapacity);
            }
        }
        return sortByValue(sortMap);
    }

    @Override
    public Map<String, Object> getKpiByItem(List<StationInfoM> stationInfoMs,
                                            KpiItem item) {

        if (null == stationInfoMs || stationInfoMs.size() == 0) {
            return null;
        }
        List<String> sids = new ArrayList<>();
        for (StationInfoM stationInfoM : stationInfoMs) {
            sids.add(stationInfoM.getStationCode());
        }
        Map<String, Map<String, Object>> kpimap = this.getRealTimeKpi(sids);
        Map<String, Object> resultMap = new HashMap<>();
        if (null != kpimap) {
            String stationCode;
            Object Val;
            Map<String, Object> temp;
            for (StationInfoM stationInfoM : stationInfoMs) {
                stationCode = stationInfoM.getStationCode();
                temp = kpimap.containsKey(stationCode) ? kpimap
                        .get(stationCode) : null;
                if (null == temp) {
                    Val = 0D;
                } else {
                    Val = temp.get(item.getVal());
                }
                resultMap.put(stationCode, Val);
            }
        }
        return resultMap;
    }

    /**
     * 历史总发电量
     * @param stationCodes
     * @return
     */
    @Override
    public Map<String,Object> getCurrentTotalCapacityByStationCode(List<String> stationCodes){
        @SuppressWarnings("unchecked")
        Map<String, Map<String, Object>> kpiMap = RealTimeKpiCache
                .getRealtimeKpiCache(stationCodes, KpiItem.TOTALCAPACITY.getVal());
        Map<String, Object> resultMap = new HashMap<>();
        if (null != kpiMap) {
            Object Val;
            Map<String, Object> temp;
            for (String stationCode : stationCodes) {
                temp = kpiMap.containsKey(stationCode) ? kpiMap
                        .get(stationCode) : null;
                if (null == temp) {
                    Val = 0D;
                } else {
                    Val = temp.get(KpiItem.TOTALCAPACITY.getVal());
                }
                resultMap.put(stationCode, Val);
            }
        }
        return resultMap;
    }

    /**
     * Map按照Value排序
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(
            Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });
        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

}
