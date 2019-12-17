package com.interest.ids.dev.alarm.analysis.analyzer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.bean.alarm.AlarmConstant.AlarmStatus;
import com.interest.ids.common.project.bean.analysis.AnalysisAlarm;
import com.interest.ids.common.project.bean.analysis.StationAnalysisTime;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.utils.SunTimes;
import com.interest.ids.dev.alarm.analysis.AlarmAnalyzer;
import com.interest.ids.dev.alarm.analysis.context.AlarmAnalysisContext;
import com.interest.ids.dev.alarm.service.impl.AnalysisAlarmJob;
import com.interest.ids.dev.alarm.utils.AlarmConstant;
import com.interest.ids.dev.alarm.utils.AlarmUtils;


/**
 * 
 * @author lhq
 *
 *  箱变分析，我们假设所有电站的并网点不可用
 *  1.获取所有箱变的电压，如果为0则认为电站停机
 *  
 */
public class TransformerBoxAnalyzer implements AlarmAnalyzer{

    private static final Logger log = LoggerFactory.getLogger(TransformerBoxAnalyzer.class);
	
	
	@Override
	public void analysis(AlarmAnalysisContext context) {
		try {
			 analy(context);
		} catch (Exception e) {
			log.error("analysis box error: ",e);
		}
		
//		if(b){
			context.fireAnalysis();
//		}
	}
	
	private boolean analy(AlarmAnalysisContext context){
		
		String stationCode = context.getStationCode();
		
		StationAnalysisTime time = context.getAnalyTime();
		
		//1.查询电站的最大功率
		Double stationPower = AnalysisAlarmJob.getDataService().getInverterSumPower2Range(context);
		
		if(stationPower == null){
			log.error("the station {} sum activepower less zero",context.getStation().getStationName());
			return false;
		}
		//2.获取电站下的所有箱变
		List<DeviceInfo> boxes = context.getTransformerBox();
		//电站功率为0，启动电站停机分析
		if(stationPower <= 0){
			//如果存在箱变
			if(boxes.size() > 0){
				//获取箱变总电压，如果为0，则表示电站停机
				Double sumVoltage = AnalysisAlarmJob.getDataService().getTransferSumVoltage2Range(stationCode, null, time.getStartTime(), time.getEndTime());
				if(sumVoltage != null && sumVoltage <= 0d){
					//电站停机告警
					AnalysisAlarm alarm = AlarmUtils.generateAlarm(stationCode,null,AlarmStatus.ACTIVE.getTypeId().byteValue(),time.getLastCollectTime(),time.getLastCollectTime(),
			        		-1,-1L,AlarmConstant.ALARMID_1);
					context.getSaveAlarm().add(alarm);
					//停止诊断
					return false;
				}	
			}else{
				//无箱变的情况
				Long emiId = AnalysisAlarmJob.getDataService().getSharedEmi(stationCode);
				//环境检测仪可用
				if(emiId != null){
					//查找最小辐照强度
					Double maxRad = AnalysisAlarmJob.getDataService().getMaxValue2Range("ids_emi_data_t", stationCode, emiId, time.getStartTime(), time.getEndTime());
					if(maxRad != null && maxRad.doubleValue() > AlarmConstant.LOW_RADIANT){
						//电站停机告警
						AnalysisAlarm alarm = AlarmUtils.generateAlarm(stationCode,null,AlarmStatus.ACTIVE.getTypeId().byteValue(),time.getLastCollectTime(),time.getLastCollectTime(),
				        		-1,-1L,AlarmConstant.ALARMID_1);
						context.getSaveAlarm().add(alarm);
					}
				}
				//环境检测仪不可用
				else{
					StationInfoM station  = context.getStation();
					//存在经纬度
					if(station.getLongitude() != null && station.getLatitude() != null){
						//当前时间是否在日出日落的1.5小时内
						if(timeInRange(station)){
							//查找所有逆变器中是否存在直流电流小于400V，直流电压大于0A的逆变器，如果不存在，电站停机告警，如果存在停止诊断
							Integer size = AnalysisAlarmJob.getDataService().queryVloAndCurrent(stationCode, time.getStartTime(), time.getEndTime());
							if(size != null && size > 0){
								//电站停机告警
								AnalysisAlarm alarm = AlarmUtils.generateAlarm(stationCode,null,AlarmStatus.ACTIVE.getTypeId().byteValue(),time.getLastCollectTime(),time.getLastCollectTime(),
						        		-1,-1L,AlarmConstant.ALARMID_1);
								context.getSaveAlarm().add(alarm);
							}
						}
					}
				}
			}
			return false;
		}else{
			//启动箱变分析,无箱变，直接进行逆变器分析
			context.setStationPower(stationPower);
			if(boxes == null || boxes.size() == 0){
				
				return true;
			}else{
				//分析箱变
				List<Long> list = new ArrayList<Long>();
				List<AnalysisAlarm> alarms = new ArrayList<AnalysisAlarm>();
				//箱变电压,这个地方的查询后面改成使用group by的方式
				for(DeviceInfo dev : context.getTransformerBox()){
					Double data = AnalysisAlarmJob.getDataService().getTransferSumVoltage2Range(stationCode, dev.getId(), time.getStartTime(), time.getEndTime());
					if(data == null || data <= 0d){
		                // 箱变告警
		                log.info("this transformer is shutdown, transId: {}", dev.getId());
		                AnalysisAlarm alarm = AlarmUtils.generateAlarm(context.getStationCode(),dev.getDevName(),AlarmStatus.ACTIVE.getTypeId().byteValue(),
		                			context.getAnalyTime().getLastCollectTime(),context.getAnalyTime().getLastCollectTime(),
		                			DevTypeConstant.BOX_DEV_TYPE,dev.getId(),AlarmConstant.ALARMID_2);
		               
		                // 箱变告警时其下挂设备无需分析
		                list.add(dev.getId());
		                alarms.add(alarm);
		            }
				}
				//如果全部箱变都停机，那么电站停机告警
				if(list.size() == context.getTransformerBox().size()){
					
					AnalysisAlarm alarm = AlarmUtils.generateAlarm(stationCode,null,AlarmStatus.ACTIVE.getTypeId().byteValue(),time.getLastCollectTime(),time.getLastCollectTime(),
			        		-1,-1L,AlarmConstant.ALARMID_1);
			         //入库保存电站告警
					context.getSaveAlarm().add(alarm);
					return false;
				}
				if(list.size() > 0){
					context.setFaultBox(list);
				}
			}
		}
		return true;
	}
	//判断当前时间是否在日出日落时间段的一段时间
	private boolean timeInRange(StationInfoM station){
		Double longitude = station.getLongitude();
		Double latitude = station.getLongitude();
		Integer timeZone = station.getTimeZone();
		if(timeZone == null){
			timeZone = AlarmConstant.DEFAULT_TIMEZONE;
		}
		Calendar stationLocalTime = AlarmUtils.getLocalCalendarByTimeZone(timeZone, null);
		int hour = stationLocalTime.get(Calendar.HOUR_OF_DAY);
		if(latitude != null && longitude != null && timeZone != null){
			List<int[]> list = SunTimes.getSunTime(SunTimes.getDayRange(), longitude, latitude, timeZone);
			
			
			Calendar time1 = Calendar.getInstance();
			Calendar time2 = Calendar.getInstance();
			int[] sunRise = list.get(0);
			int[] sunSet = list.get(1);
			time1.set(Calendar.HOUR_OF_DAY, sunRise[0]);
			time1.set(Calendar.MINUTE, sunRise[1]);
			time1.add(Calendar.MINUTE, 90);
			
			time2.set(Calendar.HOUR_OF_DAY, sunSet[0]);
			time2.set(Calendar.MINUTE, sunSet[1]);
			time2.add(Calendar.MINUTE, 90);
			
			//只比较小时好了
			if(stationLocalTime.getTimeInMillis() > time1.getTimeInMillis() && stationLocalTime.getTimeInMillis() < time2.getTimeInMillis()){
				return true;
			}
		}
		else{
			//是否在日出日落之间
			if(hour > AlarmConstant.DEFAUL_SUNRISE_TIME && hour < AlarmConstant.DEFAULT_SUNSET_TIME){
				return true;
			}
		}
		//如果在日出日落1.5小时之内
		
		return false;
	}

}
