package com.interest.ids.commoninterface.service.stationoverview;

import java.util.List;
import java.util.Map;

/**
 * 单电站总览接口
 * 
 * @author claude
 *
 */
public interface ISingleStationOverviewService {

	/**
	 * 查询当前用户管理电站的发电量和收益
	 * 
	 * @param stationCode
	 *            电站编号
	 * @param queryType
	 *            查询类型：month、year、allYear
	 * @return 
	 *         list<map{'collectTime',采集时间;'producePower':发电量值;'powerProfit':收益值}
	 *         >
	 */
	List<Map<String, Object>> getSingleStationPowerAndIncome(
			String stationCode, String queryType, String queryTime);

	/**
	 * 查询各类告警情况
	 * 
	 * @param stationCode
	 *            电站编号
	 * @return
	 */
	List<Map<String, Object>> getSingleStationAlarmStatistics(String stationCode);

	/**
	 * 电站总览中查询实时数据
	 * 
	 * @param stationCode
	 *            电站编号
	 * @return map{"activePower/dayCap/dayIncome/totalCap/yearCap",对应值}
	 */
	Map<String, Object> getSingleStationRealtimeKPI(String stationCode);

	/**
	 * 根据电站编号查询单电站总览中的电站详情
	 * 
	 * @param stationCode
	 *            电站编号
	 * @return map{
	 *         "stationName/stationAddr/stationStatus/capacity/onlineTime/runDays/contactPeople/dayCap/monthCap/yearCap"
	 *         , 对应值 }
	 */
	Map<String, Object> getSingleStationInfo(String stationCode);

	/**
	 * 单电站查询日负荷曲线
	 * 
	 * @param stationCode
	 *            电站编号
	 * @return List<Map<String, Object>>
	 */
	List<Map<String, Object>> getSingleStationActivePower(String stationCode);

	/**
	 * 获取设备发电效率
	 * 
	 * @param stationCode
	 * 		电站id
	 * @return Map<String, Object>
	 */
	Map<String, Object> getDevPowerStatus(String stationCode);

	/**
	 * 电站总览社会贡献
	 * 
	 * @param stationCode
	 * @return
	 */
	Map<String, Object> getContribution(String stationCode);

}
