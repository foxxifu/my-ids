package com.interest.ids.commoninterface.dao.station;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.interest.ids.common.project.bean.device.DeviceInfoDto;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.sm.StationShareemi;

public interface StationShareemiMapper {
	// 根据电站编号查询所有共享设备的id - 估计有问题
	List<Map<String, Long>> getSharedDeviceByStationCodes(
			List<String> stationCodes);

	// 根据电站编号查询电站下面的所有的环境检测仪
	List<DeviceInfo> getDeviceInfoByStationCode(String stationCode);

	// 插入电站共享环境检测仪数据
	int insertDeviceShare(StationShareemi share);

	/**
	 * 根据电站编号查询电站下面的所有的环境检测仪
	 * 
	 * @param stationCode
	 * @return
	 */
	public List<DeviceInfoDto> getEmiInfoByStationCode(String stationCode);

	/* add by zhulong */
	/**
	 * 获取所有的环境监测仪信息
	 * 
	 * @return
	 */
	List<StationShareemi> selectAllSharedEmis();

	/**
	 * 根据电站编号查询环境监测仪共享信息
	 * 
	 * @param stationCodes
	 * @return
	 */
	List<StationShareemi> selectStationSharedEMI(
			@Param("stationCodes") Collection<String> stationCodes);

	/**
	 * 查询该电站编号下面是否已经绑定环境检测仪
	 * 
	 * @param stationCode
	 *            电站编号
	 * @return 绑定环境检测仪数
	 */
	int getShareRmiSize(String stationCode);

	/**
	 * 通过电站编号更新共享环境监测仪信息
	 * 
	 * @param share
	 *            共享环境检测仪实体类
	 */
	void updateDeviceShare(StationShareemi share);

	/**
	 * 删除共享环境检测仪
	 * 
	 * @param devIdList
	 */
	void deleteShareEmi(@Param("devIdList") List<Long> devIdList);

	/**
	 * 根据电站编号删除绑定的共享环境检测仪
	 * 
	 * @param stationCode
	 */
	void deleteShareEmiBystationCode(String stationCode);
}
