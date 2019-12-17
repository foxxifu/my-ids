package com.interest.ids.commoninterface.service.station;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.interest.ids.common.project.bean.TreeModel;
import com.interest.ids.common.project.bean.device.DeviceInfoDto;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.sm.Page;
import com.interest.ids.common.project.bean.sm.QueryStationInfo;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.bean.sm.StationShareemi;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.commoninterface.dto.StationInfoDto;

public interface StationInfoMService {
	/**
	 * 新建一个电站信息
	 * 
	 * @param station
	 * @return
	 */
	public int insertStation(StationInfoM station);

	/**
	 * 根据id查询电站
	 * 
	 * @param id
	 * @return
	 */
	public StationInfoM selectStationInfoMById(Long id);

	List<StationInfoM> listStationsByStationCodes(
			Collection<String> stationCodes);

	/**
	 * 根据任意条件查询电站信息
	 * 
	 * @param queryStation
	 * @return
	 */
	public List<StationInfoM> selcetStationInfoMsByCondition(
			QueryStationInfo queryStation);

	/**
	 * 分页查询站点数据
	 * 
	 * @param page
	 * @return
	 */
	public List<StationInfoM> selectStationInfoMByPage(Page<StationInfoM> page);

	/**
	 * 更新电站和设备信息电站
	 * 
	 * @param station
	 *            电站和设备信息
	 */
	public void updateStationInfoMById(StationInfoM stationInfo);

	/**
	 * 根据id删除电站
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteStationInfoMById(Long id);

	/**
	 * 根据多个id批量删除电站,所个id以逗号(,)隔开
	 * 
	 * @param ids
	 *            电站id以逗号隔开的字符串
	 */
	public void deleteStationInfosByCodes(String ids);

	/**
	 * 根据电站编码查询电站信息
	 * 
	 * @param stationCode
	 * @return
	 */
	public StationInfoM selectStationInfoMByStationCode(String stationCode);

	/**
	 * 根据stationcode查询电站的名字
	 * 
	 * @param stationCode
	 * @return
	 */
	public String selectStationInfoMNameByStationCode(String stationCode);

	/**
	 * 根据stationCode删除站点
	 * 
	 * @param stationCode
	 * @return
	 */
	public boolean deleteStationInfoMByStationCode(String stationCode);

	/**
	 * 根据多个stationCode批量删除
	 * 
	 * @param stationCodes
	 * @return
	 */
	public int deleteStationInfoMsByStationCode(String[] stationCodes);

	/**
	 * 获取
	 * 
	 * @param stationCodes
	 * @return
	 */
	Map<String, Long> getShareEmiByStationCodes(Collection<String> stationCodes);

	/**
	 * 通过电站编号查询共享环境监测仪
	 * 
	 * @param stationCodes
	 * @param emiStationMap
	 * @return
	 */
	Map<String, Long> getShareEmiByStationCodes(
			Collection<String> stationCodes, Map<String, String> emiStationMap);

	/**
	 * 更新电站信息
	 */
	void updateStationStata();

	/**
	 * 查询所有电站
	 * 
	 * @return List<StationInfoM>
	 */
	List<StationInfoM> getAllStations();

	/**
	 * 查询电站下存在严重告警的电站编号集合
	 * 
	 * @param stationCodes
	 * @return
	 */
	List<String> getStationWithCriticalAlarm(List<String> stationCodes);

	// 根据企业id查询所有的站点
	public List<StationInfoM> selectStationInfoMByEnterpriseId(Long enterpriseId);

	/**
	 * 根据企业id统计电站个数
	 * 
	 * @param enterpriseId
	 * @return
	 */
	public Integer selectStationNumberByEnterprise(Long enterpriseId);

	/**
	 * 根据用户id查询所有的站点 -暂未提供
	 * 
	 * @param userId
	 * @return
	 */
	public List<StationInfoM> selectStationInfoMByUserId(
			Map<String, Object> userId);

	/**
	 * 根据用户id统计站点个数
	 * 
	 * @param userId
	 * @return
	 */
	public Integer selectStationCountByUserId(Long userId);

	/**
	 * 根据用户id分页查询站点数据
	 * 
	 * @param page
	 * @return
	 */
	public List<StationInfoM> selectStationInfoMByUserIdAndPage(
			Page<StationInfoM> page);

	/**
	 * 根据电站编号查询所有共享设备的id
	 * 
	 * @param stationCodes
	 * @return
	 */
	public Map<String, Long> getSharedDeviceByStationCodes(
			List<String> stationCodes);

	/**
	 * 查询所有拥有环境检测仪的电站
	 * 
	 * @param queryParams
	 *            id:userId;type_:system/enterprise;
	 * @return List<StationInfoM>
	 */
	public List<StationInfoM> getStationInfoByEmiId(
			Map<String, Object> queryParams);

	/**
	 * 根据电站编号查询电站下面的所有的环境检测仪
	 * 
	 * @param stationCode
	 * @return
	 */
	public List<DeviceInfoDto> getEmiInfoByStationCode(String stationCode);

	/**
	 * 插入电站共享环境检测仪数据
	 * 
	 * @param share
	 *            共享环境检测仪实体类
	 * @return
	 */
	public void insertDeviceShare(StationShareemi share);

	/**
	 * 查询电站共享环境监测仪数据
	 * 
	 * @return
	 */
	List<StationShareemi> getAllSharedEmis();

	/**
	 * 校验电站名称是否存在
	 * 
	 * @param stationName
	 *            电站名称
	 * @return 是否存在
	 */
	StationInfoM checkStationNameIsExists(String stationName);

	/**
	 * 分页查询电站信息
	 * 
	 * @param queryParams
	 *            电站查询条件
	 * @return
	 */
	public Page<StationInfoM> getStationInfoByPage(
			Map<String, Object> queryParams);

	/**
	 * 获取当前用户的区域企业结构树
	 * 
	 * @param queryParams
	 *            id、type_
	 * @return List<TreeModel>
	 */
	public List<TreeModel> getUserDomainTree(Map<String, Object> queryParams);

	/**
	 * 保存电站和设备信息
	 * 
	 * @param stationDto
	 *            电站和设备信息
	 * @return 是否成功
	 */
	public void insertStationAndDevice(StationInfoDto stationDto);

	/**
	 * 查询该电站编号下面是否已经绑定环境检测仪
	 * 
	 * @param stationCode
	 *            电站编号
	 * @return 绑定环境检测仪数
	 */
	public int getShareRmiSize(String stationCode);

	/**
	 * 通过电站编号更新绑定的环境检测仪
	 * 
	 * @param share
	 *            共享环境检测仪实体类
	 */
	public void updateDeviceShare(StationShareemi share);

	/**
	 * 根据电站编号查询电站信息
	 * 
	 * @param stationCode
	 * @return
	 */
	public StationInfoM getStationByCode(String stationCode);

	/**
	 * 根据id查询对应的电价
	 * 
	 * @param queryType
	 *            1：企业；2：区域；3：电站
	 * @param queryId
	 *            对应id
	 * @return 电价
	 */
	public Double getPowerPriceById(String queryType, String queryId);

	/**
	 * 根据电站编号查询设备列表
	 * 
	 * @param stationCode
	 * @return
	 */
	public List<DeviceInfo> getDevicesByStationCode(String stationCode);

	/**
	 * 更新电站和设备信息
	 * 
	 * @param stationDto
	 *            电站和设备信息
	 */
	public void updateStationAndDeviceInfo(StationInfoDto stationDto,List<Long> emiChanged);

	/**
	 * 批量新增或更新共享环境检测仪
	 * 
	 * @param shareEmiList
	 *            共享环境检测仪数据
	 */
	public void insertAndUpdateShareEmi(List<StationShareemi> shareEmiList);
	
	/**
	 * 初始化缓存
	 */
	void initStationCache();

	/**
	 * 根据电站编号查询电价
	 * 
	 * @param stationCode
	 * 				电站编号
	 * @return 电价列表
	 */
	public List<Map<String, Object>> getPriceByStationCode(String stationCode);

	/**
	 * 根据用户查询当前用户下管理的电站
	 * 
	 * @param user
	 * 		用户信息
	 * @return List<StationInfoM>
	 */
	public List<StationInfoM> getStationByUser(UserInfo user);
	
	/**
	 * 根据电站名，模糊匹配电站列表
	 * @param stationName
	 * @return
	 */
	public List<StationInfoM> getStationByStationName(String stationName);

	/**
	 * 查询未绑定区域的电站
	 * @param queryParams
	 * @return
	 */
	public Page<StationInfoM> getNoBindingStationInfoByPage(
			Map<String, Object> queryParams);
}
