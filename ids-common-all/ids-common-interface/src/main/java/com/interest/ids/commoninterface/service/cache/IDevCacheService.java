package com.interest.ids.commoninterface.service.cache;

import com.interest.ids.common.project.bean.signal.DeviceInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IDevCacheService {

    /**
     * 根据电站编号集及设备类型查询设备信息
     * @param stationCodes
     * @param types
     * @return
     */
    Map<String, List<DeviceInfo>> getDevsByStationAndTypes(Set<String> stationCodes, Set<Integer> types);

    List<DeviceInfo> getDevicesFromDB(Collection<String> stationCodes, Collection<Integer> deviceTypes);

    List<DeviceInfo> getDevicesFromDB(String stationCode, Integer deviceType);

    List<DeviceInfo> getDevicesFromDB(List<Long> devcieIds, Set<String> stationCodes);

    /**
     * 根据设备ID查询设备信息
     * @param deviceIds
     * @return
     */
    List<DeviceInfo> getDevicesFromDB(Collection<Long> deviceIds);
}
