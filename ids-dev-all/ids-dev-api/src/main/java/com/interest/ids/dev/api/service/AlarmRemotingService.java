package com.interest.ids.dev.api.service;

import com.interest.ids.common.project.bean.alarm.AlarmDto;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.dev.api.bean.Iec104DataBean;


/**
 * 
 * @author lhq
 *
 *
 */
public interface AlarmRemotingService {
	
	void newAlarm(AlarmDto alarmEntity);
	
	void newTeleSignal(DeviceInfo dev,Iec104DataBean dataBean);

}
