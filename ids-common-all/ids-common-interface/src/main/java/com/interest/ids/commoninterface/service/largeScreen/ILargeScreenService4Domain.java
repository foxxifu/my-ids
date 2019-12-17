package com.interest.ids.commoninterface.service.largeScreen;

import java.util.Map;

/**
 * 区域大屏业务逻辑处理类
 * 
 * @author claude
 *
 */
public interface ILargeScreenService4Domain {

	/**
	 * 查询区域层层下钻大屏地图显示数据
	 * 
	 * @param domainId
	 *            区域id
	 * @param userId
	 *            用户id
	 * @return @return map{"areaTree",地图左上角树形结构；"currentShowData",当前地图显示的区域和电站}
	 */
	Map<String, Object> getMapShowData(String domainId,
			Map<String, Object> userInfo);

	/**
	 * 根据区域id查询区域日负荷曲线
	 * 
	 * @param params
	 *            当前用户信息和区域id
	 * @return
	 */
	Map<String, Object> getActivePower(Map<String, Object> params);

	/**
	 * 根据区域id查询最新负荷
	 * 
	 * @param params
	 *            当前用户信息和区域id
	 * @return
	 */
	Double getCurrentActivePower(Map<String, Object> params);

	/**
	 * 根据区域id查询发电量趋势
	 * 
	 * @param params
	 *            当前用户信息和区域id
	 * @return
	 */
	Map<String, Object> getPowerTrends(Map<String, Object> params);

	/**
	 * 根据区域id查询当前大屏数据
	 * 
	 * @param params
	 *            当前用户信息和区域id
	 * @return
	 */
	Map<String, Object> getCommonData(Map<String, Object> params);

	/**
	 * 根据区域id查询电站分布
	 * 
	 * @param params
	 *            当前用户信息和区域id
	 * @return
	 */
	Map<String, Object> getListStationInfo(Map<String, Object> params);

	/**
	 * 根据区域id查询社会贡献
	 * 
	 * @param params
	 *            当前用户信息和区域id
	 * @return
	 */
	Map<String, Object> getSocialContribution(Map<String, Object> params);

	/**
	 * 根据区域id查询设备分布
	 * 
	 * @param params
	 *            当前用户信息和区域id
	 * @return
	 */
	Map<String, Object> getDeviceCount(Map<String, Object> params);

	/**
	 * 查询大屏发电数据
	 * 
	 * @param params
	 * @return
	 */
	Map<String, Object> getPowerGeneration(Map<String, Object> params);

}
