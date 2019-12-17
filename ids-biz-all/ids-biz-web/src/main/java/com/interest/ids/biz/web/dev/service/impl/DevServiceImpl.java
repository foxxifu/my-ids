package com.interest.ids.biz.web.dev.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interest.ids.biz.web.alarm.service.AlarmService;
import com.interest.ids.biz.web.constant.ProtocolConstant;
import com.interest.ids.biz.web.dev.service.DevService;
import com.interest.ids.common.project.bean.device.DeviceProfileDto;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.DeviceConstant.ConnectStatus;
import com.interest.ids.common.project.constant.StationInfoConstant;
import com.interest.ids.common.project.mapper.signal.DevInfoMapper;
import com.interest.ids.commoninterface.service.station.StationInfoMService;
import com.interest.ids.dev.api.localcache.SignalLocalCache;
import com.interest.ids.redis.caches.StationCache;
import com.interest.ids.redis.client.service.ConnStatusCacheClient;
import com.interest.ids.redis.client.service.SignalCacheClient;

/**
 * @Author: sunbjx
 * @Description:
 * @Date: Created in 上午10:36 18-2-2
 * @Modified By:
 */
@Service
public class DevServiceImpl implements DevService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DevServiceImpl.class);

    @Autowired
    private SignalCacheClient signalCacheClient;
    @Autowired
    private ConnStatusCacheClient statusCacheClient;
    @Autowired
    private DevInfoMapper devInfoMapper;
    @Autowired
    private StationInfoMService stationService;
    @Autowired
    private AlarmService alarmService;

    @Override
    public Map<String, Object> getDevDataByCache(long deviceId, Set<String> signalKeys) {

        Map<String, Object> result = new HashMap<>();

        if (signalKeys == null || signalKeys.size() == 0) {
            LOGGER.warn("The Device: {} doesn't config normalized info.", deviceId);
            return result;
        }

        result = signalCacheClient.batchGet(deviceId, signalKeys);

        return result;
    }

    @Override
    public List<String> removeByIds(List<Long> ids) {
    	List<String> snList = new ArrayList<>();
        DeviceInfo dev;
        try {
            for (Long id : ids) {
                dev = devInfoMapper.selectByPrimaryKey(id);
                if (null == dev)
                    continue;
                // 如果删除的设备中包括modbus设备的话，需要返回设备的sn号，用于删除java缓存中的信号点
                if("MODBUS".equals(dev.getProtocolCode())){
                	snList.add(dev.getSnCode());
                }
                dev.setIsLogicDelete(true);
                devInfoMapper.updateByPrimaryKey(dev);
                // MODBUS设备需要删除信号点缓存，因为设备再次发现会有相同的sn号
                if(ProtocolConstant.MODBUS.equals(dev.getProtocolCode()) && dev.getDevTypeId() == 1 && null != dev.getSnCode()){
                	String key = "*" +signalCacheClient.generateKeyToVagueDel(dev.getId()) + "*";
                	LOGGER.info("生成的key: {}",key);
                	signalCacheClient.batchVagueDel(key);
                }
            }
            return snList;
        } catch (Exception e) {
            LOGGER.error("Update error: ", e);
            return null;
        }
    }

    @Override
    public List<DeviceProfileDto> getDevProfile(UserInfo user, String stationCode) {
        List<DeviceProfileDto> resultList = null;

        // 获取各类型设备的数量
        List<DeviceProfileDto> queryRestul = devInfoMapper.selectDevProfile(user == null ? null : user.getId(), 
        		user == null ? null : user.getEnterpriseId(), stationCode, user == null ? null : user.getType_());
        if (queryRestul != null && queryRestul.size() > 0) {

            List<String> stationCodes = new ArrayList<>();
            if (stationCode == null || stationCode.length() == 0) {
                List<StationInfoM> stations = stationService.getStationByUser(user);
                if (stations != null) {
                    for (StationInfoM station : stations) {
                        stationCodes.add(station.getStationCode());
                    }
                } 
            }else{
                stationCodes.add(stationCode);
            }

            // 查询管理电站设备
            List<DeviceInfo> allDevices = devInfoMapper.selectDevicesWithStationCodes(stationCodes);
            Map<String, String> stationState = StationCache.getStationHealthState(stationCodes);
            List<DeviceInfo> disConnDevices = new ArrayList<>(); // 断连设备
            List<Long> connDevIds = new ArrayList<>();
            for (DeviceInfo device : allDevices) {
                String state = stationState.get(device.getStationCode());
                ConnectStatus connStatus = statusCacheClient.get(device.getId());
                if (ConnectStatus.DISCONNECTED.equals(connStatus) || connStatus == null ||
                        String.valueOf(StationInfoConstant.DISCONECTED).equals(state)) {
                    disConnDevices.add(device);
                } else {
                    connDevIds.add(device.getId());
                }
            }

            List<DeviceInfo> troubleDevices = new ArrayList<>();
            List<DeviceInfo> heathDevices = new ArrayList<>();
            // 查询已连接设备是否存在严重告警
            if (connDevIds.size() > 0) {
                Map<Long, Integer> deviceAlarmCount = alarmService.countDeviceCriticalAlarmsByDeviceId(connDevIds);
                // 移除断连的设备，根据设备是否存在严重告警，判断设备是否故障
                List<DeviceInfo> temp = new ArrayList<>(allDevices);
                temp.removeAll(disConnDevices);

                for (DeviceInfo device : temp) {
                    if (deviceAlarmCount.containsKey(device.getId())) {
                        troubleDevices.add(device);
                    }
                }

                // 移除断连、故障设备，剩下为正常设备;
                temp.removeAll(troubleDevices);
                heathDevices = temp;
            }

            // 统计各类型设备，各状态数量
            Map<Integer, Integer> devStatus = null;
            resultList = new ArrayList<>();
            for (DeviceProfileDto devProfile : queryRestul) {
                Integer devTypeId = devProfile.getDevTypeId();

                int disConnNum = 0;
                int heathNum = 0;
                int troubNum = 0;
                for (DeviceInfo device : disConnDevices) {
                    if (device.getDevTypeId() == devTypeId) {
                        disConnNum++;
                    }
                }

                for (DeviceInfo device : heathDevices) {
                    if (device.getDevTypeId() == devTypeId) {
                        heathNum++;
                    }
                }

                for (DeviceInfo device : troubleDevices) {
                    if (device.getDevTypeId() == devTypeId) {
                        troubNum++;
                    }
                }

                devStatus = new HashMap<>();
                devStatus.put(StationInfoConstant.DISCONECTED, disConnNum);
                devStatus.put(StationInfoConstant.HEALTHY, heathNum);
                devStatus.put(StationInfoConstant.TROUBLE, troubNum);

                devProfile.setDevStatus(devStatus);
                
                resultList.add(devProfile);
            }
        }

        return resultList;
    }

}
