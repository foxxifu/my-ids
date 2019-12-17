package com.interest.ids.commoninterface.dao.station;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.interest.ids.common.project.bean.analysis.PowerPriceM;
import com.interest.ids.common.project.bean.sm.Page;
import com.interest.ids.common.project.bean.sm.QueryStationInfo;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.bean.sm.UserInfo;

public interface StationInfoMMapper {

	/**
	 * 新建一个电站信息
	 * 
	 * @param station
	 * @return
	 */
	int insertstation(StationInfoM station);

	/**
	 * 根据id查询电站
	 * 
	 * @param id
	 * @return
	 */
	StationInfoM selectStationInfoMById(Long id);

	/**
	 * 根据任意条件查询电站信息 - 没有条件则查询所有
	 * 
	 * @param queryStation
	 * @return
	 */
	List<StationInfoM> selcetStationInfoMsByCondition(
			QueryStationInfo queryStation);

	/**
	 * 查询站点总条数
	 * 
	 * @param station
	 * @return
	 */
	int selectAllCount(StationInfoM station);

	/**
	 * 分页查询站点数据
	 * 
	 * @param page
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List<StationInfoM> selectStationInfoMByPage(Page page);

	/**
	 * 修改电站信息
	 * 
	 * @param station
	 *            电站信息
	 */
	void updateStationInfoMById(StationInfoM station);

	/**
	 * 根据id删除电站
	 * 
	 * @param id
	 * @return
	 */
	boolean deleteStationInfoMById(Long id);

	/**
	 * 根据多个id批量删除电站
	 * 
	 * @param ids
	 *            电站id以逗号隔开的字符串
	 */
	int deleteStationInfosByCodes(String[] idArray);

	/**
	 * 根据stationcode查询电站的名字
	 * 
	 * @param stationCode
	 * @return
	 */
	StationInfoM selectStationInfoMNameByStationCode(String stationCode);

	/**
	 * 根据stationCode删除站点
	 * 
	 * @param stationCode
	 * @return
	 */
	boolean deleteStationInfoMByStationCode(String stationCode);

	/**
	 * 根据多个stationCode批量删除
	 * 
	 * @param stationCodes
	 * @return
	 */
	int deleteStationInfoMsByStationCode(String[] stationCodes);

	/**
	 * 根据电站编码查询电站信息
	 * 
	 * @param stationCode
	 * @return
	 */
	StationInfoM selectStationInfoMByStationCode(String stationCode);

	/**
	 * 根据企业id查询所有的站点
	 * 
	 * @param enterpriseId
	 * @return
	 */
	List<StationInfoM> selectStationInfoMByEnterpriseId(Long enterpriseId);

	/**
	 * 统计电站个数
	 * 
	 * @param enterpriseId
	 * @return
	 */
	Integer selectStationNumberByEnterprise(Long enterpriseId);

	/**
	 * 根据用户id查询所有的站点
	 * 
	 * @param userInfo
	 *            id：userId;type_:system/enterprise
	 * @return
	 */
	List<StationInfoM> selectStationInfoMByUserId(Map<String, Object> userInfo);

	/**
	 * 根据用户id统计站点个数
	 * 
	 * @param userId
	 * @return
	 */
	Integer selectStationCountByUserId(Long userId);

	/**
	 * 根据用户id分页查询站点数据
	 * 
	 * @param page
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List<StationInfoM> selectStationInfoMByUserIdAndPage(Page page);

	/**
	 * 查询所有拥有环境检测仪的电站
	 * 
	 * @param deviceTypeId
	 * @return
	 */
	List<StationInfoM> getStationInfoByEmiId(Map<String, Object> queryParams);

	/**
	 * 传入电站编号列表查询所有电站信息
	 * 
	 * @param stationC
	 * @return
	 */
	List<StationInfoM> selectStationInfoMByStationCodes(
			Collection<String> stationC);

	/**
	 * 获取所有的未被逻辑删除的电站信息
	 * 
	 * @return
	 */
	List<StationInfoM> selectAllStations();
	/**
	 * 查询电站下存在严重告警的电站编号集合
	 * 
	 * @param stationCodes
	 * @return
	 */
	List<String> selectStationWithCriticalAlarm(Collection<String> stationCodes);

	/**
	 * 根据名称统计记录数
	 * 
	 * @param stationName
	 * @return StationInfoM
	 */
	StationInfoM checkStationNameIsExists(String stationName);

	/**
	 * @Author: sunbjx
	 * @Description: 通过电站名称查询电站信息
	 * @param stationName
	 *            电站名称
	 * @return 电站实例信息
	 */
	StationInfoM getStationInfoByStationName(String stationName);

	/**
	 * author:xm
	 * 
	 * @param 行政区域的id编号
	 *            ,用户的id
	 * @Description: 通过行政区域的id和用户的id查询电站的编码和名称
	 * @return 所有符合要求的电站
	 */
	public List<StationInfoM> selectstationInfoByAreaCode(
			Map<String, Object> param);

	/**
	 * 根据电站名称查询电站
	 * 
	 * @param stationName
	 *            电站名称
	 * @return 电站列表
	 */
	public List<StationInfoM> getStationInfoByName(String stationName);

	/**
	 * 根据查询条件查询电站信息
	 * 
	 * @param queryParams
	 * @return
	 */
	public List<StationInfoM> getStationInfo(Map<String, Object> queryParams);

	/**
	 * 根据查询条件查询电站信息总数
	 * 
	 * @param queryParams
	 * @return
	 */
	public Integer getStationInfoTotalCount(Map<String, Object> queryParams);

	/**
	 * 根据电站编号查询电站信息
	 * 
	 * @param stationCode
	 * @return
	 */
	StationInfoM getStationByCode(String stationCode);

	/**
	 * 根据id查询对应的电价
	 * 
	 * @param queryType
	 *            1：企业；2：区域；3：电站
	 * @param queryId
	 *            对应id
	 * @return 电价
	 */
	Double getPowerPriceById(@Param("queryType") String queryType,
			@Param("queryId") String queryId);

	/**
	 * 新增电站分时电价
	 * 
	 * @param priceList
	 */
	void insertPowerPrices(List<PowerPriceM> priceList);

	/**
	 * 删除电价信息
	 * @param stationCode
	 */
	void deletePowerPrices(String stationCode);
	/**
	 * 获取所有电站的电价信息
	 * 
	 * @return
	 */
	List<PowerPriceM> getAllStationPowerPrices();

	/**
	 * 查询电价
	 * 
	 * @param stationCode
	 * @return
	 */
	List<PowerPriceM> getPriceByStationCode(String stationCode);

	/**
	 * 根据用户查询当前用户所管理的电站
	 * 
	 * @param user
	 * 		用户信息
	 * @return List<StationInfoM>
	 */
	List<StationInfoM> getStationByUser(UserInfo user);
	
	/**
	 * 根据电站名称，模糊查询电站列表
	 * @param stationName
	 * @return
	 */
	List<StationInfoM> getStationByStationName(String stationName);

	List<Map<String, Object>> getStationDevAndType(List<String> sids);

	List<Map<String, Object>> getStationDevAndAlarm(List<String> sids);

	/**
	 * 统计未绑定区域的电站的个数
	 * @param queryParams
	 * @return
	 */
	int getNoBindingStationInfoTotalCount(Map<String, Object> queryParams);

	/**
	 * 查询未绑定区域的电信信息
	 * @param queryParams
	 * @return
	 */
	List<StationInfoM> getNoBindingStationInfo(Map<String, Object> queryParams);

	/**
	 * 只根据电站编码查询电站
	 * @param stationCode
	 * @return
	 */
	StationInfoM getStationByStationCode(String stationCode);
	/**
	 * 查询监控传递上来的电站
	 * @return
	 */
	List<StationInfoM> getAllMonitorStations();

}
