package com.interest.ids.commoninterface.dao.largescreen;

import java.util.List;
import java.util.Map;

/**
 * 电站大屏数据查询dao层
 * 
 * @author claude
 *
 */
public interface LargeScreenDao4StationMapper {

	/**
	 * 查询电站的日负荷曲线数据
	 * 
	 * @param startDate
	 *            当天开始时间时间戳
	 * @param endDate
	 *            当天结束时间时间戳
	 * @param stationCode
	 *            电站编号
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return List<Map{"collectTime/avtivePower",对应值}>
	 */
	List<Map<String, Object>> getActivePower(Map<String, Object> params);

	/**
	 * 查询电站的当前负荷
	 * 
	 * @param startDate
	 *            当天开始时间时间戳
	 * @param endDate
	 *            当天结束时间时间戳
	 * @param stationCode
	 *            电站编号
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return List<Map{"collectTime/avtivePower",对应值}>
	 */
	Double getCurrentActivePower(Map<String, Object> params);

	/**
	 * 查询电站的发电量趋势
	 * 
	 * @param startDate
	 *            当天开始时间时间戳
	 * @param endDate
	 *            当天结束时间时间戳
	 * @param stationCode
	 *            电站编号
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return List<Map{"collectTime/avtivePower",对应值}>
	 */
	List<Map<String, Object>> getPowerTrends(Map<String, Object> params);

	/**
	 * 查询电站的当天数据
	 * 
	 * @param startDate
	 *            当天开始时间时间戳
	 * @param endDate
	 *            当天结束时间时间戳
	 * @param stationCode
	 *            电站编号
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return List<Map{"collectTime/avtivePower",对应值}>
	 */
	Map<String, Object> getCurrentDayCap(Map<String, Object> params);

	/**
	 * 查询电站总发电量
	 * 
	 * @param stationCode
	 *            电站编号
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return List<Map{"collectTime/avtivePower",对应值}>
	 */
	Map<String, Object> getCommonData(Map<String, Object> params);

	/**
	 * 查询电站电站分布
	 * 
	 * @param stationCode
	 *            电站编号
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return List<Map{"collectTime/avtivePower",对应值}>
	 */
	List<Map<String, Object>> getListStationInfo(Map<String, Object> params);

	/**
	 * 查询电站年社会贡献
	 * 
	 * @param year
	 *            当年时间时间戳
	 * @param stationCode
	 *            电站编号
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return List<Map{"collectTime/avtivePower",对应值}>
	 */
	Map<String, Object> getYearSocialContribution(Map<String, Object> params);

	/**
	 * 查询电站累计社会贡献
	 * 
	 * @param stationCode
	 *            电站编号
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return List<Map{"collectTime/avtivePower",对应值}>
	 */
	Map<String, Object> getTotalSocialContribution(Map<String, Object> params);

	/**
	 * 查询电站设备分布
	 * 
	 * @param stationCode
	 *            电站编号
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return List<Map{"collectTime/avtivePower",对应值}>
	 */
	List<Map<String, Object>> getDeviceCount(Map<String, Object> params);

	/**
	 * 根据电站编号查询电站告警信息
	 * 
	 * @param stationCode
	 * @return
	 */
	List<Map<String, Object>> getAlarmStatistics(String stationCode);

	/**安全运行天数
	 * 
	 * 
	 * @return
	 */
	String getSafeRunDays(String stationCode);

	/**
	 * 大屏数据
	 * 
	 * @param params
	 * @return
	 */
	Map<String, Object> getPowerGeneration(Map<String, Object> params);

	/**
	 * 电站任务工单统计
	 * 
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> getTaskStatistics(Map<String, Object> params);

}
