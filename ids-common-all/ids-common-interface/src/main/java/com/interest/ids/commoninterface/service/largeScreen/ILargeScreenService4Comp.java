package com.interest.ids.commoninterface.service.largeScreen;

import java.util.Map;

/**
 * 企业大屏业务逻辑处理类
 * 
 * @author claude
 *
 */
public interface ILargeScreenService4Comp {

	/**
	 * 根据用户id查询该用户所在企业所有电站的日负荷曲线数据
	 * 
	 * @param id
	 *            用户id
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return map{dayActivePowerList,list<map{时间戳，对应时刻点的值}>}
	 */
    Map<String, Object> getActivePower(Map<String, Object> params);

	/**
	 * 根据用户id查询该用户所在企业所有电站的当前最新实时负荷
	 * 
	 * @param userId
	 *            用户id
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return Double：今日负荷
	 */
    Double getCurrentActivePower(Map<String, Object> params);

	/**
	 * 根据用户查询企业的简介
	 * 
	 * @param userId
	 *            用户id
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return String:简介
	 */
    String getCompdisc(Map<String, Object> params);

	/**
	 * 查询当前系统的安全运行天数
	 * 
	 * @param userId
	 *            用户id
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return
	 */
    Map<String, Object> getCommonData(Map<String, Object> params);

	/**
	 * 查询当前用户所在企业的所有电站分布情况
	 * 
	 * @param userId
	 *            用户ID
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return map{"0to10/10to100/100to500/500up",对应值}
	 */
    Map<String, Object> getListStationInfo(Map<String, Object> params);

	/**
	 * 当前用户所在企业的社会贡献数据
	 * 
	 * @param userId
	 *            用户id
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return map{"year/accumulativeTotal",对应社会贡献指标}
	 */
    Map<String, Object> getSocialContribution(Map<String, Object> params);

	/**
	 * 查询当前用户所在企业当月每天的发电量趋势
	 * 
	 * @param userId
	 *            用户id
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return map{key:"dayEarningsList",value:
	 *         0到30对应1号到31号的收益，根据当月天数返回不同的数据长度;key
	 *         :"dayPowerList",value:dayPowerList}
	 */
    Map<String, Object> getPowerTrends(Map<String, Object> params);

	/**
	 * 根据人员查询当前人员所在的企业及企业下属区域和电站
	 * 
	 * @param userId
	 *            用户id
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return map{"areaTree",地图左上角树形结构；"currentShowData",当前地图显示的区域和电站}
	 */
    Map<String, Object> getMapShowData(Map<String, Object> params);

	/**
	 * 根据用户id查询用户所在企业的设备数量
	 * 
	 * @param userId
	 *            用户id
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return 
	 *         map{"combinerBoxCount":汇流箱数量;"deviceTotalCount":设备总数;"inverterConcCount"
	 *         :集中式逆变器数量;"inverterStringCount":组串式逆变器数量;"pvCount":组件数量}
	 */
    Map<String, Object> getDeviceCount(Map<String, Object> params);

    /**
     * 大屏发电指标信息
     * 
     * @param params
     * @return
     */
	Map<String, Object> getPowerGeneration(Map<String, Object> params);

}
