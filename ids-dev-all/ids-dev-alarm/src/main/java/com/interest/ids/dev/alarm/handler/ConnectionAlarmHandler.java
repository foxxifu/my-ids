package com.interest.ids.dev.alarm.handler;

import com.interest.ids.common.project.bean.alarm.AlarmConstant.AlarmStatus;
import com.interest.ids.common.project.bean.alarm.AlarmDto;
import com.interest.ids.common.project.bean.alarm.AlarmM;
import com.interest.ids.common.project.bean.signal.DeviceInfo;

/**
 * 
 * @author lhq
 * 连接状态的告警
 *
 */
public class ConnectionAlarmHandler {
	
	public void handle(final AlarmDto alarmEntity){
		
	}
	
	private AlarmM convent(AlarmDto entity){
		AlarmM alarm = new AlarmM();
		DeviceInfo dev = entity.getDev();
		alarm.setDevTypeId(dev.getDevTypeId());
        alarm.setDevId(dev.getId());
        alarm.setAlarmId(65535L);
        alarm.setCauseId(1L);
        alarm.setSequenceNum(entity.getSeqNum());
        if(AlarmStatus.ACTIVE == entity.getStatus()){
            alarm.setStatusId(AlarmStatus.ACTIVE.getTypeId());
            alarm.setLastHappenTime(entity.getOccurDate());
        }else if(AlarmStatus.RECOVERED == entity.getStatus()){
            alarm.setStatusId(AlarmStatus.RECOVERED.getTypeId());
            alarm.setRecoverTime(entity.getOccurDate());
        }
        if(dev.getStationCode() != null){
            alarm.setStationCode(dev.getStationCode());
        }
        alarm.setDevName(dev.getDevName());
        alarm.setLastHappenTime(entity.getOccurDate());
        
        //获取电站
        /*StationInfoM station = bizService.getStationByCode(dev.getStationCode());
        if(station != null){
        	alarm.setStationName(station.getStationName());
        }*/
        alarm.setSignalVersion(dev.getSignalVersion());
		
		return alarm;
	}

}
