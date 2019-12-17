package com.interest.ids.dev.alarm.analysis.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.interest.ids.common.project.bean.analysis.AnalysisAlarmModel;
import com.interest.ids.dev.alarm.service.impl.AnalysisAlarmJob;


/**
 * 
 * @author lhq
 *
 *
 */
public class AnalysisAlarmModelCache {
	
	private static ConcurrentMap<Byte,AnalysisAlarmModel> cache = new ConcurrentHashMap<Byte, AnalysisAlarmModel>();
	
	
	
	
	public static AnalysisAlarmModel getModel(Byte alarmId){
		AnalysisAlarmModel model = cache.get(alarmId);
		if(model == null){
			model = AnalysisAlarmJob.getDataService().getAnalysisAlarmModel(alarmId);
			if(model != null){
				put(alarmId,model);
			}
		}
		return model;
	}
	
	public static void put(Byte alarmId,AnalysisAlarmModel model){
		cache.put(alarmId, model);
	}

}
