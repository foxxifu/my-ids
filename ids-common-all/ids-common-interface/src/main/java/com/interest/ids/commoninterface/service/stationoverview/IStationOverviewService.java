package com.interest.ids.commoninterface.service.stationoverview;

import java.util.List;
import java.util.Map;

import com.interest.ids.common.project.bean.sm.StationInfoM;

/**
 * 电站总览接口
 * 
 * @author claude
 *
 */
public interface IStationOverviewService {

	/**
	 * 查询当前用户管理电站的行政区域分布<br>
	 * 规则： <br>
	 * 1、如果当前用户下管理的电站所属行政区域省级别大于五个，则电站分布显示到省级别
	 * 2、如果当前用户下管理的电站所属行政区域市级别大于五个，则电站分布显示到市级别
	 * 3、如果当前用户下管理的电站所属行政区域县/区级别大于五个，则电站分布显示到县/区级别
	 * 
	 * @param userInfo
	 *            userInfo（map）<br>
	 *            id:userId;type_:system/enterprise/"";
	 * @return map{"stationCount/staitonDist" ：电站总数/电站分布情况list{map}}
	 */
	Map<String, Object> getStationDistribution(Map<String, Object> userInfo);

	/**
	 * 查询当前用户管理电站的发电量和收益
	 * 
	 * @param userInfo
	 *            userInfo（map）<br>
	 *            id:userId;type_:system/enterprise/"";startTime:开始时间;endTime:
	 *            结束时间;timeDimension:时间维度(year/month)
	 * @return map{"producePowerList" :
	 *         map{时间:对应发电量};"powerProfitList":map{时间:对应收益};}
	 */
	List<Map<String, Object>> getPowerAndIncome(Map<String, Object> userInfo);

	/**
	 * 查询各类告警情况
	 * 
	 * @param userInfo
	 *            userInfo（map）<br>
	 *            id:userId;type_:system/enterprise/"";
	 * @return
	 */
	List<Map<String, Object>> getAlarmStatistics(Map<String, Object> userInfo);

	/**
	 * 查询电站总览PR
	 * 
	 * @param userInfo
	 *            userInfo（map）<br>
	 *            id:userId;type_:system/enterprise/"";
	 * @return
	 */
	List<Map<String, Object>> getPRList(Map<String, Object> userInfo);

	/**
	 * 查询电站总览PPR
	 * 
	 * @param userInfo
	 *            userInfo（map）<br>
	 *            id:userId;type_:system/enterprise/"";
	 * @return
	 */
	List<Map<String, Object>> getPPRList(Map<String, Object> userInfo);

	/**
	 * 电站总览中查询电站状态
	 * 
	 * @param userInfo
	 *            id、type_
	 * @return map{"abnormal/fault/interruption/online",对应值}
	 */
	Map<String, Object> getStationStatus(Map<String, Object> userInfo);

	/**
	 * 电站总览中查询实时数据
	 * 
	 * @param userInfo
	 *            id：userId;type_：system/enterprise
	 * @return map{"activePower/monthCap/monthIncome/totalCap/yearCap",对应值}
	 */
	Map<String, Object> getRealtimeKPI(Map<String, Object> userInfo);

	/**
	 * 获取人员管理电站的列表
	 * 
	 * @param userInfo
	 *            用户相关信息
	 * @return List<StationInfoM>
	 */
	List<StationInfoM> getPowerStationList(Map<String, Object> paramMap);

	/**
	 * 获取设备分布信息
	 * 
	 * @param userInfo
	 * @return
	 */
	Map<String, Object> getDevDistrition(Map<String, Object> userInfo);

	/**
	 * 获取电站总览社会贡献
	 * 
	 * @param userInfo
	 * @return
	 */
	Map<String, Object> getContribution(Map<String, Object> userInfo);

	/**
	 * 获取app的电站列表
	 * 
	 * @param paramMap
	 *            查询参数
	 * @return List<StationInfoM>
	 */
	Map<String, Object> getAppPowerStationList(Map<String, Object> paramMap);

}
