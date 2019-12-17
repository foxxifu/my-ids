package com.interest.ids.commoninterface.dao.stationoverview;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 单电站总览mapper
 * 
 * @author claude
 *
 */
public interface SingleStationOverviewDaoMapper {

	/**
	 * 查询电站总览中的发电量及收益
	 * 
	 * @param params
	 *            开始时间:startTime,结束时间:endTime,电站编号:stationCode,查询类型:queryType
	 * @return 
	 *         list<map{'collectTime',采集时间;'producePower':发电量值;'powerProfit':收益值}
	 *         >
	 */
	List<Map<String, Object>> getSingleStationPowerAndIncome(
			Map<String, Object> params);

	/**
	 * 查询当前用户所管理电站的告警分类统计
	 * 
	 * @param stationCode
	 *            电站编号
	 * @return list<map{"levelId",告警级别;"alarmCount",告警数量}>
	 */
	List<Map<String, Object>> getSingleStationAlarmStatistics(String stationCode);

	/**
	 * 查询电站总览实时数据
	 * 
	 * @param params
	 *            currentDay:当日时间戳;currentYear：当年时间戳;stationCode：电站编号;
	 * @return map{"activePower/dayCap/dayIncome/totalCap/yearCap",对应值}
	 */
	Map<String, Object> getSingleStationRealtimeKPI(Map<String, Object> params);

	/**
	 * 根据电站编号查询单电站总览中的电站详情
	 * 
	 * @param params
	 *            currentDay：当天时间戳、currentMonth：当月时间戳、currentYear：当年时间戳、
	 *            stationCode：电站编号
	 * @return map{
	 *         "stationName/stationAddr/capacity/onlineTime/safeRunTime/contactPeople/dayCap/monthCap/yearCap"
	 *         , 对应值 }
	 */
	Map<String, Object> getSingleStationInfo(Map<String, Object> params);

	/**
	 * 查询当天日负荷曲线
	 * 
	 * @param params
	 *            当天开始时间和结束时间,电站编号
	 * @return List<Map<String, Object>>
	 */
	List<Map<String, Object>> getSingleStationActivePower(Map<String, Object> params);

	/**
	 * 电站总览社会贡献
	 * 
	 * @param stationCode
	 * @return
	 */
	Map<String, Object> getContribution(@Param("stationCode")String stationCode, @Param("year")long year, 
			@Param("month")long month);
}
