package com.interest.ids.dev.alarm.analysis.analyzer;

import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.dev.alarm.analysis.AlarmAnalyzer;
import com.interest.ids.dev.alarm.analysis.context.AlarmAnalysisContext;
import com.interest.ids.dev.alarm.service.impl.AlarmAnalysisServiceImpl;
import com.interest.ids.dev.alarm.service.impl.AnalysisDataServiceImpl;


/**
 * 
 * @author lhq
 * 电站功率分析
 *
 */
public class StationPowerAnalyzer implements AlarmAnalyzer{

	
	private AnalysisDataServiceImpl devDataService;
	
	private AlarmAnalysisServiceImpl alarmAnalyService;
	
	private AlarmAnalyzer analyzer;
	@Override
	public void analysis(AlarmAnalysisContext context) {
		
		boolean b = isPowerSatisfy(context);
		alarmAnalyService.getActiveAlarm(context.getStationCode());
		if(!b){
			//发电功率没有达到功率阈值时，对电站停机和箱变停机告警做恢复处理，其它告警不做处理
			
			return;
		}
		analyzer.analysis(context);
	}
	
	private boolean isPowerSatisfy(AlarmAnalysisContext context) {
				
		Double totalPower = devDataService.getInverterSumPower2Range(context);
		
		StationInfoM station = context.getStation();
		Double capacity = station.getInstalledCapacity();
		String paramValue = devDataService.getParam(context.getStationCode(), "powerThresh");
		double powerThresh = 0.0;
		if(paramValue != null){
			powerThresh = Double.parseDouble(paramValue);
		}
		double lowestPower = capacity * powerThresh * 0.01;
		if(totalPower > 0 && totalPower > lowestPower){
	         return true;
	    }
		return false;
	}

}
