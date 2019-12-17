package com.interest.ids.common.project.mapper.signal;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.interest.ids.common.project.bean.alarm.DeviceAlamDto;
import com.interest.ids.common.project.bean.device.DevUpgradeDto;
import com.interest.ids.common.project.bean.device.DeviceDetail;
import com.interest.ids.common.project.bean.device.DeviceInfoDto;
import com.interest.ids.common.project.bean.device.DeviceProfileDto;
import com.interest.ids.common.project.bean.device.DevicePvModuleDto;
import com.interest.ids.common.project.bean.device.DeviceSignalDataDto;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.sm.StationShareemi;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.utils.CommonMapper;

public interface DevInfoMapper extends CommonMapper<DeviceInfo> {

    /**
     * 插入返回主键
     * 
     * @param deviceInfo
     * @return
     */
    int insertAndGetId(DeviceInfo deviceInfo);

    List<DeviceInfo> selectDeviceBySidsAndDeviceTypeIds(@Param("stationCodes") Collection<String> stationCodes,
            @Param("deviceTypeIds") Collection<Integer> deviceTypeIds);

    List<DeviceInfo> selectDeviceBySidsAndDeviceIds(@Param("stationCodes") Collection<String> stationCodes,
            @Param("deviceIds") Collection<Long> deviceIds);

    List<DeviceInfo> selectDeviceByDeviceIds(Collection<Long> deviceIds);

    List<Map<String, Object>> selectDevicesByStaionCodeAndDeviceType(
            @Param("stationCodes") Collection<String> stationCodes, @Param("deviceTypeId") Integer deviceTypeId);

    List<DeviceInfo> selectDevicesWithStationCodes(Collection<String> stationCodes);

    // 根据条件统计总记录数
    Integer selectAllCount(DeviceInfoDto dto);

    // 根据条件查询设备相关信息
    List<DeviceInfoDto> selectDeviceByCondtion(DeviceInfoDto dto);

    // 根据设备id查询设备详情
    DeviceInfoDto selectDeviceById(Long id);

    /**
     * 查询所有未被删除的设备
     * 
     * @return
     */
    List<DeviceInfo> selectAllDeviceInfoMs();

    /**
     * 查询设备的厂家名称
     * 
     * @param modelVersionCode
     * @return
     */
    String selectVenderName(String modelVersionCode);

    /**
     * 查询组串相关信息
     * 
     * @param id
     * @return
     */
    Map<String, Object> selectModuleInfo(Long id);

    /**
     * 根据设备SN号查询设备信息
     * 
     * @param snCode
     *            设备SN号
     * @return 设备信息
     */
    DeviceInfo getDeviceInfoBySN(String snCode);

    /**
     * 查询设备类型对应的moduleVersionCode
     * 
     * @return List<Map<String, Object>>
     */
    List<Map<String, Object>> getModuleVersionCodeList();

    /**
     * 保存设备信息
     * 
     * @param devList
     */
    void insertDevInfos(List<DeviceInfo> devList);

    /**
     * 根据电站编号查询该电站绑定的环境监测仪数
     * 
     * @param stationCode
     *            电站编号
     * @return 数量
     */
    int getShareRmiSize(String stationCode);

    /**
     * 更新共享环境检测仪信息
     * 
     * @param share
     */
    void updateDeviceShare(StationShareemi share);

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
    int updateDevInfos(DeviceInfo deviceInfo);

    /**
     * 根据设备id删除设备信息
     * 
     * @param devIds
     *            设备id数组
     */
    void deleteDeviceInfos(Object[] devIds);

    /**
     * 根据id更新设备信息
     * 
     * @param deviecInfo
     * @return
     */
    int updateDeviceById(DeviceInfo deviecInfo);

    /**
     * 按设备类型统计电站下的设备的个数
     * 
     * @param stationCode
     * @return
     */
    List<Map<Integer, Long>> countDeviceByStationCode(String stationCode);

    /**
     * 按设备类型统计设备的厂商和数量
     * 
     * @param stationCode
     * @return
     */
    List<DeviceInfoDto> getDeviceVenders(String stationCode);

    /**
     * 统计电站下的直流汇率箱的个数
     * 
     * @param stationCode
     * @return
     */
    Long countDCJS(String stationCode);

    /**
     * 根据条件查询设备的告警数据条数
     * 
     * @param condition
     * @return
     */
    Long countAlarmByDevId(DeviceAlamDto condition);

    /**
     * 根据条件查询设备的信息和设备的告警信息
     * 
     * @param condition
     * @return
     */
    List<DeviceAlamDto> selectDevAlarm(DeviceAlamDto condition);

    /**
     * 查询设备统计数据
     * 
     * @param user
     * @return
     */
//    List<DeviceProfileDto> selectDevProfile(@Param("user") UserInfo user, @Param("stationCode") String stationCode);
    /**
     * 查询设备统计
     * @param id 用户id
     * @param enterpriseId 企业id
     * @param stationCode 电站编号
     * @return
     */
    List<DeviceProfileDto> selectDevProfile(@Param("id") Long id, @Param("enterpriseId") Long enterpriseId, @Param("stationCode") String stationCode, @Param("userType") String userType);

    /**
     * 根据设备类型查询设备归一化配置，以便获取实时数据
     * 
     * @param devId
     * @return
     */
    List<DeviceSignalDataDto> selectDevSignalConf(@Param(value = "devId") Long devId);

    /**
     * 查询设备组件基本信息
     * 
     * @param devId
     * @return
     */
    List<DevicePvModuleDto> selectDevicePvInfos(@Param(value = "devId") Long devId);

    /**
     * 查询设备是否存在严重告警，用于判断设备是否故障
     * 
     * @param deviceIds
     * @return
     */
    List<Long> countCritAlarmsByDeviceIds(@Param("deviceIds") List<Long> deviceIds);

    /**
     * 查询企业下所有未绑定集中式逆变器的汇率箱的id和name
     * 
     * @param enterpriseId
     * @return
     */
    List<DeviceInfoDto> getDCJSIdAndName(Long enterpriseId);

    /**
     * 根据id查询支流汇率的详情信息
     * 
     * @param id
     * @return
     */
    List<Map<String, Object>> getDCJSDetail(String id);

    /**
     * 统计所有未被绑定的直流汇率箱
     * 
     * @param dto
     * @return
     */
    Integer selectAllDCJSCount(DeviceInfoDto dto);

    /**
     * 根据条件查询未被绑定的直流汇率箱的详情
     * 
     * @param dto
     * @return
     */
    List<DeviceInfoDto> selectDCJSByCondtion(DeviceInfoDto dto);

    /**
     * 插入集中式逆变器和直流汇率箱的绑定关系
     * 
     * @param map
     */
    void insertCenterVerDetail(Map<String, Object> map);

    /**
     * 查询设备及其关联的子设备信息
     * 
     * @param devId
     * @return
     */
    List<DeviceDetail> selectDevWithChild(Long devId);

    /**
     * 根据集中是逆变器查询绑定的汇率箱的id
     * 
     * @param id
     * @return
     */
    Long[] getDCJSByShip(Long id);

    /**
     * 查询绑定的汇率箱的id和名字
     * 
     * @param id
     * @return
     */
    List<DeviceInfoDto> getBindedById(Long id);

    /**
     * 根据集中式逆变器设备删除已经存在的关系
     * 
     * @param id
     */
    void deleteCenterVerDetailByCenterId(Long id);

    /**
     * 统计当前添加的汇率箱是否已被其他设备绑定
     * 
     * @param ids
     * @return
     */
    Integer dCJSCount(String ids);

    /**
     * 查询指定子阵下的设备编号集
     * 
     * @param matrixId
     * @return
     */
    List<Long> queryDevIdsByMatrixId(@Param(value = "matrixId") Long matrixId,
            @Param(value = "devTypeIds") List<Integer> devTypeIds);
    /**
     * 通过版本信息的id获取铁牛数采的设备id
     * @param versionIdList
     * @return
     */
	List<Long> getDevIdsByVersionId(@Param(value = "versionIdList")List<Long> versionIdList);
	/**
	 * 通过通管机/铁牛数采版本的id获取通管机/铁牛数采设备
	 * @param versionId 通管机/铁牛数采版本的id
	 * @return
	 */
	List<DeviceInfo> getTGJByVersionId(Long versionId);
	/**
	 * 查询所有的采集器设备
	 * @param params
	 * @return
	 */
	List<DevUpgradeDto> selectUpgradeDevByCoudition(Map params);
	/**
	 * 根据父设备(采集器)的Id获取采集器下挂的需要升级的设备
	 * @param parentDevId
	 * @return
	 */
	List<DeviceInfoDto> getChildDevList(@Param(value = "id") Long parentDevId);
}