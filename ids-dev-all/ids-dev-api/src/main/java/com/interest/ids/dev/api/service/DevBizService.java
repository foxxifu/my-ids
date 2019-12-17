package com.interest.ids.dev.api.service;

import java.util.List;

import com.interest.ids.common.project.bean.alarm.AlarmM;
import com.interest.ids.common.project.bean.alarm.AlarmModel;
import com.interest.ids.common.project.bean.alarm.ClearedAlarmM;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.sm.StationInfoM;


/**
 * 
 * @author lhq
 *
 *
 */
public interface DevBizService {
	
	void newAlarm(AlarmM alarm,ClearedAlarmM clearAlarm);
	/**
	 * 获取一条告警的模型
	 * @param alarmId 告警模型中的alarmId字段
	 * @param causeId 原因id
	 * @param stationCode 产生告警的设备的电站编号
	 * @return
	 */
	public AlarmModel getAlarmModel(Integer alarmId,Integer causeId, String stationCode);
	
	public StationInfoM getStationByCode(String code);
	
	public List<AlarmM> getUnRecoveredAlarms(Long deviceId,Long alarmId,Long causeId,Long raiseDate);
	
	public void recoverAlarm(AlarmM ala,AlarmM alarm,ClearedAlarmM clearAlarm);
	
	public void updateAlarm(AlarmM alarm);
	/**
	 * 更新电站
	 * @param station
	 */
	void updateStationInfoMById(StationInfoM station);
	/**
	 * 根据设备id获取设备
	 * @param devId
	 * @return
	 */
	DeviceInfo getDevById(Long devId);
	/**
	 * 更加设备id修改设备
	 * @param dev
	 */
	void updateDevByDevId(DeviceInfo dev);
	/**
	 * 根据设备的id删除监控传给集维的设备
	 * @param devIds
	 */
	void deleteMonitorDevByIds (String devIds);

}
