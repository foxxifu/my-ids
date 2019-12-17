package com.interest.ids.redis.caches;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.commoninterface.service.cache.IDevCacheService;
import com.interest.ids.commoninterface.service.device.IDeviceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("deviceCacheService")
public class DevCacheServiceImpl implements IDevCacheService{

    @Autowired
    private IDeviceInfoService deviceInfoService;

    @Override
    public Map<String, List<DeviceInfo>> getDevsByStationAndTypes(Set<String> stationCodes, Set<Integer> types) {
        Map<String, List<DeviceInfo>> result = new HashMap<>();
        //从缓存中获取所有的设备信息
        List<DeviceInfo> cachedDevices = DeviceCache.getAllDevs();
        
        if(CommonUtil.isNotEmpty(cachedDevices)){
          //根据电站编号、设备类型进行过滤并得到最终结果
            for(DeviceInfo device : cachedDevices){
                String stationCode = device.getStationCode();
                Integer deviceType = device.getDevTypeId();

                if((CommonUtil.isEmpty(stationCodes) || stationCodes.contains(stationCode)) &&
                        (CommonUtil.isEmpty(types) || types.contains(deviceType))){
                    List<DeviceInfo> deviceInfoList = result.get(stationCode);
                    if(deviceInfoList == null){
                        deviceInfoList = new ArrayList<>();
                    }
                    if(!device.getIsLogicDelete()){
                        deviceInfoList.add(device);
                    }

                    result.put(stationCode, deviceInfoList);
                }
            }
        }
        //如果从缓存中获取失败， 则查询数据库获取设备信息
        else{
            List<DeviceInfo> queryDevicesList = deviceInfoService.queryDevicesByStationCodes(stationCodes, types);
            if(queryDevicesList != null){
                for(DeviceInfo deviceInfo : queryDevicesList){
                    String stationCode = deviceInfo.getStationCode();
                    List<DeviceInfo> deviceInfoList = result.get(stationCode);
                    if(deviceInfoList == null){
                        deviceInfoList = new ArrayList<>();
                    }
                    
                    deviceInfoList.add(deviceInfo);
                    result.put(stationCode, deviceInfoList);
                }
            }
        }

        return result;
    }

    @Override
    public List<DeviceInfo> getDevicesFromDB(Collection<String> stationCodes, Collection<Integer> deviceTypes) {

        return deviceInfoService.queryDevicesByStationCodes(stationCodes, deviceTypes);
    }

    @Override
    public List<DeviceInfo> getDevicesFromDB(String stationCode, Integer deviceType) {

        return getDevicesFromDB(CommonUtil.createListWithElements(stationCode),
                CommonUtil.createListWithElements(deviceType));
    }

    @Override
    public List<DeviceInfo> getDevicesFromDB(List<Long> devcieIds, Set<String> stationCodes) {
        return deviceInfoService.queryDevicesBySIdDId(devcieIds, stationCodes);
    }

    @Override
    public List<DeviceInfo> getDevicesFromDB(Collection<Long> deviceIds) {
        return deviceInfoService.queryDevicesByIds(deviceIds);
    }
}
