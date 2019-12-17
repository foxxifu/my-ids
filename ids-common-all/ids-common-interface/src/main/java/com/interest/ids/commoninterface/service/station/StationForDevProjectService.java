package com.interest.ids.commoninterface.service.station;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.interest.ids.common.project.bean.analysis.AnalysisAlarm;
import com.interest.ids.common.project.bean.analysis.AnalysisAlarmModel;
import com.interest.ids.common.project.bean.sm.StationInfoM;

/**
 * 对于dev模块的电站的service
 * @author wq
 *
 */
public interface StationForDevProjectService {
	/**
	 * 根据电站编号获取电站信息
	 * @param stationCode
	 * @return
	 */
	StationInfoM getStationByCode(String stationCode);
	/**
	 * 获取所有的监控传递上来的电站
	 * @return
	 */
	List<StationInfoM> getAllMonitorStations();
	/**
	 * 根据告警模型的主键查询告警
	 * @param alarmId 告警的主键
	 * @return
	 */
	AnalysisAlarmModel getAnalysisAlarmMode(Byte alarmId);
	/**
	 * 获取未恢复的监控断连告警
	 * @param stationCode 电站编号
	 * @param alarmId 告警模型id
	 * @return
	 */
	int getUnRecordAnalysisAlarmCount(String stationCode, Byte alarmId);
	/**
	 * 保存一条监控与集维断连的智能告警
	 * @param alarm
	 */
	void saveAnalysisAlarm(List<AnalysisAlarm> list);
	/**
	 * 恢复监控断连的智能告警
	 * @param stationCode
	 * @param alarmId
	 */
	void recoverAnalysisAlarm(String stationCode, Byte alarmId);
	
	/**
	 * 查询监控传递上来的数据的pv配置的数量
	 * @param params
	 * @return 0:没有，就需要删除对应的设备和电站对应的pv配置； 1：不需要做任何配置
	 */
	int getMonitorPvCapNum(Map<String, Object> params);
	/**
	 * 删除监控传递过来的pv配置信息
	 * @param params
	 */
	void deleteMonitorPvConfig(Map<String, Object> params);
	/**
	 * 删除监控传递的ids_center_vert_detail_t的数据
	 * @param map
	 */
	void deleteMonitorCenterPvConfig(HashMap<String, Object> map);
	
}
