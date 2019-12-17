package com.interest.ids.dev.alarm.analysis.analyzer;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.bean.alarm.AlarmConstant.AlarmStatus;
import com.interest.ids.common.project.bean.analysis.AnalysisAlarm;
import com.interest.ids.common.project.bean.analysis.StationAnalysisTime;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.dev.alarm.analysis.AlarmAnalyzer;
import com.interest.ids.dev.alarm.analysis.bean.DevVolCurrentDataBean;
import com.interest.ids.dev.alarm.analysis.bean.DeviceAnalysisBean;
import com.interest.ids.dev.alarm.analysis.context.AlarmAnalysisContext;
import com.interest.ids.dev.alarm.service.impl.AnalysisAlarmJob;
import com.interest.ids.dev.alarm.utils.AlarmConstant;
import com.interest.ids.dev.alarm.utils.AlarmUtils;


/**
 * 
 * @author lhq
 * 直流汇流箱分析一小时一次
 *
 */
public class CombinerBoxAnalyzer implements AlarmAnalyzer{
	
    private static final Logger log = LoggerFactory.getLogger(CombinerBoxAnalyzer.class);
    

	@Override
	public void analysis(AlarmAnalysisContext context) {
	    Map<Long, DeviceAnalysisBean> map = null;

		if(context.getCombiners().size() == 0){
		    map = AnalysisAlarmJob.getDataService().getDeviceAnalysisMap(context.getStationCode());
	        context.setMap(map);
			context.fireAnalysis();
			return;
		}
		long times = AnalysisTimes.getInstance().getTimes();
		if(times % 2 != 0){
			return;
		}
		StationAnalysisTime time = context.getAnalyTime();
		Double cap = context.getStation().getInstalledCapacity();
		double thresh = cap * 0.3;
		if(context.getStationPower() < thresh){
			log.error("combinerBox power not staisfy {}",thresh);
			return;
		}

		map = AnalysisAlarmJob.getDataService().getDeviceAnalysisMap(context.getStationCode());
		context.setMap(map);
		Map<Long, DevVolCurrentDataBean> datas = AnalysisAlarmJob.getDataService().queryCombinerDcInfo(context.getStationCode(), time.getStartTime(), time.getEndTime(),map);
		if(datas != null){
			for(Map.Entry<Long, DevVolCurrentDataBean> entry : datas.entrySet()){
				DevVolCurrentDataBean bean = entry.getValue();
				if(bean.getConnectPvCount() == bean.getFaultPviCount()){
					//直流汇流箱异常
					DeviceAnalysisBean analysis = map.get(bean.getDevId());
					AnalysisAlarm alarm = AlarmUtils.generateAlarm(context.getStationCode(), analysis.getDevName(), AlarmStatus.ACTIVE.getTypeId().byteValue(),
							System.currentTimeMillis(), System.currentTimeMillis(), 
							DevTypeConstant.DCJS_DEV_TYPE, bean.getDevId(), AlarmConstant.ALARMID_14);
					context.getSaveAlarm().add(alarm);
				}
			}
		}
		context.fireAnalysis();
	}

}
