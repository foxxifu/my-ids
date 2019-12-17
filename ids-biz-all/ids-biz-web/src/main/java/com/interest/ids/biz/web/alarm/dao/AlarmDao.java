package com.interest.ids.biz.web.alarm.dao;

import com.interest.ids.biz.web.alarm.controller.params.AlarmParams;
import com.interest.ids.biz.web.alarm.vo.AlarmRepariVO;
import com.interest.ids.biz.web.alarm.vo.AlarmStatisticsVO;
import com.interest.ids.biz.web.alarm.vo.AlarmVO;
import com.interest.ids.biz.web.alarm.vo.AnalysisRepariVO;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: sunbjx
 * @Description: 告警DAO层
 * @Date: Created in 下午4:37 18-1-8
 * @Modified By:
 */
public interface AlarmDao {

    /**
     * 告警信息
     *
     * @param params
     * @return
     */
    List<AlarmVO> listInfoByUserId(@Param("params") AlarmParams params);

    /**
     * 根据设备id获取告警信息
     *
     * @return
     */
    List<AlarmVO> listInfoByDevId(@Param("params") AlarmParams params);


    /**
     * 告警修复建议
     *
     * @param alarmId 告警id
     * @return
     */
    AlarmRepariVO getAlarmRepariByAlarmId(Long alarmId);

    /**
     * 告警统计
     *
     * @param userId 用户id
     * @return
     */
    AlarmStatisticsVO getStatisticsByUserId(Long userId);

    /**
     * 智能告警列表
     *
     * @param params
     * @return
     */
    List<AlarmVO> listAnalysisInfo(@Param("params") AlarmParams params);

    /**
     * 智能告警修复信息
     *
     * @param id
     * @return
     */
    List<AnalysisRepariVO> getAnalysisRepari(Long id);

    /**
     * 统计某设备上的活动告警数
     * * @return
     */
    List<Map<String, Object>> countDeviceCriticalAlarms(@Param("stationCodes") List<String> stationCodes);

    /**
     * 统计设备上的严重告警数
     *
     * @param deviceIds
     * @return
     */
    List<Map<String, Object>> countDeviceCriticalAlarmsByDeviceId(@Param("deviceIds") List<Long> deviceIds);

	void updateAlarm(Map<String, Object> condition);
	/**
	 * 查询实时告警信息
	 * @param params
	 * @return
	 */
	List<AlarmVO> listOnlineInfoByUserId(@Param("params")AlarmParams params);

}
