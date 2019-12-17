package com.interest.ids.gatekeeper.server.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interest.ids.common.project.bean.analysis.AnalysisAlarm;
import com.interest.ids.common.project.bean.analysis.AnalysisAlarmModel;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.mapper.analysis.AnalysisAlarmMapper;
import com.interest.ids.common.project.mapper.analysis.AnalysisAlarmModelMapper;
import com.interest.ids.commoninterface.dao.device.DevPvCapacityMMapper;
import com.interest.ids.commoninterface.dao.station.StationInfoMMapper;
import com.interest.ids.commoninterface.service.station.StationForDevProjectService;

import tk.mybatis.mapper.entity.Example;

/**
 * 获取电站信息的实现类
 * @author wq
 *
 */
@Service("stationForDevProjectService")
public class StationForDevProjectServiceImpl implements StationForDevProjectService {

	@Autowired
	private StationInfoMMapper stationInfoMMapper;
	
	@Autowired
	private AnalysisAlarmModelMapper analysisAlarmModelMapper;
	
	@Autowired
	private AnalysisAlarmMapper analysisAlarmMapper;
	@Autowired
	private DevPvCapacityMMapper devPvCapacityMMapper;
	
	@Override
	public StationInfoM getStationByCode(String stationCode) {
		return stationInfoMMapper.getStationByCode(stationCode);
	}
	
	@Override
	public List<StationInfoM> getAllMonitorStations() {
		return this.stationInfoMMapper.getAllMonitorStations();
	}

	@Override
	public AnalysisAlarmModel getAnalysisAlarmMode(Byte alarmId) {
		return analysisAlarmModelMapper.selectByPrimaryKey(alarmId);
	}
	
	@Override
	public int getUnRecordAnalysisAlarmCount(String stationCode, Byte alarmId) {
		Example example = new Example(AnalysisAlarm.class);
		example.createCriteria().andEqualTo("stationId", stationCode)
		.andEqualTo("alarmId", alarmId).andIsNull("recoveredTime");
		return analysisAlarmMapper.selectCountByExample(example);
	}
	
	@Override
	public void saveAnalysisAlarm(List<AnalysisAlarm> list) {
		analysisAlarmMapper.insertList(list);
	}

	@Override
	public void recoverAnalysisAlarm(String stationCode, Byte alarmId) {
		analysisAlarmMapper.updateRecoverAlarm(stationCode, alarmId, System.currentTimeMillis());
	}

	@Override
	public int getMonitorPvCapNum(Map<String, Object> params) {
		return devPvCapacityMMapper.getMonitorPvCapNum(params);
	}

	@Override
	public void deleteMonitorPvConfig(Map<String, Object> params) {
		if (this.getMonitorPvCapNum(params) == 0){ // 没有查询到就说明是没有传递或者下面已经删除重新添加了的
			// 1.删除ids_pv_capacity_t
			devPvCapacityMMapper.deleteMonitorCapPvConfig(params);
			// 2. 删除ids_device_pv_module_t
			devPvCapacityMMapper.deleteMonitorDevPvConfig(params);
		}
	}

	@Override
	public void deleteMonitorCenterPvConfig(HashMap<String, Object> map) {
		int len = ((String) map.get("ids")).split(",").length;
		int count = devPvCapacityMMapper.countCenterPvConfig(map);
		if (len != count) { // 如果不相同就要去做删除ids_center_vert_detail_t的操作
			devPvCapacityMMapper.deleteMonitorCenterPvConfig(map);
		}
	}
}
