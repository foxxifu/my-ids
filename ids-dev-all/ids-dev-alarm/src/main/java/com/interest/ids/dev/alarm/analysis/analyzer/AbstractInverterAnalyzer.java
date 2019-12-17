package com.interest.ids.dev.alarm.analysis.analyzer;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.bean.alarm.AlarmConstant.AlarmStatus;
import com.interest.ids.common.project.bean.analysis.AnalysisAlarm;
import com.interest.ids.common.project.bean.analysis.StationAnalysisTime;
import com.interest.ids.common.project.bean.kpi.InverterConc;
import com.interest.ids.common.project.bean.kpi.InverterString;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.dev.alarm.analysis.AlarmAnalyzer;
import com.interest.ids.dev.alarm.analysis.context.AlarmAnalysisContext;
import com.interest.ids.dev.alarm.service.impl.AnalysisAlarmJob;
import com.interest.ids.dev.alarm.utils.AlarmConstant;
import com.interest.ids.dev.alarm.utils.AlarmUtils;


/**
 */
public abstract class AbstractInverterAnalyzer implements AlarmAnalyzer{
	
    private static final Logger log = LoggerFactory.getLogger(AbstractInverterAnalyzer.class);

    	
	public void analysis(AlarmAnalysisContext context){
		
		StationAnalysisTime time = context.getAnalyTime();
		StationInfoM station = context.getStation();
		double thresh = station.getInstalledCapacity() * 0.1;
		//如果电站功率未达标，不再分析
		if(thresh > context.getStationPower()){
			log.error("the station power not statis");
			return;
		}
		
		
		
		List<DeviceInfo> devs = AnalysisAlarmJob.getDataService().queryDeviceByStationAndType(context.getStationCode(),getDevType(),context.getFaultBox());
		
		if(devs != null && devs.size() > 0){
			if(getDevType().equals(DevTypeConstant.CENTER_INVERT_DEV_TYPE)){
				List<InverterConc> concInverters = AnalysisAlarmJob.getDataService().queryConInverter(context.getStationCode(),time.getStartTime(),time.getEndTime());
				if(concInverters!= null && concInverters.size() > 0){
					for(DeviceInfo dev : devs){
						for(InverterConc inverter : concInverters){
							
							if(dev.getId().equals(inverter.getDeviceId())){

								boolean b = isVoltageGraterZero(inverter);
								if(b){
									BigDecimal dayCap = inverter.getDayCapacity();
									if(dayCap == null || dayCap.compareTo(BigDecimal.ZERO) == 0){
										//逆变器停机告警
										AnalysisAlarm alarm = AlarmUtils.generateAlarm(context.getStationCode(), inverter.getDeviceName(), AlarmStatus.ACTIVE.getTypeId().byteValue(), 
												inverter.getCollectTime(), System.currentTimeMillis(), getDevType(), inverter.getDeviceId(), 
												AlarmConstant.ALARMID_20);
										if (!context.getSaveAlarm().contains(alarm)) {
		                                    context.getSaveAlarm().add(alarm);
		                                }
									}
								}
							}
						}
					}
				
				}
			}
			if(getDevType().equals(DevTypeConstant.INVERTER_DEV_TYPE)){
				List<InverterString> inverters = AnalysisAlarmJob.getDataService().queryInverter(context.getStationCode(),time.getStartTime(),time.getEndTime());
				if(inverters!= null && inverters.size() > 0){
					for(DeviceInfo dev : devs){
						for(InverterString inverter : inverters){
							
							if(dev.getId().equals(inverter.getDevId())){

								boolean b = isVoltageGraterZero(inverter);
								if(b){
									BigDecimal dayCap = inverter.getDayCapacity();
									if(dayCap == null || dayCap.compareTo(BigDecimal.ZERO) <= 0){
										//逆变器停机告警
										AnalysisAlarm alarm = AlarmUtils.generateAlarm(context.getStationCode(), inverter.getDevName(), AlarmStatus.ACTIVE.getTypeId().byteValue(), 
												inverter.getCollectTime(), System.currentTimeMillis(), getDevType(), inverter.getDevId(), 
												AlarmConstant.ALARMID_21);
										if (!context.getSaveAlarm().contains(alarm)) {
		                                    context.getSaveAlarm().add(alarm);
		                                }
									}
								}
							}
						}
					}
				}
			}
			
			
		}
	}
	
	//abstract void innerAnalysis(AlarmAnalysisContext context);
	
	private boolean isVoltageGraterZero(InverterString inverter) {
		
		/*if(isGreaterZero(inverter.getAbU()) || isGreaterZero(inverter.getBcU()) || isGreaterZero(inverter.getCaU())){
			return true;
		}*/
	    if(isGreaterZero(inverter.getaU()) || isGreaterZero(inverter.getbU()) || isGreaterZero(inverter.getcU())){
            return true;
        }
	    
		return false;
	}
	
	private boolean isVoltageGraterZero(InverterConc inverter) {
		
		if(isGreaterZero(inverter.getaU()) || isGreaterZero(inverter.getbU()) || isGreaterZero(inverter.getcU())){
			return true;
		}
		return false;
	}
	
	
	private boolean isGreaterZero(BigDecimal data){
		
		if(data != null && BigDecimal.ZERO.compareTo(data) <= 0){
			return true;
		}
		return false;
	}
	
	abstract Integer getDevType();

}
