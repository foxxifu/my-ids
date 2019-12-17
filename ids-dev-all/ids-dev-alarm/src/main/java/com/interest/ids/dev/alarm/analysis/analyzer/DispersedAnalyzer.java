package com.interest.ids.dev.alarm.analysis.analyzer;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.bean.alarm.AlarmConstant.AlarmStatus;
import com.interest.ids.common.project.bean.analysis.AnalysisAlarm;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.dev.alarm.analysis.AlarmAnalyzer;
import com.interest.ids.dev.alarm.analysis.bean.DispersionRatioBean;
import com.interest.ids.dev.alarm.analysis.context.AlarmAnalysisContext;
import com.interest.ids.dev.alarm.service.impl.AnalysisAlarmJob;
import com.interest.ids.dev.alarm.utils.AlarmConstant;
import com.interest.ids.dev.alarm.utils.AlarmUtils;
import com.interest.ids.dev.api.localcache.DeviceCache;

/**
 * 
 * @author lhq
 * 组串故障低效分析
 * 
 */
public class DispersedAnalyzer implements AlarmAnalyzer{

    private static final Logger log = LoggerFactory.getLogger(DispersedAnalyzer.class);
	
	
	@Override
	public void analysis(AlarmAnalysisContext context) {
		
		Map<Long, DispersionRatioBean> disperMap = AnalysisAlarmJob.getDataService().queryInverterDepersionMap(context.getStationCode(),
				context.getAnalyTime().getStartTime(),context.getAnalyTime().getEndTime(),context.getPowerThresh(),context.getMap());
		for(Map.Entry<Long, DispersionRatioBean> entry : disperMap.entrySet()){
			Long devId = entry.getKey();
			DispersionRatioBean bean = entry.getValue();
			//离散率大于10%
			if(bean.getDispersion() > AlarmConstant.DISPERSION_THRESHOLD){
				//pvx支路断开
				Map<Integer, Double> maxPVX = bean.getMaxPVxIMap();
				for(Map.Entry<Integer, Double> tmp : maxPVX.entrySet()){
					Double maxPVI = tmp.getValue();
					Integer pv = tmp.getKey();
					//XX组串电流小于等于0.3
					if(maxPVI <= AlarmConstant.CURRENT_THRESHOLD){
						DeviceInfo dev = DeviceCache.getDeviceById(devId); // 根据设备id获取数据
						//PVX支路断开告警
						AnalysisAlarm alarm = AlarmUtils.generateAlarm(context.getStationCode(), 
								dev.getDevAlias(), AlarmStatus.ACTIVE.getTypeId().byteValue(), 
								System.currentTimeMillis(), System.currentTimeMillis(), dev.getDevTypeId(), devId, 
								// 判断是组串还是直流汇流箱
								DevTypeConstant.INVERTER_DEV_TYPE.equals(dev.getDevTypeId())? AlarmConstant.ALARMID_16 : AlarmConstant.ALARMID_17);
						alarm.setAlarmPvNum(pv);
						alarm.setAlarmName(alarm.getAlarmName() + "#" + pv); // 在生成的告警位置设置告警的pv信息
						if (!context.getSaveAlarm().contains(alarm)) {
                            context.getSaveAlarm().add(alarm);
                        }
					}else{
						//判断pvx功率偏低告警
						String branchPowerThreshParam = AnalysisAlarmJob.getDataService().getParam(context.getStationCode(), "PVPOWERTHRESH");
			            Double branchPowerThresh = branchPowerThreshParam == null ? 0.0
			                    : Double.parseDouble(branchPowerThreshParam) * 0.01;
			            Double avgPV = bean.getAvgEfficiencyCap();
			            Double power = bean.getSumPVxPowerMap().get(pv);
			            if(avgPV != null && power != null){
			            	// double dValue = power - avgPV;
			            	// double abValue = Math.abs(dValue);
			            	double ration = power / avgPV;
			            	if(ration < branchPowerThresh){
			                    log.info("pvx power lower {}",pv);
			                    DeviceInfo dev = DeviceCache.getDeviceById(devId); // 根据设备id获取数据
			                    if (dev == null) {
			                    	log.error("no find dev info : devID = {}", devId);
			                    	continue;
			                    }
			                    //产生告警
			                    AnalysisAlarm alarm = AlarmUtils.generateAlarm(context.getStationCode(), 
			                    		dev.getDevAlias(), AlarmStatus.ACTIVE.getTypeId().byteValue(), 
										System.currentTimeMillis(), System.currentTimeMillis(), dev.getDevTypeId(), devId, 
										DevTypeConstant.INVERTER_DEV_TYPE.equals(dev.getDevTypeId())? AlarmConstant.ALARMALARMID_18 : AlarmConstant.ALARMID_19);
								alarm.setAlarmPvNum(pv);
								alarm.setAlarmName(alarm.getAlarmName() + "#" + pv); // 在生成的告警位置设置告警的pv信息
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
