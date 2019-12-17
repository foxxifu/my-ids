package com.interest.ids.biz.kpicalc.realtimekpi.job.handler;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.spring.context.SystemContext;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.commoninterface.service.cache.IDevCacheService;
import com.interest.ids.commoninterface.service.device.IDevPVCapacityService;
import com.interest.ids.commoninterface.service.kpi.IKpiCommonService;
import com.interest.ids.redis.caches.DeviceCache;
import com.interest.ids.redis.caches.RealTimeKpiCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@SuppressWarnings("rawtypes")
public abstract class JobHandler {

    protected JobHandler nextJobHandler;
    public String handlerPrefix;

    private IDevCacheService devCacheService;
    private IKpiCommonService kpiService;
    private IDevPVCapacityService devPVCapacityService;

    private static final Logger log = LoggerFactory.getLogger(JobHandler.class);

    public static final String ACTIVECAPACITY = "activeCapacity";

    public static final String SIGNAL = "signal";

    public static final String KPISTATIONHOURMS = "kpiStationHourMs";

    public static final String SUMHOURCAPACITY = "sumHourCapacity";
    
    public static final String REALDAYCAPACITY = "sumDayCapacity";

    public static final String TOTALINCOMENOWDAY = "totalIncomeNowDay";

    JobHandler() {

    }

    JobHandler(String handlerprefix) {
        this.handlerPrefix = handlerprefix;
    }

    public void setNextJobHandler(JobHandler nextJobHandler) {
        this.nextJobHandler = nextJobHandler;
    }

    public void setHandlerprefix(String handlerprefix) {
        this.handlerPrefix = handlerprefix;
    }

    public Map handleRequest(Map<String, Object> params) throws Exception {
        Map result = excuteJob(params);
        if (null == result) {
            log.warn(handlerPrefix + " return null");
            return null;
        }

        params.put(handlerPrefix, result);

        if (null != nextJobHandler) {
            return nextJobHandler.handleRequest(params);
        } else {
            return params;
        }

    }

    /**
     * 实时KPI统计处理入口方法：具体在子类实现
     * @param params
     * @return
     * @throws Exception
     */
    protected abstract Map excuteJob(Map<String, Object> params) throws Exception;

    /**
     * 获取电站编号和信号点对应关系
     * 
     * @param signalMap
     * @param item 具体取得点
     * @return
     */
    public Map<String, String> getSignalMap(Map<String, Map<String, String>> signalMap, String item) {
        Map<String, String> paramMap = new HashMap<String, String>();
        if (null == signalMap || signalMap.size() == 0) {
            return null;
        }

        for (String stationCode : signalMap.keySet()) {
            String signalCode = signalMap.get(stationCode).get(item);
            paramMap.put(stationCode, signalCode);
        }
        return paramMap;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Double> getStationPropertyData(Map<String, String> map, Set<Integer> types) {
        Map<String, Double> result = new HashMap<String, Double>();
        if (null != map && map.size() >= 0) {
            Set<String> stationCodes = map.keySet();
            List<DeviceInfo> devCommonInfoMs;
            Map<Long, String> valueMap = null;
            Map<Long, Double> snapshotMap;
            Map<String, List<DeviceInfo>> allDevs = getDevCacheService().getDevsByStationAndTypes(stationCodes, types);
            for (String stationCode : stationCodes) {
                String propertyCode = map.get(stationCode);
                Double totalVal = null;
                Map<Long, Double> correctMap = new HashMap<Long, Double>();
                devCommonInfoMs = allDevs.get(stationCode);
                try {
                    valueMap = DeviceCache.getPropertiesByDevsAndCode(devCommonInfoMs, propertyCode);
                } catch (Exception e) {
                    log.error("get propertyData error", e);
                }
                
                // 获取上一次快照
                snapshotMap = RealTimeKpiCache.getSnapshot(devCommonInfoMs, propertyCode);
                if (null != valueMap && valueMap.size() > 0) {
                    Set<Long> devIds = valueMap.keySet();
                    String valueStr;
                    Double value;
                    totalVal = 0d;
                    for (Long devId : devIds) {
                        valueStr = valueMap.get(devId);
                        if (null != valueStr) {
                            String[] valueArray = valueStr.split("_");
                            // 0表示有效值
                            if (valueStr.indexOf("_") < 0 || "0".equals(valueArray[1])) {
                                value = Double.parseDouble(valueArray[0]);
                                correctMap.put(devId, value);
                            } else {
                                value = null == snapshotMap.get(devId) ? 0D : snapshotMap.get(devId);
                            }
                            totalVal += value;
                        }
                    }
                    // 有值需要更新
                    if (null != correctMap && correctMap.size() > 0) {
                        RealTimeKpiCache.updateSnapshot(correctMap, propertyCode);
                    }
                }
                result.put(stationCode, totalVal);
            }
        }
        return result;
    }

    /**
     * 返回设备实时值
     * 
     * @param stationSignalMap
     * @param types
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Map<String, Double>> getDeviceRealTimeKpi(Map<String, String> stationSignalMap, Set<Integer> types) {
        Map<String, Map<String, Double>> result = new HashMap<>();
        Map<String, Double> resultTemp = new HashMap<>();

        if (CommonUtil.isNotEmpty(stationSignalMap)) {
            List<DeviceInfo> deviceInfoList = null;
            Map<Long, String> valueMap = null;
            Map<Long, Double> snapshotMap = null;

            Set<String> stationCodes = stationSignalMap.keySet();
            Map<String, List<DeviceInfo>> allDevs = getDevCacheService().getDevsByStationAndTypes(stationCodes, types);

            for (String stationCode : stationCodes) {
                String signalColumn = stationSignalMap.get(stationCode);
                Map<Long, Double> correctMap = new HashMap<Long, Double>();
                deviceInfoList = allDevs.get(stationCode);
                try {
                    valueMap = DeviceCache.getPropertiesByDevsAndCode(deviceInfoList, signalColumn);
                } catch (Exception e) {
                    log.error("get propertyData error", e);
                }

                // 获取上一次快照
                snapshotMap = RealTimeKpiCache.getSnapshot(deviceInfoList, signalColumn);
                if (null != valueMap && valueMap.size() > 0) {
                    Set<Long> devIds = valueMap.keySet();
                    String valueStr;
                    Double value = 0d;
                    for (Long devId : devIds) {
                        valueStr = valueMap.get(devId);
                        if (null != valueStr) {
                            String[] valueArray = valueStr.split("_");
                            // 0表示有效值
                            if (valueStr.indexOf("_") < 0 || "0".equals(valueArray[1])) {
                                value = Double.parseDouble(valueArray[0]);
                                correctMap.put(devId, value);
                            } else {
                                value = null == snapshotMap.get(devId) ? 0D : snapshotMap.get(devId);
                            }
                        }
                        resultTemp.put(devId.toString(), value);
                    }

                    result.put(stationCode, resultTemp);

                    // 有值需要更新
                    if (null != correctMap && correctMap.size() > 0) {
                        RealTimeKpiCache.updateSnapshot(correctMap, signalColumn);
                    }
                }

            }
        }

        return result;
    }

    public IDevCacheService getDevCacheService() {
        if (null == devCacheService) {
            devCacheService = (IDevCacheService) SystemContext.getBean("deviceCacheService");
        }
        return devCacheService;
    }

    public IKpiCommonService getKpiService() {
        if (null == kpiService) {
            kpiService = (IKpiCommonService) SystemContext.getBean("kpiCommonService");
        }
        return kpiService;

    }

    public IDevPVCapacityService getDevPVCapacityService() {
        if (null == devPVCapacityService) {
            devPVCapacityService = (IDevPVCapacityService) SystemContext.getBean("devPvCapacityService");
        }
        return devPVCapacityService;
    }
}
