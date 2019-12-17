package com.interest.ids.common.project.mapper.analysis;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.interest.ids.common.project.bean.analysis.AnalysisAlarm;
import com.interest.ids.common.project.utils.CommonMapper;

public interface AnalysisAlarmMapper extends CommonMapper<AnalysisAlarm> {

	void updateAlarm(Map<String, Object> condition);
	
	/**
	 * 恢复监控智能告警的方法
	 * @param stationId 电站id
	 * @param alarmId 告警模型
	 */
	void updateRecoverAlarm(@Param("stationId") String stationId, @Param("alarmId") byte alarmId, @Param("recoveredTime") Long recoveredTime);
}