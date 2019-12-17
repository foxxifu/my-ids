package com.interest.ids.commoninterface.dao.device;

import com.interest.ids.common.project.bean.device.DevicePvCapacity;
import com.interest.ids.common.project.bean.device.PvCapacityM;

import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DevPvCapacityMMapper {

    List<Map<String, Double>> sumStationPVCapByList(@Param("stationCodes") List<String> stationCodes,
                                                    @Param("deviceTypeIds") List<Integer> deviceTypeIds);

    List<PvCapacityM> selectPvCapByType(@Param("stationCode") String stationCode, @Param("devTypeId") Integer devTypeId);

    List<PvCapacityM> selectPvCapBySIdsAndType(@Param("stationCodes") List<String> stationCodes,
                                               @Param("deviceTypeId") Integer deviceTypeId);
    
    /**
     * 查询单个设备的组串容量和
     * @param deviceId
     * @return
     */
    Double selectTotalPvCapacityByDeviceId(@Param("deviceId")Long deviceId);
    
    /**
     * 查询单个电站的组串容量和
     * @param stationCode
     * @return
     */
    Double selectTotalPvCapacityByStationCode(@Param("stationCode")String stationCode);
    
    /**
     * 根据设备id查询组串容量
     */
    List<PvCapacityM> selectPvCapacityMByDeviceId(Long deviceId);

    /**
     * 更新组串容量
     * @param insertCapacity
     * @return
     */
    int updateCapacity(List<DevicePvCapacity> insertCapacity);

    /**
     * 批量插入组串容量
     * @param insertCapacity
     * @return
     */
    int insertinsertCapacity(DevicePvCapacity devicePvCapacity);

    /**
     * 更新组串容量
     * @param cap
     * @return
     */
    int updateByPrimaryKeySelective(PvCapacityM cap);
    
    /**
     * 统计系统已接入的容量
     * @return
     */
    Double countAllInstalledCapacity();
    
    List<PvCapacityM> selectPvCapByDevIds(List<Long> devIds);
    /**
     * 监控传递过来的是否需要删除的操作
     * @param params
     * @return
     */
	int getMonitorPvCapNum(Map<String, Object> params);
	/**
	 * 根据设备id和电站编号删除对应的pv配置,删除ids_pv_capacity_t
	 * @param params
	 */
	void deleteMonitorCapPvConfig(Map<String, Object> params);
	/**
	 * 根据设备id和电站编号删除对应的pv配置,删除 ids_device_pv_module_t
	 * @param params
	 */
	void deleteMonitorDevPvConfig(Map<String, Object> params);
	/**
	 * 获取监控配置的和上面配置的数量个数
	 * @param map
	 * @return
	 */
	int countCenterPvConfig(HashMap<String, Object> map);
	/**
	 * 根据设备id删除对应的pv配置,删除ids_center_vert_detail_t
	 * @param map
	 */
	void deleteMonitorCenterPvConfig(HashMap<String, Object> map);
} 
