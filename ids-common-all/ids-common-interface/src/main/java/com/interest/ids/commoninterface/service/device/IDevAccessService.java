package com.interest.ids.commoninterface.service.device;

import java.util.List;
import java.util.Map;

import com.interest.ids.common.project.bean.signal.DeviceInfo;

public interface IDevAccessService {

	/**
	 * 设备接入保持
	 * 
	 * @param devInfo
	 */
	void insertDevInfo(DeviceInfo devInfo);

	/**
	 * 查询对应协议的点表数据
	 * 
	 * @param protocolType
	 * 			协议类型
	 * @return
	 */
	List<Map<String, Object>> getSignalVersionList(String protocolType);

	/**
	 * 根据接入协议和设备类型查询设备列表
	 * 
	 * @param protocolType
	 * 			接入协议
	 * @param devTypeId
	 * 			设备类型id
	 * @return
	 */
	List<Map<String, Object>> getDevList(String protocolType, Integer devTypeId, String stationCode);

	/**
	 * 通过sn编号查询该设备条数
	 * 
	 * @param snCode
	 * @return
	 */
	int getCountBySnCode(String snCode);

	/**
	 * 
	 * @param snCode
	 */
	List<DeviceInfo> getDevInfoBySnCode(String snCode);

	/**
	 * 判断mqqt设备的父设备sn和二级地址是否存在
	 * @param parentSn
	 * @param secendAddress
	 * @return
	 */
	boolean valdateParentSnAndSecendAddress(String parentSn, Integer secendAddress);

	/**
	 * 新增mqqt设备的信息
	 * @param dev
	 * @return
	 */
	void insertMqqtDev(DeviceInfo dev, Map<String, String> map);
	/**
	 * 新增modbus或者采集棒下挂设备
	 * @param dev
	 * @return
	 */
	void insertModbusDev(DeviceInfo dev);
	
		/**
	 * 获取当前电站已经存在的parentSnCode
	 * @param stationCode
	 * @return
	 */
	List<String> getMqqtParentSnList(String stationCode);
	/**
	 * 查询modbus协议的数采设备的sn号的集合
	 * @param stationCode 电站编号
	 * @return
	 */
	List<String> getModbusParentSnList(String stationCode);

}
