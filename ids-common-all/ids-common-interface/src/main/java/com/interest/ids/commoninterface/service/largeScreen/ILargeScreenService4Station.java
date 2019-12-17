package com.interest.ids.commoninterface.service.largeScreen;

import java.util.List;
import java.util.Map;

/**
 * 企业大屏业务逻辑处理类
 * 
 * @author claude
 *
 */
public interface ILargeScreenService4Station {

	/**
	 * 当前用户所在企业的社会贡献数据
	 * 
	 * @param params
	 *            用户id
	 * @return map{"year/accumulativeTotal",对应社会贡献指标}
	 */
	Map<String, Object> getSocialContribution(Map<String, Object> params);

	/**
	 * 根据电站编号查询日负荷曲线
	 * 
	 * @param params
	 *            电站编号
	 * @return
	 */
	Map<String, Object> getActivePower(Map<String, Object> params);

	/**
	 * 根据电站编号查询当前负荷
	 * 
	 * @param params
	 *            电站编号
	 * @return
	 */
	Double getCurrentActivePower(Map<String, Object> params);

	/**
	 * 根据电站编号查询发电量趋势
	 * 
	 * @param params
	 *            电站编号
	 * @return
	 */
	Map<String, Object> getPowerTrends(Map<String, Object> params);

	/**
	 * 根据电站编号查询当前数据
	 * 
	 * @param params
	 *            电站编号
	 * @return
	 */
	Map<String, Object> getCommonData(Map<String, Object> params);

	/**
	 * 根据电站编号查询电站分布
	 * 
	 * @param params
	 *            电站编号
	 * @return
	 */
	Map<String, Object> getListStationInfo(Map<String, Object> params);

	/**
	 * 根据电站编号查询设备分布
	 * 
	 * @param params
	 *            电站编号
	 * @return
	 */
	Map<String, Object> getDeviceCount(Map<String, Object> params);

	/**
	 * 根据电站编号查询电站告警信息
	 * 
	 * @param stationCode
	 * @return
	 */
	List<Map<String, Object>> getAlarmStatistics(String stationCode);

	/**
	 * 查询电站发电数据
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
	Map<String, Object> getTaskStatistics(Map<String, Object> params);

}
