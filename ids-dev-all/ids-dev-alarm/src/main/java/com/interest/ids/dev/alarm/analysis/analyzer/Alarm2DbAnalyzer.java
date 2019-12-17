package com.interest.ids.dev.alarm.analysis.analyzer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.interest.ids.common.project.bean.alarm.AlarmConstant.AlarmStatus;
import com.interest.ids.common.project.bean.analysis.AnalysisAlarm;
import com.interest.ids.dev.alarm.analysis.AlarmAnalyzer;
import com.interest.ids.dev.alarm.analysis.context.AlarmAnalysisContext;
import com.interest.ids.dev.alarm.service.impl.AnalysisAlarmJob;

/**
 * 
 * @author lhq
 *
 *
 */
public class Alarm2DbAnalyzer implements AlarmAnalyzer{
	

	@Override
	public void analysis(AlarmAnalysisContext context) {
		
		//如果存在告警
		List<AnalysisAlarm> alarms = context.getSaveAlarm();
		if(alarms.size() > 0){
			List<AnalysisAlarm> list = context.getUnrecoveredAlarm();
			if(list == null || list.size() == 0){
				save(alarms);
			}else{
				List<AnalysisAlarm> recoveAlarms = new ArrayList<AnalysisAlarm>();
				for(AnalysisAlarm alarm : list){
					if(!alarms.contains(alarm)){ // 如果没有需要做告警的，就需要恢复这条告警
						alarm.setAlarmState(AlarmStatus.RECOVERED.getTypeId().byteValue()); // 修改状态为恢复
						alarm.setRecoveredTime(System.currentTimeMillis()); // 设置恢复时间为当前时间
						recoveAlarms.add(alarm);
					}
				}
				if(recoveAlarms.size() > 0){
					AnalysisAlarmJob.getDataService().updateAlarms(recoveAlarms);
				}
				Iterator<AnalysisAlarm> it = alarms.iterator();
				while(it.hasNext()){
					AnalysisAlarm alarm = it.next();
					if(list.contains(alarm)){ // 去掉已存在的告警,不能重复添加智能告警
						it.remove();
					}
				}
				save(alarms);
			}
		}
	}
	
	private void save(List<AnalysisAlarm> alarms){
		if(alarms != null && alarms.size()>0){
			
			AnalysisAlarmJob.getDataService().saveAlarms(alarms);
		}
	}
	

}
