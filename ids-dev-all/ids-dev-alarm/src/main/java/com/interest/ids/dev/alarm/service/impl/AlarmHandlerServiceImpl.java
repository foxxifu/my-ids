package com.interest.ids.dev.alarm.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.interest.ids.common.project.bean.TupleParam;
import com.interest.ids.common.project.bean.alarm.AlarmConstant.AlarmACT;
import com.interest.ids.common.project.bean.alarm.AlarmConstant.AlarmStatus;
import com.interest.ids.common.project.bean.alarm.AlarmDto;
import com.interest.ids.common.project.bean.alarm.AlarmM;
import com.interest.ids.common.project.bean.alarm.AlarmModel;
import com.interest.ids.common.project.bean.alarm.ClearedAlarmM;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.signal.SignalInfo;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.enums.SignalTypeEnum;
import com.interest.ids.dev.alarm.pool.AlarmPool;
import com.interest.ids.dev.alarm.utils.AlarmUtils;
import com.interest.ids.dev.alarm.utils.LocalTimeZone;
import com.interest.ids.dev.api.bean.Iec104DataBean;
import com.interest.ids.dev.api.localcache.AlarmModelCache;
import com.interest.ids.dev.api.localcache.DeviceCache;
import com.interest.ids.dev.api.service.AlarmRemotingService;
import com.interest.ids.dev.api.service.DevBizService;


/**
 * 
 * @author lhq
 * 加入分类模型
 *
 */
@Service("alarmService")
public class AlarmHandlerServiceImpl implements AlarmRemotingService{
	
	private static final Logger log = LoggerFactory.getLogger(AlarmHandlerServiceImpl.class);
	
	@Resource
	private DevBizService bizService;

	@Override
	public void newAlarm(final AlarmDto alarmEntity) {
		
		Runnable task = new Runnable() {
			
			@Override
			public void run() {
				
				try {
					
					handle(alarmEntity);
				} catch (Exception e) {
					log.error("handle alarm error",e);
				}
			}
		};
		AlarmPool.getPool().execute(task);
	}
	
	
	
	 
	 
	 private void handle(AlarmDto alarmEntity){
		 
		 AlarmModel model = bizService.getAlarmModel(alarmEntity.getAlarmId().intValue(), alarmEntity.getCauseId().intValue(), alarmEntity.getStationCode());
		 if(model == null){
			 log.error("alarm model is null");
			 return;
		 }
		 AlarmM alarm = convent(model,alarmEntity);
		 AlarmStatus status = alarmEntity.getStatus();
		 if(status == AlarmStatus.ACTIVE){
			 onActive(alarm);
		 }
		 if(status == AlarmStatus.RECOVERED){
			 onRecovered(alarm);
		 }
	 }
	 
	 private void onRecovered(AlarmM alarm) {
		 List<AlarmM> list = bizService.getUnRecoveredAlarms(alarm.getDevId(), alarm.getAlarmId(), alarm.getCauseId(), alarm.getRecoverTime());
		 if(list != null && list.size() > 0){
			
			 for(int i=0;i<list.size();i++){
				 AlarmM ala = list.get(i);
				 
				 ClearedAlarmM clearAlarm = new ClearedAlarmM();
				 clearAlarm.setStatusId(AlarmStatus.RECOVERED.getTypeId());
				 clearAlarm.setRecoverTime(alarm.getLastHappenTime());
				 bizService.recoverAlarm(ala,alarm,clearAlarm);
			}
		 }
	}


	private void onActive(AlarmM alarm) {
		
		List<AlarmM> list = bizService.getUnRecoveredAlarms(alarm.getDevId(), alarm.getAlarmId(), alarm.getCauseId(), alarm.getLastHappenTime());
		//log.info("get alarm list is:"+list.size());
		if(list != null && list.size() > 0){
			for(int i=0;i<list.size();i++){
				AlarmM dbAlarm = list.get(i);
				if(dbAlarm.getLastHappenTime()!= null && dbAlarm.getLastHappenTime() < alarm.getLastHappenTime()){
					AlarmM bean = new AlarmM();
					bean.setId(dbAlarm.getId());
					bean.setLastHappenTime(alarm.getLastHappenTime());
					bizService.updateAlarm(bean);
				}
			}
		}
		else{
			//推送到前端，存库
			alarm.setStatusId(AlarmStatus.ACTIVE.getTypeId());
			alarm.setActId(AlarmACT.ACT.getTypeId());
			alarm.setCreateTime(System.currentTimeMillis());
			alarm.setFirstHappenTime(alarm.getLastHappenTime());
			
			ClearedAlarmM clearAlarm = new ClearedAlarmM();
			BeanUtils.copyProperties(alarm, clearAlarm);
			
			bizService.newAlarm(alarm,clearAlarm);
		}
	}
	


	private AlarmM convent(AlarmModel model,AlarmDto entity){
		AlarmM alarm = new AlarmM();
		DeviceInfo dev = entity.getDev();
		alarm.setDevTypeId(dev.getDevTypeId());
        alarm.setDevId(dev.getId());
        alarm.setAlarmId(entity.getAlarmId());
        alarm.setCauseId(entity.getCauseId());
        alarm.setSequenceNum(entity.getSeqNum());
        alarm.setAlarmName(model.getAlarmName());
        alarm.setLevelId((int)model.getSeverityId());
        // 设置告警类型
        alarm.setAlarmType(model.getAlarmType().intValue());
        //alarm.setTeleTypeId(model.getTeleType());
        if(AlarmStatus.ACTIVE == entity.getStatus()){
            alarm.setStatusId(AlarmStatus.ACTIVE.getTypeId());
            alarm.setLastHappenTime(entity.getOccurDate());
        }else if(AlarmStatus.RECOVERED == entity.getStatus()){
            alarm.setStatusId(AlarmStatus.RECOVERED.getTypeId());
            alarm.setRecoverTime(entity.getOccurDate());
        }
        if(model.getTeleType() != null){
        	alarm.setTeleTypeId(model.getTeleType().shortValue());
        }
        if(dev.getStationCode() != null){
        	StationInfoM station = bizService.getStationByCode(dev.getStationCode());
        	if(station != null){
        		alarm.setStationCode(station.getStationCode());
    			alarm.setStationName(station.getStationName());
    			alarm.setStationHappenTime(AlarmUtils.getTimeByTimeZone(entity.getOccurDate(),station.getTimeZone()==null?8:station.getTimeZone()));
        	}            
        }
        alarm.setDevName(dev.getDevName());
        alarm.setDevName(dev.getDevName());
        alarm.setLastHappenTime(entity.getOccurDate());
    
        alarm.setSignalVersion(dev.getSignalVersion());
		return alarm;
	}


	@Override
	public void newTeleSignal(final DeviceInfo dev,final Iec104DataBean dataBean) {
		
		
		Runnable task = new Runnable() {
			
			@Override
			public void run() {
				
				try {
					onTeleSignal(dev,dataBean);
				} catch (Exception e) {
					log.error("on tele Signal error",e);
				}
			}
		};
		AlarmPool.getPool().execute(task);
	}
	
	private void onTeleSignal(DeviceInfo dev,Iec104DataBean dataBean){
		List<TupleParam<SignalInfo, Object, Long>> values = dataBean.getValues();
		for(int i=0 ; i<values.size() ; i++){
			TupleParam<SignalInfo, Object, Long> param = values.get(i);
			SignalInfo info = param.getKey();
			if(info.getSignalType() != null &&info.getSignalType().intValue() == SignalTypeEnum.GJ.getValue()){
				//转换成告警的情况下
				AlarmModel model = AlarmModelCache.getAlarmModel(dev.getStationCode(), info.getModelId());
				if(model != null){
					
					int value = Integer.valueOf(param.getValue().toString());
					//遥信产生
					if(value == 1){
						AlarmM alarm = createAlarm(info,param.getT(),model);
						onActive(alarm);
					}
					//遥信复归
					else if(value == 0){
						AlarmM alarm = createAlarm(info,param.getT(),model);
						onRecovered(alarm);
					}
				}
			}
		}
	}
	
	private AlarmM createAlarm(SignalInfo info,Long time,AlarmModel model){
		AlarmM alarm = new AlarmM();
	    
	    Long devId = info.getDeviceId();
	    alarm.setDevId(devId);
	    alarm.setAlarmType(model.getAlarmType().intValue());
	    if(time == 0 || time == null){
	    	time = System.currentTimeMillis();
	    }
	    DeviceInfo dev = DeviceCache.getDeviceById(devId);
	    alarm.setLastHappenTime(time);
	    if(dev != null){
	    	alarm.setDevName(dev.getDevAlias());
	    	alarm.setDevTypeId(dev.getDevTypeId());
	    	alarm.setSignalVersion(info.getSignalVersion());
	    }
	    if(model != null){
	    	alarm.setAlarmId(model.getAlarmId().longValue());
	    	alarm.setAlarmName(model.getAlarmName());
	    	if(model.getSeverityId() != null){
	    		
	    		alarm.setLevelId(model.getSeverityId().intValue());
	    	}
	    	 if(model.getTeleType() != null){
	         	alarm.setTeleTypeId(model.getTeleType().shortValue());
	         }
	    }
	    if(dev.getStationCode() != null){
        	StationInfoM station = bizService.getStationByCode(dev.getStationCode());
        	if(station != null){
        		alarm.setStationCode(station.getStationCode());
    			alarm.setStationName(station.getStationName());
    			int localTimezone = LocalTimeZone.getInstance().getLocalOPTimeZone();
    			int stationTimeZone = station.getTimeZone()==null ? localTimezone : station.getTimeZone();
    			if(stationTimeZone != localTimezone){
    				alarm.setStationHappenTime(AlarmUtils.getTimeByTimeZone(time,station.getTimeZone()));
    			}else{
    				alarm.setStationHappenTime(time);
    			}
        	}            
        }
	    
		return alarm;
	}
	
	public static void main(String[] args) {
		
	}
}
