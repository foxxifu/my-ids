package com.interest.ids.dev.alarm.service.impl;

import java.util.List;

import javax.annotation.Resource;

import tk.mybatis.mapper.entity.Example;

import com.interest.ids.common.project.bean.alarm.AlarmConstant.AlarmStatus;
import com.interest.ids.common.project.bean.analysis.AnalysisAlarm;
import com.interest.ids.common.project.bean.analysis.StationAnalysisTime;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.mapper.analysis.AnalysisAlarmMapper;
import com.interest.ids.common.project.mapper.analysis.StationAnalysisTimeMapper;
import com.interest.ids.commoninterface.dao.station.StationShareemiMapper;

/**
 * 
 * @author lhq
 *
 *
 */
public class AlarmAnalysisServiceImpl {
	
	@Resource
	private StationAnalysisTimeMapper timeMapper;
	
	private AnalysisAlarmMapper analyAlarmMapper;
	
	private StationShareemiMapper emiMapper;
	
	public StationAnalysisTime getAnalyTime(String stationCode){
		
		Example ex = new Example(StationAnalysisTime.class);
		Example.Criteria criteria = ex.createCriteria();
		criteria.andEqualTo("stationCode", stationCode);
		List<StationAnalysisTime> list = timeMapper.selectByExample(ex);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	public List<AnalysisAlarm> getActiveAlarm(String stationCode){
		
		Example ex = new Example(AnalysisAlarm.class);
		Example.Criteria criteria = ex.createCriteria();
		criteria.andEqualTo("stationCode", stationCode);
		criteria.andNotEqualTo("alarmState", AlarmStatus.RECOVERED);
		criteria.andNotEqualTo("alarmState", AlarmStatus.CLEARED);
		List<AnalysisAlarm> list = analyAlarmMapper.selectByExample(ex);
		return list;
	}
	
	public Long getSharedEmi(String stationCode){
		
		List<DeviceInfo> emis = emiMapper.getDeviceInfoByStationCode(stationCode);
		if(emis != null && emis.size() > 0){
			return emis.get(0).getId();
		}
		return null;
	}

}
