package com.interest.ids.dev.alarm.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.interest.ids.common.project.bean.alarm.AlarmConstant.AlarmACT;
import com.interest.ids.common.project.bean.alarm.AlarmM;
import com.interest.ids.common.project.bean.alarm.AlarmModel;
import com.interest.ids.common.project.bean.alarm.ClearedAlarmM;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.mapper.signal.AlarmMapper;
import com.interest.ids.common.project.mapper.signal.AlarmModelMapper;
import com.interest.ids.common.project.mapper.signal.ClearedAlarmMapper;
import com.interest.ids.common.project.mapper.signal.DevInfoMapper;
import com.interest.ids.commoninterface.dao.station.StationInfoMMapper;
import com.interest.ids.dev.api.service.DevBizService;
import com.interest.ids.redis.caches.StationCache;

import tk.mybatis.mapper.entity.Example;


@Service("devBizService")
public class DevBizServiceImpl implements DevBizService{
	
	@Resource
	private AlarmModelMapper mapper;
	
	@Resource
	private StationInfoMMapper stationMapper;
	
	@Resource
	private  AlarmMapper alarmMapper;
	
	@Resource
	private ClearedAlarmMapper clearAlarmMapper;
	
	@Autowired
	private DevInfoMapper devInfoMapper;
	
	
	
	
	public AlarmModel getAlarmModel(Integer alarmId,Integer causeId, String stationCode){
		Example ex = new Example(AlarmModel.class);
		Example.Criteria criteria = ex.createCriteria();
		criteria.andEqualTo("alarmId", alarmId);
		criteria.andEqualTo("causeId", causeId);
		criteria.andEqualTo("stationCode", stationCode);
		List<AlarmModel> alarmModels = this.mapper.selectByExample(ex);
		if(alarmModels != null && alarmModels.size() > 0){
			return alarmModels.get(0);
		}
		return null;
	}
	
	
	public StationInfoM getStationByCode(String code){
		
		StationInfoM station = this.stationMapper.selectStationInfoMNameByStationCode(code);
		
		return station;
		
	}
	
	
	public List<AlarmM> getUnRecoveredAlarms(Long deviceId,Long alarmId,Long causeId,Long raiseDate){
		
		Example ex = new Example(AlarmM.class);
		Example.Criteria criteria = ex.createCriteria();
		criteria.andEqualTo("devId", deviceId);
		criteria.andEqualTo("alarmId", alarmId);
		if(causeId != null){
			criteria.andEqualTo("causeId", causeId);
		}
		// 恢复时间是空
		criteria.andIsNull("recoverTime");
		//criteria.andLessThanOrEqualTo("lastHappenTime", raiseDate);
		criteria.andEqualTo("actId", AlarmACT.ACT.getTypeId());
		List<AlarmM> alarms = this.alarmMapper.selectByExample(ex);
		return alarms;
	}
	
	
	public void updateAlarm(AlarmM alarm){
		
		alarmMapper.updateByPrimaryKeySelective(alarm);
		//alarmMapper.updateByPrimaryKey(alarm);
	}
	
	public void saveAlarm(AlarmM alarm){
		alarmMapper.insertSelective(alarm);
	}
	
	@Transactional
	public void newAlarm(AlarmM alarm,ClearedAlarmM clearAlarm){
		saveAlarm(alarm);
		
		saveClearedAlarm(clearAlarm);
	}
	
	public void saveClearedAlarm(ClearedAlarmM alarm){
		
		clearAlarmMapper.insertSelective(alarm);
	}
	
	public void updateClearedAlarm(ClearedAlarmM clearAlarm,Long deviceId,Long alarmId,Long causeId,Long raiseDate){
		
		Example ex = new Example(ClearedAlarmM.class);
		Example.Criteria criteria = ex.createCriteria();
		criteria.andEqualTo("devId", deviceId);
		criteria.andEqualTo("alarmId", alarmId);
		if(causeId != null){
			criteria.andEqualTo("causeId", causeId);
		}
		// 没有恢复时间的,即恢复时间是空
		criteria.andIsNull("recoverTime");
		criteria.andLessThanOrEqualTo("lastHappenTime", raiseDate);
		criteria.andEqualTo("actId", AlarmACT.ACT.getTypeId());
		
		clearAlarmMapper.updateByExampleSelective(clearAlarm, ex);
	}
	
	public void deleteAlarm(AlarmM alarm){
		alarmMapper.deleteByPrimaryKey(alarm);
	}
	
	@Transactional
	public void recoverAlarm(AlarmM ala,AlarmM alarm,ClearedAlarmM clearAlarm){
		
		 deleteAlarm(ala);
		 updateClearedAlarm(clearAlarm,alarm.getDevId(),alarm.getAlarmId(),alarm.getCauseId(),alarm.getLastHappenTime());
	}


	@Override
	public void updateStationInfoMById(StationInfoM station) {
		if (station == null || station.getId() == null) {
			return;
		}
		this.stationMapper.updateStationInfoMById(station);
		// 更新缓存
		List<StationInfoM> sl = new ArrayList<StationInfoM>();
		sl.add(station);
		StationCache.updateStationInfo(sl);
	}


	@Override
	public DeviceInfo getDevById(Long devId) {
		if (devId == null) {
			return null;
		}
		return devInfoMapper.selectByPrimaryKey(devId);
	}


	@Override
	public void updateDevByDevId(DeviceInfo dev) {
		if (dev.getId() == null) {
			return;
		}
		devInfoMapper.updateByPrimaryKey(dev);
	}


	@Override
	public void deleteMonitorDevByIds(String devIds) {
		if (StringUtils.isEmpty(devIds)) {
			return;
		}
		List<String> devIdList = Arrays.asList(devIds.split(","));
		Example ex = new Example(DeviceInfo.class);
		Example.Criteria criteria = ex.createCriteria();
		criteria.andEqualTo("isMonitorDev", "1");
		criteria.andIn("id", devIdList);
		devInfoMapper.deleteByExample(ex);
	}

}
