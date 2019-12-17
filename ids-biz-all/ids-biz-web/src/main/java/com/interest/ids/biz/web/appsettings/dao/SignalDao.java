package com.interest.ids.biz.web.appsettings.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.interest.ids.biz.web.appsettings.controller.params.SignalUpdateParams;
import com.interest.ids.biz.web.appsettings.vo.SignalConfigVO;
import com.interest.ids.biz.web.appsettings.vo.SignalModelVo;
import com.interest.ids.common.project.bean.Pagination;

/**
 * @Author: sunbjx
 * @Description:
 * @Date Created in 18:55 2018/1/16
 * @Modified By:
 */
public interface SignalDao {

    /**
     * 点表导入查询列表
     *
     * @param params
     * @return
     */
    List<SignalConfigVO> listSignalInfo(Pagination params);

    boolean updateIsAlarmFlagById(@Param("type") boolean type, @Param("modelIds") List<String> ids,
    		@Param("stationCode") String stationCode);

    boolean updateByParams(@Param("params") ArrayList<SignalUpdateParams> params);

    /**
     * 获取点表模型
     * 
     * @param vo
     * 		点表id，信号点类型
     * @return 点表模型
     */
    List<SignalModelVo> getSignalModel(@Param("vo") SignalModelVo vo);

	void updateAlarmToYx(@Param("type") boolean type, @Param("modelIds") List<String> ids,
    		@Param("stationCode") String stationCode);

	/**
	 * 根据点表id获取设备信息
	 * 
	 * @param id
	 * @return
	 */
	List<Map<String, Object>> selectSignalDev(@Param("id") String id, @Param("signalVersion") String signalVersion);

	/**
	 * 根据点表id查询信号点信息
	 * 
	 * @param id
	 * @return
	 */
	List<Map<String, Object>> selectSignalInfo(String id);
	/**
	 * 获取所有的mqtt用户信息
	 * @return
	 */
	List<Map<String, Object>> queryMqttUsers();
	/**
	 * 根据signalVersion 获取设备信息
	 * @param signalVersion
	 * @return
	 */
	List<Map<String, Object>> selectMqttSignalDev(@Param("signalVersion") String signalVersion);
	/**
	 * 根据id获取信号点信息
	 * @param id
	 * @return
	 */
	List<Map<String, Object>> selectMqttSignalInfo(@Param("signalVersion") String signalVersion);
	/**
	 * 根据 signalVersion 获取告警点表信息
	 * @param signalVersion
	 * @return
	 */
	List<Map<String, Object>> selectMqttAlarm(@Param("signalVersion") String signalVersion);
}
