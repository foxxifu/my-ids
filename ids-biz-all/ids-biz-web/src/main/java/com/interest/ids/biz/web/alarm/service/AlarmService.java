package com.interest.ids.biz.web.alarm.service;

import com.interest.ids.biz.web.alarm.controller.params.AlarmParams;
import com.interest.ids.biz.web.alarm.vo.AlarmRepariVO;
import com.interest.ids.biz.web.alarm.vo.AlarmStatisticsVO;
import com.interest.ids.biz.web.alarm.vo.AlarmVO;
import com.interest.ids.biz.web.alarm.vo.AnalysisRepariVO;

import java.util.List;
import java.util.Map;

/**
 * @Author: sunbjx
 * @Description: 告警
 * @Date Created in 16:17 2018/1/2
 * @Modified By:
 */
public interface AlarmService {

    /**
     * 告警信息
     *
     * @return 用户id
     */
    List<AlarmVO> listInfoByUserId(AlarmParams params);
    /**
     * 告警实时告警信息
     *
     * @return 用户id
     */
    List<AlarmVO> listOnlineInfoByUserId(AlarmParams params);

    /**
     * 根据设备id获取告警信息
     *
     * @return
     */
    List<AlarmVO> listInfoByDevId(AlarmParams params);

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
     * 通过告警id更新告警信息
     *
     * @param ids
     * @return
     */
    Boolean updateStateById(List<Long> ids, int flag);

    /**
     * 查询指定电站集上的各类型设备严重告警数：stationCode:{deviceTypeId:count}
     *
     * @param deviceId
     * @return
     */
    Map<String, Map<Integer, Integer>> countDeviceCriticalAlarms(List<String> stationCodes);

    /**
     * 统计设备上的严重告警数
     *
     * @param deviceIds
     * @return
     */
    Map<Long, Integer> countDeviceCriticalAlarmsByDeviceId(List<Long> deviceIds);

    // **********************智能告警***********************

    /**
     * 告警信息
     *
     * @return 用户id
     */
    List<AlarmVO> listAnalysisInfoByUserId(AlarmParams params);

    /**
     * 告警修复建议
     *
     * @param id 告警id
     * @return
     */
    List<AnalysisRepariVO> getAnalysisRepariById(Long id);

    /**
     * 通过告警id更新告警信息
     *
     * @param ids
     * @return
     */
    Boolean updateAnalysisStateById(List<Long> ids, int flag);

    /**
     * 通过告警id更新告警信息
     *
     * @param ids
     * @return
     */
    Boolean updateStateMoreById(List<Long> ids, List<Long> autoIds, int flag);

    /**更新告警的状态和确认时间*/
	void updateAlarm(Map<String, Object> condition);
}
