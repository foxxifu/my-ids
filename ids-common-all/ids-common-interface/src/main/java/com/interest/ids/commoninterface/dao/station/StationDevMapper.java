package com.interest.ids.commoninterface.dao.station;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.interest.ids.common.project.bean.device.DeviceModuleQuery;
import com.interest.ids.common.project.bean.device.DevicePvCapacity;
import com.interest.ids.common.project.bean.device.StationDevicePvModule;
import com.interest.ids.common.project.bean.device.StationPvModule;
import com.interest.ids.common.project.bean.signal.DeviceInfo;

public interface StationDevMapper {
	// 查询所有设备的版本号 (根据企业号去查询)
	List<String> selectDevModelVersion(Long enterpriseId);

	// 设备接入保持
	int insetStationDev(DeviceInfo dev);

	// 查看电站下关联的设备
	List<DeviceInfo> selectStationDevsByStationCode(String stationCode);

	// 组串接入厂家信息
	List<StationPvModule> selectStationPvModel();

	// 根据厂家的id查询某一个厂家的详细信息
	StationPvModule selectStationPvModuleDetail(Long id);

	// 给设备添加组串
	int insertStationPvModule(StationDevicePvModule module);

	// 给设备添加容量
	int insertStationPvCapacity(List<StationDevicePvModule> list);

	// 根据id查询设备信息-非全部信息
	DeviceInfo selectStationDevsByStationId(Long deviceId);

	// 添加设备容量到容量表中
	void insertDeviceCapacity(DevicePvCapacity capacity);

	// 查询设备下的组串详情
	List<Map<String, String>> selectModulesByEsn(
			DeviceModuleQuery deviceModuleQuery);

	// 查询设备关联的组件
	List<StationDevicePvModule> selectStationDevicePvModules(String esn);

	// 更新设备组件关联关系
	Integer updateDeviceModule(List<StationDevicePvModule> list);

	// 同步数据到容量表
	Integer updateDeviceModuleCapacity(DevicePvCapacity capacity);
    // 删除ids_pv_capacity_t中的数据
    void deleteStationPvCapacity(Long id);

    //根据设备id查询组串信息
    public List<StationDevicePvModule> selectPvByDevId(Long devid);
    
    public List<StationDevicePvModule> selectPvByStationCodeAndDevType(@Param(value = "stationCode")String stationCode,
            @Param(value = "devType")Integer devType);

	// 删除原来的组串容量详情配置
	void deleteStationPvModule(Long id);

}
