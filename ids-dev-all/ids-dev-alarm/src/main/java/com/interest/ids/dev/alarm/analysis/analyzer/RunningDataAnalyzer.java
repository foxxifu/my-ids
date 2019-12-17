package com.interest.ids.dev.alarm.analysis.analyzer;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.bean.analysis.StationAnalysisTime;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.dev.alarm.analysis.AlarmAnalyzer;
import com.interest.ids.dev.alarm.analysis.bean.DeviceAnalysisBean;
import com.interest.ids.dev.alarm.analysis.context.AlarmAnalysisContext;
import com.interest.ids.dev.alarm.service.impl.AnalysisAlarmJob;
import com.interest.ids.dev.alarm.utils.AlarmUtils;

/**
 * 
 * @author lhq
 *
 * 实时数据分析，分析应该向下取整数
 * 
 * 1、是否有采集实时数据
 * 2、电站箱变判断
 * 3、逆变器判断
 */
public class RunningDataAnalyzer implements AlarmAnalyzer{
	
	private static final Logger log = LoggerFactory.getLogger(RunningDataAnalyzer.class);

	
	
	@Override
	public void analysis(AlarmAnalysisContext context) {
		Map<Long, DeviceAnalysisBean> map = AnalysisAlarmJob.getDataService().getDeviceAnalysisMap(context.getStationCode());
		context.setMap(map);
		if(!init(context)){
			log.error("station {} has no dev ",context.getStation().getStationName());
			return;
		}
		//如果不是在日出日落时间段，直接返回,则可以进行其他数据分析
		if(!AlarmUtils.timeInRange(context.getStation(), null)){
			log.error("station time not in sunraise and set {}",context.getStation().getStationName());
			return;
		}
		
		//时间判断
		String stationCode = context.getStationCode();
		StationAnalysisTime time = context.getAnalyTime();
		
		//判断在这段时间的采集数据是否为空
		Long last2DbTime = AnalysisAlarmJob.getDataService().getMaxCollectTime2Range("ids_inverter_string_data_t", stationCode, time.getStartTime(), time.getEndTime());
		if(last2DbTime == null){
			//查找集中式逆变器
			last2DbTime = AnalysisAlarmJob.getDataService().getMaxCollectTime2Range("ids_inverter_conc_data_t", stationCode, time.getStartTime(), time.getEndTime());
		}
		//继续向下分析
		if(last2DbTime != null){
			time.setLastCollectTime(last2DbTime);
			context.fireAnalysis();
		}else{
			log.error("station has no collect data {}",context.getStation().getStationName());
		}
	}
	
	private boolean init(AlarmAnalysisContext context){
		List<DeviceInfo> devs = AnalysisAlarmJob.getDataService().getDevs(context.getStationCode());
		if(devs != null){
			for(DeviceInfo dev : devs){
				Integer type = dev.getDevTypeId();
				if(type == DevTypeConstant.INVERTER_DEV_TYPE){
					context.getStringInverter().add(dev);
					continue;
				}
				if(type == DevTypeConstant.BOX_DEV_TYPE){
					context.getTransformerBox().add(dev);
					continue;
				}
				if(type == DevTypeConstant.CENTER_INVERT_DEV_TYPE){
					context.getCentInverter().add(dev);
					continue;
				}
				if(type == DevTypeConstant.DCJS_DEV_TYPE){
					context.getCombiners().add(dev);
				}
			}
			return true;
		}
		return false;
	}
}
