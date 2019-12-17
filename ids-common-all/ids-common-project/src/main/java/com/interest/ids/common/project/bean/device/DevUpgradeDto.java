package com.interest.ids.common.project.bean.device;

public class DevUpgradeDto {
	private Long devId;
	private String stationName;
	private String devName;
	private int devTypeId;
	private String signalVersion;
	private int upgradeProcess;
	private String devTypeName;
	
	public Long getDevId() {
		return devId;
	}
	public void setDevId(Long devId) {
		this.devId = devId;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getDevName() {
		return devName;
	}
	public void setDevName(String devName) {
		this.devName = devName;
	}
	public int getDevTypeId() {
		return devTypeId;
	}
	public void setDevTypeId(int devTypeId) {
		this.devTypeId = devTypeId;
	}
	public String getSignalVersion() {
		return signalVersion;
	}
	public void setSignalVersion(String signalVersion) {
		this.signalVersion = signalVersion;
	}
	public int getUpgradeProcess() {
		return upgradeProcess;
	}
	public void setUpgradeProcess(int upgradeProcess) {
		this.upgradeProcess = upgradeProcess;
	}
	public String getDevTypeName() {
		return devTypeName;
	}
	public void setDevTypeName(String devTypeName) {
		this.devTypeName = devTypeName;
	}
	
	
}
