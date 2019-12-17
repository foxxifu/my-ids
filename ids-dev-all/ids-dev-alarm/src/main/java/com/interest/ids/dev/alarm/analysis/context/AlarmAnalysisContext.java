package com.interest.ids.dev.alarm.analysis.context;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.interest.ids.common.project.bean.analysis.AnalysisAlarm;
import com.interest.ids.common.project.bean.analysis.StationAnalysisTime;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.dev.alarm.analysis.AlarmAnalyzer;
import com.interest.ids.dev.alarm.analysis.bean.DeviceAnalysisBean;


/**
 * 
 * @author lhq
 *
 *
 */
public class AlarmAnalysisContext {
	
	public AlarmAnalysisContext(StationInfoM station,StationAnalysisTime time){
		this.stationCode = station.getStationCode();
		this.station = station;
		this.analyTime = time;
	}
	
	private LinkedList<AlarmAnalyzer> list = new LinkedList<AlarmAnalyzer>();
	
	//电站编号
	private String stationCode;
	
	private StationInfoM station;
	
	private StationAnalysisTime analyTime;
	//电站实时功率
	private double stationPower;
	
	private double powerThresh;
	//箱变
	private List<DeviceInfo> transformerBox = new ArrayList<DeviceInfo>();
	//故障箱变
	private List<Long> faultBox;
	//组串式逆变器
	private List<DeviceInfo> stringInverter = new ArrayList<DeviceInfo>();
	//汇流箱
	private List<DeviceInfo> combiners = new ArrayList<DeviceInfo>();
	//集中式逆变器
	private List<DeviceInfo> centInverter = new ArrayList<DeviceInfo>();
	//未恢复告警，在后面做恢复处理
	private List<AnalysisAlarm> unrecoveredAlarm = new ArrayList<AnalysisAlarm>();
	
	private List<AnalysisAlarm> saveAlarm = new ArrayList<AnalysisAlarm>();
	
	private List<AnalysisAlarm> unRecoveredAlarm = new ArrayList<>();
	
	private List<Long> noNeddAnalyDevs = new ArrayList<Long>(); 
	//产生告警的设备id群
	private List<Long> alarmDevIds;
	
	private Map<Long,List<Long>> transBox2Device;
	//所有设备的组串和容量
	private Map<Long,DeviceAnalysisBean> map;
	
	public void fireAnalysis(){
		AlarmAnalyzer analyzer = list.pollFirst();
		if(analyzer != null){
			analyzer.analysis(this);
		}
	}
	
	public AlarmAnalysisContext addLast(AlarmAnalyzer analyzer){
		list.add(analyzer);
		return this;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public StationAnalysisTime getAnalyTime() {
		return analyTime;
	}

	public void setAnalyTime(StationAnalysisTime analyTime) {
		this.analyTime = analyTime;
	}

	public StationInfoM getStation() {
		return station;
	}

	public void setStation(StationInfoM station) {
		this.station = station;
	}

	public double getPowerThresh() {
		return powerThresh;
	}

	public void setPowerThresh(double powerThresh) {
		this.powerThresh = powerThresh;
	}

	public List<DeviceInfo> getTransformerBox() {
		return transformerBox;
	}

	public void setTransformerBox(List<DeviceInfo> transformerBox) {
		this.transformerBox = transformerBox;
	}

	public List<Long> getFaultBox() {
		return faultBox;
	}

	public void setFaultBox(List<Long> faultBox) {
		this.faultBox = faultBox;
	}
	
	public List<AnalysisAlarm> getUnrecoveredAlarm() {
		return unrecoveredAlarm;
	}

	public void setUnrecoveredAlarm(List<AnalysisAlarm> unrecoveredAlarm) {
		this.unrecoveredAlarm = unrecoveredAlarm;
	}

	public List<Long> getAlarmDevIds() {
		return alarmDevIds;
	}

	public void setAlarmDevIds(List<Long> alarmDevIds) {
		this.alarmDevIds = alarmDevIds;
	}

	public List<AnalysisAlarm> getSaveAlarm() {
		return saveAlarm;
	}

	public void setSaveAlarm(List<AnalysisAlarm> saveAlarm) {
	    saveAlarm = saveAlarm == null ? new ArrayList<AnalysisAlarm>() : saveAlarm;
		this.saveAlarm = saveAlarm;
	}
	
	public List<AnalysisAlarm> getUnRecoveredAlarm() {
        return unRecoveredAlarm;
    }

    public void setUnRecoveredAlarm(List<AnalysisAlarm> unRecoveredAlarm) {
        this.unRecoveredAlarm = unRecoveredAlarm;
    }

    public List<Long> getNoNeddAnalyDevs() {
		return noNeddAnalyDevs;
	}

	public void setNoNeddAnalyDevs(List<Long> noNeddAnalyDevs) {
		this.noNeddAnalyDevs = noNeddAnalyDevs;
	}

	public Map<Long, List<Long>> getTransBox2Device() {
		return transBox2Device;
	}

	public void setTransBox2Device(Map<Long, List<Long>> transBox2Device) {
		this.transBox2Device = transBox2Device;
	}

	public Map<Long, DeviceAnalysisBean> getMap() {
		return map;
	}

	public void setMap(Map<Long, DeviceAnalysisBean> map) {
		this.map = map;
	}

	public List<DeviceInfo> getStringInverter() {
		return stringInverter;
	}

	public void setStringInverter(List<DeviceInfo> stringInverter) {
		this.stringInverter = stringInverter;
	}

	public List<DeviceInfo> getCombiners() {
		return combiners;
	}

	public void setCombiners(List<DeviceInfo> combiners) {
		this.combiners = combiners;
	}

	public List<DeviceInfo> getCentInverter() {
		return centInverter;
	}

	public void setCentInverter(List<DeviceInfo> centInverter) {
		this.centInverter = centInverter;
	}

	public double getStationPower() {
		return stationPower;
	}

	public void setStationPower(double stationPower) {
		this.stationPower = stationPower;
	}
}
