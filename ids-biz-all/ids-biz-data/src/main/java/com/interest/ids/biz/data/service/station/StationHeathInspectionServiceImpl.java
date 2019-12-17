package com.interest.ids.biz.data.service.station;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interest.ids.common.project.constant.DeviceConstant.ConnectStatus;
import com.interest.ids.common.project.constant.StationInfoConstant;
import com.interest.ids.common.project.spring.context.SystemContext;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.commoninterface.service.device.IDeviceInfoService;
import com.interest.ids.commoninterface.service.station.IStationHeathInspectionService;
import com.interest.ids.commoninterface.service.station.StationInfoMService;
import com.interest.ids.redis.caches.StationCache;
import com.interest.ids.redis.client.service.ConnStatusCacheClient;

@Service
public class StationHeathInspectionServiceImpl implements IStationHeathInspectionService {

    private static final Logger logger = LoggerFactory.getLogger(StationHeathInspectionServiceImpl.class);
    @Autowired
    private IDeviceInfoService deviceInfoService;

    @Autowired
    private StationInfoMService stationInfoMService;

    @Override
    public void inspectStationStatus() {

        logger.info("[inspectStationStatus] inspect station device running status.");

        List<String> allStationCodes = StationCache.getAllstationCodes();
        if (CommonUtil.isEmpty(allStationCodes)) {
            return;
        }

        Map<String, Integer> stationConnStatusMap = new HashMap<>();
        // 获取从监控上传的电站状态
        Map<String, String> monitorStationState = StationCache.getStationHealthState(StationCache
                .getAllMonitorStationCodes());
        List<String> monitorOffStations = new ArrayList<>();
        if (monitorStationState != null && monitorStationState.size() > 0) {
            for (String stationCode : monitorStationState.keySet()) {
                String state = monitorStationState.get(stationCode);
                if (String.valueOf(StationInfoConstant.DISCONECTED).equals(state)) {
                    monitorOffStations.add(stationCode);
                    
                    stationConnStatusMap.put(stationCode, StationInfoConstant.DISCONECTED);
                }
            }
            
            allStationCodes.removeAll(monitorOffStations);
        }

        // 1. 判断电站是否通讯中断
        ConnStatusCacheClient connStatuCache = (ConnStatusCacheClient) SystemContext.getBean("connCacheClient");
        Map<String, List<Long>> stationDevicesMap = deviceInfoService.queryAllDevicesMap(allStationCodes);

        if (CommonUtil.isNotEmpty(stationDevicesMap)) {

            // 电站下没有设备，默认为断连状态
            if (stationDevicesMap.size() < allStationCodes.size()) {
                for (String stationCode : allStationCodes) {
                    if (!stationDevicesMap.containsKey(stationCode)) {
                        stationConnStatusMap.put(stationCode, StationInfoConstant.DISCONECTED);
                    }
                }
            }

            for (String stationCode : stationDevicesMap.keySet()) {
                boolean connFlag = false;
                List<Long> deviceIdList = stationDevicesMap.get(stationCode);
                for (Long deviceId : deviceIdList) {
                    try {
                        ConnectStatus connectStatus = (ConnectStatus) connStatuCache.get(deviceId);
                        // 如果有一个设备连接，则电站状态标记为非通讯中断
                        if (connectStatus != null && connectStatus.equals(ConnectStatus.CONNECTED)) {
                            connFlag = true;
                            break;
                        }
                    } catch (Exception e) {
                        logger.error("[DeviceId=" + deviceId + "]can not get device connection status.", e);
                    }
                }

                if (!connFlag) {
                    stationConnStatusMap.put(stationCode, StationInfoConstant.DISCONECTED);
                }
            }
        }

        // 2. 检查电站下是否存在严重告警
        Map<String, Integer> stationWithTrouble = new HashMap<>();
        // 排除电站已标记为通讯中断的电站
        if (allStationCodes.size() > stationConnStatusMap.size()) {
            List<String> excepDisconnStationList = new ArrayList<>();
            for (String stationCode : allStationCodes) {
                if (!stationConnStatusMap.containsKey(stationCode)) {
                    excepDisconnStationList.add(stationCode);
                }
            }

            // 查询电站是否存在故障
            List<String> stationWithCriAlarmList = stationInfoMService
                    .getStationWithCriticalAlarm(excepDisconnStationList);
            if (CommonUtil.isNotEmpty(stationWithCriAlarmList)) {
                for (String stationCode : stationWithCriAlarmList) {
                    stationWithTrouble.put(stationCode, StationInfoConstant.TROUBLE);
                }
            }
        }

        // 3. 移除通讯中断及故障电站
        allStationCodes.removeAll(stationConnStatusMap.keySet());
        allStationCodes.removeAll(stationWithTrouble.keySet());
        Map<String, Integer> result = new HashMap<>();
        for (String stationCode : allStationCodes) {
            result.put(stationCode, StationInfoConstant.HEALTHY);
        }

        result.putAll(stationConnStatusMap);
        result.putAll(stationWithTrouble);

        StationCache.updateStationState(result);
    }
}
