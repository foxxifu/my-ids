package com.interest.ids.biz.web.appsettings.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.interest.ids.biz.web.appsettings.controller.params.DeviceSignalParams;
import com.interest.ids.biz.web.appsettings.controller.params.SignalUpdateParams;
import com.interest.ids.biz.web.appsettings.vo.SignalConfigVO;
import com.interest.ids.biz.web.appsettings.vo.SignalModelVo;
import com.interest.ids.common.project.bean.Pagination;
import com.interest.ids.common.project.bean.signal.SignalInfo;
import com.interest.ids.common.project.bean.signal.SignalVersionInfo;

/**
 * @Author: sunbjx
 * @Description: 接口：系统设置-参数配置-点表
 * @Date Created in 14:38 2018/1/2
 * @Modified By:
 */
public interface SignalConfigService {
	
    /**
     * 点表导入
     *
     * @param file excel
     * @return string:{1:入库成功，入库失败返回提示信息}
     */
    String importExcel(MultipartFile file, String userType, Long enterpriseId, Map<String, String> map);

    /**
     * 通过版本ID删除点表依赖数据
     *
     * @param ids
     * @return
     */
    boolean deleteByVersionId(List<String> ids, Map<String, String> map);

    /**
     * 点表导入后列表信息查询
     *
     * @param
     * @return
     */
    List<SignalConfigVO> listSignalInfo(Pagination params);

    /**
     * 更新设备信号参数
     *
     * @param params
     * @return
     */
    boolean updateSignal(ArrayList<SignalUpdateParams> params, Long modelVersionId);

    /**
     * 遥信转告警, 告警转遥信
     *
     * @param ids
     * @return
     */
    void convertToSignal(String modelIds,Long signalVersionId);

    /**
     * 信号点实例
     *
     * @param params
     * @return
     */
    List<SignalInfo> listSignalInfoByVersion(DeviceSignalParams params);

    String getDeviceNameById(Long deviceId);

    /**
     * 获取点表模型
     * 
     * @param vo
     * 		点表id，信号点类型
     * @return 点表模型
     */
    List<SignalModelVo> getSignalModel(SignalModelVo vo);

    /**
     * 遥信转告警
     * 
     * @param alarmLevel
     * @param modelIds
     * @param signalVersionId
     */
	void convertToAlarm(byte alarmLevel, String modelIds, Long signalVersionId,byte teleType);

	/**
	 * 根据点表id查询设备信息
	 * 
	 * @param id
	 * @return
	 */
	List<Object[]> selectSignalDev(String id, String signalVersion);

	/**
	 * 根据signal version id获取点表信息
	 * 
	 * @param id
	 * @return
	 */
	SignalVersionInfo getSignalVersionById(Long id);

	/**
	 * 根据点表id查询信号点信息
	 * 
	 * @param id
	 * @return
	 */
	List<Object[]> selectSignalInfo(String id);
	/**
	 * 获取所有的mqqt的版本信息
	 * @return
	 */

	List<SignalVersionInfo> getMqttVersions(String protocal);
	/**
	 * 获取所有mqtt用户信息
	 * @return
	 */
	List<Object[]> selecMqttUsers(Map<String, String> map);
	/**
	 * 根据 signalVersion获取点表信息
	 * @param signalVersion
	 * @return
	 */
	List<Object[]> selectMqttSignalDev(String signalVersion);
	/**
	 * 根据id查询信号点信息
	 * @param id
	 * @return
	 */
	List<Object[]> selectMqttSignalInfo(String id);
	/**
	 * 根据 signalVersion获取告警点表信息
	 * @param signalVersion
	 * @return
	 */
	List<Object[]> selectMqttAlarm(String signalVersion);
    
}
