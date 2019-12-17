package com.interest.ids.commoninterface.service.device;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.interest.ids.common.project.bean.alarm.DeviceAlamDto;
import com.interest.ids.common.project.bean.device.DeviceDetail;
import com.interest.ids.common.project.bean.device.DeviceInfoDto;
import com.interest.ids.common.project.bean.signal.DeviceInfo;

public interface IDeviceInfoService {

	/**
	 * 通过电站编号列表查询设备列表
	 * 
	 * @param stationCodes
	 *            电站编号列表
	 * @param deviceTypes
	 *            设备类型
	 * @return 设备信息列表
	 */
	List<DeviceInfo> queryDevicesByStationCodes(
			Collection<String> stationCodes, Collection<Integer> deviceTypes);

	List<DeviceInfo> queryDevicesBySIdDId(List<Long> devcieIds,
			Set<String> stationCodes);

	List<DeviceInfo> queryDevicesByIds(Collection<Long> deviceIds);

	Map<String, List<Long>> queryStationDevicesMap(
			Collection<String> stationCodes, Integer deviceTypeId);

	/**
	 * 查询所有电站的设备列表
	 * 
	 * @param stationCodes
	 * @return 电站编号：设备编号集合
	 */
	Map<String, List<Long>> queryAllDevicesMap(Collection<String> stationCodes);

	/**
	 * 查询所有未删除的设备
	 * 
	 * @return
	 */
	List<DeviceInfo> queryAllDeviceInfoMs();

	/**
	 * 通过设备sn编号查询设备信息，
	 * 
	 * @param snCode
	 *            设备sn编号
	 * @return map{"code":1,设备信息有效；3,无设备信息；4，设备已被使用}，{"deviceInfo":DeviceInfo}，{
	 *         "message":ResponseConstants对应值}
	 */
	Map<String, Object> getDeviceInfoBySN(String snCode);

	/**
	 * 查询设备model version code list
	 * 
	 * @return List<Map<String, String>>
	 */
	List<Map<String, Object>> getModelVersionCodeList();

	/**
	 * 保存设备信息数据
	 * 
	 * @param devList
	 */
	void insertDevInfos(List<DeviceInfo> devList);

	/**
	 * 根据电站编号查询设备列表
	 * 
	 * @param stationCode
	 * @return
	 */
	List<DeviceInfo> getDevicesByStationCode(String stationCode);

	/**
	 * 更新设备信息
	 * 
	 * @param updateDeviceList
	 *            设备信息
	 */
	void updateDevInfos(DeviceInfo deviceInfo);

	/**
	 * 根据设备id删除设备信息
	 * 
	 * @param devIds
	 *            设备id数组
	 */
	void deleteDeviceInfos(Object[] devIds);
	
	/**
	 * 初始化设备缓存
	 */
	void initDeviceInfoCach();
	
	/**
	 * 按设备类型统计电站下的设备的个数
	 * @param stationCode
	 * @return
	 */
	List<Map<Integer,Long>> countDeviceByStationCode(String stationCode);

	/**
	 * 按设备类型统计设备的厂商和数量
	 * @param stationCode
	 * @return
	 */
	List<DeviceInfoDto> getDeviceVenders(String stationCode);

	/**
	 * 统计电站下的直流汇率箱的个数
	 * @param stationCode
	 * @return
	 */
	Long countDCJS(String stationCode);

	/**
	 * 根据条件统计设备的条数
	 * @param dto
	 * @return
	 */
	Integer selectAllCount(DeviceInfoDto dto);

	/**
	 * 根据条件分页查询设备的数据
	 * @param dto
	 * @return
	 */
	List<DeviceInfoDto> selectDeviceByCondtion(DeviceInfoDto dto);

	/**
	 * 根据设备的id查询设备的详情
	 * @param id
	 * @return
	 */
	DeviceDetail selectDeviceDetail(Long id);
	
	/**
	 * 查询设备及其关联设备信息
	 * @param id
	 * @return
	 */
	DeviceDetail selectDeviceWithChild(Long id);

	/**
	 * 根据条件查询设备告警的总条数
	 * @param condition
	 * @return
	 */
	Long countAlarmByDevId(DeviceAlamDto condition);

	/**
	 * 查询设备的告警数据
	 * @param condition
	 * @return
	 */
	List<DeviceAlamDto> selectDevAlarm(DeviceAlamDto condition);

}
