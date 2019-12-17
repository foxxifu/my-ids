package com.interest.ids.commoninterface.dao.largescreen;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 区域大屏数据查询dao层
 * 
 * @author claude
 *
 */
public interface LargeScreenDao4DomainMapper {

	/**
	 * 根据区域id查询当前区域信息
	 * 
	 * @param domainId
	 *            区域id
	 * @return
	 */
	LinkedList<Map<String, Object>> getDomainInfo(Long domainId);

	/**
	 * 根据区域id查询当前区域下直属电站和区域
	 * 
	 * @param domainId
	 *            区域id
	 * @param userId
	 *            用户id
	 * @return
	 */
	List<Map<String, Object>> getDomainChildByDomainId(
			Map<String, Object> userInfo);

	/**
	 * 查询区域的日负荷曲线数据
	 * 
	 * @param startDate
	 *            当天开始时间时间戳
	 * @param endDate
	 *            当天结束时间时间戳
	 * @param domainId
	 *            电站编号
	 * @param id
	 *            当前登录用户id
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return List<Map{"collectTime/avtivePower",对应值}>
	 */
	List<Map<String, Object>> getActivePower(Map<String, Object> params);

	/**
	 * 查询区域的当前负荷
	 * 
	 * @param startDate
	 *            当天开始时间时间戳
	 * @param endDate
	 *            当天结束时间时间戳
	 * @param domainId
	 *            电站编号
	 * @param id
	 *            当前登录用户id
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return List<Map{"collectTime/avtivePower",对应值}>
	 */
	Double getCurrentActivePower(Map<String, Object> params);

	/**
	 * 查询区域的发电量趋势
	 * 
	 * @param startDate
	 *            当天开始时间时间戳
	 * @param endDate
	 *            当天结束时间时间戳
	 * @param domainId
	 *            电站编号
	 * @param id
	 *            当前登录用户id
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return List<Map{"collectTime/avtivePower",对应值}>
	 */
	List<Map<String, Object>> getPowerTrends(Map<String, Object> params);

	/**
	 * 查询区域的当天数据
	 * 
	 * @param startDate
	 *            当天开始时间时间戳
	 * @param endDate
	 *            当天结束时间时间戳
	 * @param domainId
	 *            电站编号
	 * @param id
	 *            当前登录用户id
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return List<Map{"collectTime/avtivePower",对应值}>
	 */
	Map<String, Object> getCurrentDayCap(Map<String, Object> params);

	/**
	 * 查询区域总发电量
	 * 
	 * @param domainId
	 *            电站编号
	 * @param id
	 *            当前登录用户id
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return List<Map{"collectTime/avtivePower",对应值}>
	 */
	Map<String, Object> getCommonData(Map<String, Object> params);

	/**
	 * 查询区域电站分布
	 * 
	 * @param domainId
	 *            电站编号
	 * @param id
	 *            当前登录用户id
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return List<Map{"collectTime/avtivePower",对应值}>
	 */
	List<Map<String, Object>> getListStationInfo(Map<String, Object> params);

	/**
	 * 查询区域年社会贡献
	 * 
	 * @param year
	 *            当年时间时间戳
	 * @param domainId
	 *            电站编号
	 * @param id
	 *            当前登录用户id
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return List<Map{"collectTime/avtivePower",对应值}>
	 */
	Map<String, Object> getYearSocialContribution(Map<String, Object> params);

	/**
	 * 查询区域累计社会贡献
	 * 
	 * @param domainId
	 *            电站编号
	 * @param id
	 *            当前登录用户id
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return List<Map{"collectTime/avtivePower",对应值}>
	 */
	Map<String, Object> getTotalSocialContribution(Map<String, Object> params);

	/**
	 * 查询区域设备分布
	 * 
	 * @param domainId
	 *            电站编号
	 * @param id
	 *            当前登录用户id
	 * @param type_
	 *            用户类型：system/enterprise
	 * @return List<Map{"collectTime/avtivePower",对应值}>
	 */
	List<Map<String, Object>> getDeviceCount(Map<String, Object> params);

	/**
	 * 获取区域安全运行天数
	 * 
	 * @param params
	 * @return
	 */
	String getSafeRunDays(Map<String, Object> params);

	Map<String, Object> getPowerGeneration(Map<String, Object> params);

}
