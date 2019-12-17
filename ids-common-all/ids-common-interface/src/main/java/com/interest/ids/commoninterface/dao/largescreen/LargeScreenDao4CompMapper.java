package com.interest.ids.commoninterface.dao.largescreen;

import java.util.List;
import java.util.Map;

/**
 * 企业大屏数据查询dao层
 * 
 * @author claude
 *
 */
public interface LargeScreenDao4CompMapper {

	/**
	 * 查询用户所在企业的日负荷曲线数据
	 * 
	 * @param startDate
	 *            当天开始时间时间戳
	 * @param endDate
	 *            当天结束时间时间戳
	 * @param userId
	 *            用户id
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return List<Map{"collectTime/avtivePower",对应值}>
	 */
    List<Map<String, Object>> getActivePower(Map<String, Object> params);

	/**
	 * 查询用户所在企业的当前负荷
	 * 
	 * @param userId
	 *            用户id
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return List<Map<"currentActivePower", 对应值>>
	 */
    Double getCurrentActivePower(Map<String, Object> params);

	/**
	 * 根据用户查询所在企业的简介
	 * 
	 * @param userId
	 *            用户id
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return 简介
	 */
    String getCompdisc(Map<String, Object> params);

	/**
	 * 查询安全运行天数，如果未配置安装运行初始化时间，则返回0
	 * 
	 * @return 安全运行开始时间
	 */
    String getSafeRunDays(Map<String, Object> map);

	/**
	 * 查询员工所在集团的累计发电量，直接从电站实时表中获取
	 * 
	 * @param userId
	 *            用户id
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return Double：累计发电量
	 */
    Map<String, Object> getCommonData(Map<String, Object> params);

	/**
	 * 查询当前用户所在企业的所有电站分布情况
	 * 
	 * @param userId
	 *            用户ID
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return list<map{"0to10/10to100/100to500/500up",对应值}>
	 */
    List<Map<String, Object>> getListStationInfo(
            Map<String, Object> params);

	/**
	 * 获取当前用户所在企业当年发电量
	 * 
	 * @param userId
	 *            用户id
	 * @param year
	 *            当前年份
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return 当年发电量
	 */
    Map<String, Object> getYearSocialContribution(
            Map<String, Object> params);

	/**
	 * 获取当前用户所在企业当年发电量
	 * 
	 * @param userId
	 *            用户id
	 * @param year
	 *            当前年份
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return 当年发电量
	 */
    Map<String, Object> getTotalSocialContribution(
            Map<String, Object> params);

	/**
	 * 查询当前人员所在企业当月每天的日发电量和收益
	 * 
	 * @param userId
	 *            用户id
	 * @param firstDayOfMonth
	 *            当月第一天的时间毫秒
	 * @param lastDayOfMonth
	 *            当月最后一天的时间毫秒
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return list<map{"collectTime",时间;"productPower",日发电量;"powerProfit",收益}>
	 */
    List<Map<String, Object>> getPowerTrends(Map<String, Object> params);

	/**
	 * 根据人员查询当前人员所在的企业
	 * 
	 * @param userId
	 *            用户id
	 * @return map("id/name/pid","企业id/企业名称/企业父id")
	 */
    List<Map<String, Object>> getEnterpriseByUserId(
            Map<String, Object> params);

	/**
	 * 根据人员查询当前企业下直属电站和区域
	 * 
	 * @param userId
	 *            用户id
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return List<map>
	 */
    List<Map<String, Object>> getEnterPChildByUserId(
            Map<String, Object> params);

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
    List<Map<String, Object>> getDeviceCount(Map<String, Object> params);

	/**
	 * 查询当前人员所在企业的当天实时发电量
	 * 
	 * @param userId
	 *            用户id
	 * @param currentDay
	 *            当天最早时间
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return 当天实时发电量和收益
	 */
    Map<String, Object> getCurrentDayCap(Map<String, Object> params);

    /**
     * 大屏发现指标
     * 
     * @param params
     * @return
     */
	Map<String, Object> getPowerGeneration(Map<String, Object> params);

	/**
	 * 查询所有企业
	 * 
	 * @return
	 */
	List<Map<String, Object>> getAllEnterprise();

}
