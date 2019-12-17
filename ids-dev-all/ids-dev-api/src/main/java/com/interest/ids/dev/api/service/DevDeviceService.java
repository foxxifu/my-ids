package com.interest.ids.dev.api.service;

import java.util.List;

import com.interest.ids.common.project.bean.alarm.AlarmModel;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.signal.SignalVersionInfo;

public interface DevDeviceService {
	
	
	//根据数采SN查找下挂设备
	List<DeviceInfo> getDevicesByParentSn(String parentSn);
	
	//根据ESN查找设备
	DeviceInfo getDeviceBySn(String sn);
	
	DeviceInfo getDeviceByID(Long id);
	
	DeviceInfo getDevByParentSnAndSecondAddr(String parentSn,Integer secondAddr);
	
	void updateDevice(DeviceInfo device);
	
	List<DeviceInfo> getDevicesByLinkedHostAndType(String linkedHost,Integer type);
	
	List<DeviceInfo> getChildDevices(Long parentId);
	
	List<DeviceInfo> getDeviceByStationAndType(String stationCode,Integer devType);
	
	List<DeviceInfo> getDeviceByStationAndTypeAndBox(String stationCode,Integer devType,List<Long> boxes);
	
	List<DeviceInfo> getDevsBySpecialType(String stationCode);
	
	AlarmModel getAlarmModel(Long modelId,String stationCode);
	//获取采集器设备
	List<DeviceInfo> getDcDevices();
	/**
	 * 根据协议类型获取版本信息
	 * @param procode 主要是查询MQQT协议类型的版本
	 * @return
	 */
	List<SignalVersionInfo> getVersionByProcode(String procode);
	/**
	 * 根据mqqt的版本信息，获取这个版本下所有的mqtt设备信息
	 * @param versionCode mqtt的版本号
	 * @return
	 */
	List<DeviceInfo> getByMqttVesionDev(String versionCode);
	
	/**
	 * 批量存入设备
	 * @param devices
	 */
	void insertDeviceList(List<DeviceInfo> devices);
	/**
	 * 存入一个设备，并且返回id值
	 */
	long insertAndGetId(DeviceInfo device);
	

}
