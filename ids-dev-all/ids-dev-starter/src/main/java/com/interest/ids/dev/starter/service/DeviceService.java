package com.interest.ids.dev.starter.service;

import com.interest.ids.common.project.bean.device.DevUpgradeDto;
import com.interest.ids.common.project.bean.device.DeviceDetail;
import com.interest.ids.common.project.bean.device.DeviceInfoDto;
import com.interest.ids.common.project.bean.device.DevicePvCapacity;
import com.interest.ids.common.project.bean.device.PvCapacityM;
import com.interest.ids.common.project.bean.device.StationDevicePvModule;
import com.interest.ids.common.project.bean.device.StationPvModule;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.dto.DevUpgradeSearchParams;
import com.interest.ids.dev.starter.dto.ScDevBindParams;

import java.util.List;
import java.util.Map;

/**
 * @Author: sunbjx
 * @Description: 设备服务接口(RESTFUL)
 * @Date Created in 10:26 2017/12/19
 * @Modified By:
 */
public interface DeviceService {

    /**
     * 根据设备类型 ID 查询设备信息
     * @param deviceTypeId
     * @return
     */
    List<DeviceInfo> getByDeviceTypeId(Integer deviceTypeId);

    /**
     * 根据 ID 更新 IP 和 端口
     * @param id
     * @param ip
     * @param port
     * @return
     */
    int update(Long id, String ip, Integer port);

    /**
     * 获取设备类型等于 2 的 esnCode 等于 parentEsnCode
     * @param parentEsnCode
     * @return
     */
    List<DeviceInfo> getByEsn(String parentEsnCode);

    /**
     * 根据条件统计总记录数
     * @param dto
     * @return
     */
    Integer selectAllCount(DeviceInfoDto dto);

    /**
     * 根据条件查询设备相关信息
     * @param dto
     * @return
     */
    List<DeviceInfoDto> selectDeviceByCondtion(DeviceInfoDto dto);

    /**
     * 根据设备id查询设备详情
     * @param id
     * @return
     */
    DeviceInfoDto selectDeviceById(Long id);

    /**
     * 更新设备
     * @param dto
     * @return
     */
    int updateDeviceById(DeviceInfo deviecInfo);

    /**
     * 根据设备的id查询设备
     * @param ids
     * @return
     */
    List<DeviceInfo> selectDeviceByIds(String[] ids);

    /**
     * 批量插入组串容量
     * @param insertCapacity
     * @return
     */
    int insertCapacity(List<DevicePvCapacity> insertCapacity);

    /**
     * 根据设备id查询组串容量
     * @param deviceId
     * @return
     */
    List<PvCapacityM> getPvCapacityMByDeviceId(long deviceId);

    /**
     * 更新组串容量
     * @param insertCapacity
     * @return
     */
    int updateCapacity(List<DevicePvCapacity> insertCapacity);

    /**
     * 查询所有制造商和型号
     * @return
     */
    Map<String,List<StationPvModule>> selectStationPvModule();

    /**
     * 根据型号的id查询详情
     * @param id
     * @return
     */
    StationPvModule selectStationPvModuleDetail(Long id);

    /**
     * 保存组串详情配置
     * @param list
     * @param modules
     * @return
     */
    int saveStationPvModule(List<DeviceInfo> list,
            List<StationDevicePvModule> modules,Integer num);

    /**
     * 查询设备详情
     * @param id
     * @return
     */
    DeviceDetail selectDeviceDetail(Long id);

    /**
     * 查询企业下所有未绑定集中式逆变器的汇率箱的id和name
     * @param enterpriseId
     * @return
     */
	List<DeviceInfoDto> getDCJSIdAndName(Long enterpriseId);

	/**
	 * 根据id查询支流汇率的详情信息
	 * @param id
	 * @return
	 */
	List<Map<String,Object>> getDCJSDetail(String id);

	/**
	 * 统计所有未被绑定的直流汇率箱的数量
	 * @param dto
	 * @return
	 */
	Integer selectAllDCJSCount(DeviceInfoDto dto);

	/**
	 * 根据条件查询未被绑定的直流汇率箱的详情
	 * @param dto
	 * @return
	 */
	List<DeviceInfoDto> selectDCJSByCondtion(DeviceInfoDto dto);

	/**
	 * 插入集中式逆变器和直流汇率箱的绑定关系
	 * @param dto
	 */
	void insertCenterVerDetail(DeviceInfoDto dto);

	/**
	 * 根据集中是逆变器查询绑定的汇率箱的id
	 * @param id
	 * @return
	 */
	Map<String, Object> getDCJSByShip(Long id);

	/**
	 * 查询绑定的汇率箱的id和名字
	 * @param id
	 * @return
	 */
	List<DeviceInfoDto> getBindedById(Long id);
	
	/**
	 * 根据集中式逆变器设备删除已经存在的关系
	 * @param id
	 */
	void deleteCenterVerDetailByCenterId(Long id);

	/**
	 * 统计当前添加的汇率箱是否已被其他设备绑定
	 * @param ids
	 * @return
	 */
	Integer countDCJS(String ids);
	/**
	 * 通过版本的id获取铁牛数采设备的id
	 * @param versionIdList
	 * @return
	 */
	List<Long> getDevIdsByVersionId(List<Long> versionIdList);
	
	/**
	 * 根据通管机/铁牛数采的版本号，获取通管机/铁牛数采设备
	 * @param vesionId 通管机/铁牛数采版本id
	 * @return DeviceInfo 通管机/铁牛数采设备
	 */
	DeviceInfo getTGJDevByVersionId (Long vesionId);
	/**
	 * 根据电站名称设备名称模糊查询，设备类型，设备型号准确查询设备的升级状况
	 * @param params
	 * @return
	 */
	List<DevUpgradeDto> getDevUpgradeListByCondition(Map<String,Object> params);
	
	/**
	 * 修改设备根据用户名更新密码
	 * @param username
	 * @param password
	 * @return
	 */
	int updateMqttDevPassword(String username,String password);
	/**
	 * 根据父设备的Id查找出所有子设备的信息
	 * @param parentDevId
	 * @return
	 */
	List<DeviceInfoDto> getChildDevListByParentDevId(Long parentDevId);
	/**
	 * 根据id查询设备信息
	 */
	DeviceInfo getDeviceInfoById(Long devId);
	/**
	 * 查询所有未绑定电站的MODBUS协议的数采设备
	 * @return
	 */
	List<DeviceInfo> findAllModbusUnbindScDevs();
	/**
	 * 将数采绑定到电站中，并且需要将他下面的子设备一起绑定到对应的电站中
	 * @param params
	 */
	void bindScDevToStation(ScDevBindParams params);
	/**
	 * 查询所有已经绑定电站的MODBUS协议的数采设备
	 * @return
	 */
	List<DeviceInfo> findAllModbusBindScDevs();
	/**
	 * 根据设备ID获取设备列表
	 * @param devIds
	 * @return
	 */
	List<DeviceInfo> findDevsFromDbByIds(String[] devIds);
	/**
	 * 修改设备升级上传文件的路径信息
	 * @param devIds
	 * @param upgradePath
	 * @return
	 */
	int updateDevsUpgradeByDevList(String[] devIds, String upgradePath);
	/**
	 * 查询设备升级的信息
	 * @param devUpgradeSearchParams
	 * @return
	 */
	List<DeviceInfo> findUpgradeDevInfos(DevUpgradeSearchParams devUpgradeSearchParams);
	/**
	 * 根据设备sn删除点表信息中的信号点
	 * @param devSns
	 */
	void deleteModbusDevInSns(List<String> devSns);
}
