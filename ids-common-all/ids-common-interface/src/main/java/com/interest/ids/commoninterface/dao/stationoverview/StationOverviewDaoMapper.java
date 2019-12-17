package com.interest.ids.commoninterface.dao.stationoverview;

import java.util.List;
import java.util.Map;

/**
 * 电站总览mapper
 * 
 * @author claude
 *
 */
public interface StationOverviewDaoMapper {

	/**
	 * 查询省级行政区域的电站分布
	 * 
	 * @param userInfo
	 *            userInfo（map）<br>
	 *            id:userId;type_:system/enterprise/"";
	 * @return list<map{'province',对应值；'capacity':对应值}>
	 */
	List<Map<String, Object>> listProvince(Map<String, Object> userInfo);

	/**
	 * 查询市级行政区域的电站分布
	 * 
	 * @param userInfo
	 *            userInfo（map）<br>
	 *            id:userId;type_:system/enterprise/"";
	 * @return list<map{'city',对应值；'capacity':对应值}>
	 */
	List<Map<String, Object>> listCity(Map<String, Object> userInfo);

	/**
	 * 查询县/区级行政区域的电站分布
	 * 
	 * @param userInfo
	 *            userInfo（map）<br>
	 *            id:userId;type_:system/enterprise/"";
	 * @return list<map{'county',对应值；'capacity':对应值}>
	 */
	List<Map<String, Object>> listCounty(Map<String, Object> userInfo);

	/**
	 * 查询电站总览中的发电量及收益
	 * 
	 * @param userInfo
	 *            userInfo（map）<br>
	 *            id:userId;type_:system/enterprise/"";startTime:开始时间;endTime:
	 *            结束时间;timeDimension:时间维度(year/month)
	 * @return 
	 *         list<map{'collectTime',采集时间;'producePower':发电量值};'powerProfit':收益值
	 *         >
	 */
	List<Map<String, Object>> getPowerAndIncome(Map<String, Object> userInfo);

	/**
	 * 查询当前用户所管理电站的告警分类统计
	 * 
	 * @param userInfo
	 *            userInfo（map）<br>
	 *            id:userId;type_:system/enterprise/"";
	 * @return list<map{"levelId",告警级别;"alarmCount",告警数量}>
	 */
	List<Map<String, Object>> getAlarmStatistics(Map<String, Object> userInfo);

	/**
	 * 查询当前用户所管理电站PR降序排序top5
	 * 
	 * @param userInfo
	 * 			id、type_、startTime、endTime
	 * @return list<map{"stationCode/stationName/pr":对应值}>
	 */
	List<Map<String, Object>> getPRList(Map<String, Object> userInfo);

	/**
	 * 查询当前用户所管理电站PPR降序排序top5
	 * 
	 * @param userInfo
	 * 			id、type_、startTime、endTime
	 * @return list<map{"stationCode/stationName/ppr":对应值}>
	 */
	List<Map<String, Object>> getPPRList(Map<String, Object> userInfo);

	/**
	 * 查询电站总览实时数据
	 * 
	 * @param userInfo
	 * @return
	 */
	Map<String, Object> getRealtimeKPI(Map<String, Object> userInfo);

	/**
	 * 获取设备分布信息
	 * 
	 * @param userInfo
	 * @return
	 */
	List<Map<String, Object>> getDevDistrition(Map<String, Object> userInfo);

	/**
	 * 当年社会贡献
	 * 
	 * @param userInfo
	 * @return
	 */
	Map<String, Object> getYearSocialContribution(Map<String, Object> userInfo);

	/**
	 * 累计社会贡献
	 * 
	 * @param userInfo
	 * @return
	 */
	Map<String, Object> getTotalSocialContribution(Map<String, Object> userInfo);

}
