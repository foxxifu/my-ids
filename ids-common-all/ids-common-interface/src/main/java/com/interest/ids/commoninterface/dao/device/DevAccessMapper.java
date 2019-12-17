package com.interest.ids.commoninterface.dao.device;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.utils.CommonMapper;

public interface DevAccessMapper extends CommonMapper<DeviceInfo>{

	/**
	 * 设备接入保持
	 * 
	 * @param dev
	 */
	void insertDevInfo(DeviceInfo dev);

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
	List<Map<String, Object>> getDevList(@Param("protocolType") String protocolType, 
			@Param("devTypeId") Integer devTypeId,@Param("stationCode") String stationCode);

}
