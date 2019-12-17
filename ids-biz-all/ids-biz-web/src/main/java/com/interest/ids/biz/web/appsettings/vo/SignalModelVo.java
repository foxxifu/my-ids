package com.interest.ids.biz.web.appsettings.vo;

import com.interest.ids.common.project.constant.DevTypeConstant;

public class SignalModelVo {

	private String venderName;

	private Short devTypeId;

	private String devTypeName;

	private String signalVersion;

	private String signalName;

	private Long modelId;

	private String stationCode;

	private String alarmType;

	private String levelId;

	/**
	 * 1：遥测；2：单点遥信；3：双点遥信；4：单点遥控；5：双点遥控,6：遥脉；7：遥调；8：事件；9：告警
	 */
	private Integer signalType;

	private Long signalVersionId;

	// 当前页
	private int index = 1;
	// 页大小
	private int pageSize = 10;

	public String getVenderName() {
		return venderName;
	}

	public void setVenderName(String venderName) {
		this.venderName = venderName;
	}

	public Short getDevTypeId() {
		return devTypeId;
	}

	public void setDevTypeId(Short devTypeId) {
		this.devTypeId = devTypeId;
		this.devTypeName = DevTypeConstant.DEV_TYPE_I18N_ID.get(devTypeId);
	}

	public String getDevTypeName() {
		return devTypeName;
	}

	public void setDevTypeName(String devTypeName) {
		this.devTypeName = devTypeName;
	}

	public String getSignalVersion() {
		return signalVersion;
	}

	public void setSignalVersion(String signalVersion) {
		this.signalVersion = signalVersion;
	}

	public String getSignalName() {
		return signalName;
	}

	public void setSignalName(String signalName) {
		this.signalName = signalName;
	}

	public Long getModelId() {
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}

	public Integer getSignalType() {
		return signalType;
	}

	public void setSignalType(Integer signalType) {
		this.signalType = signalType;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public Long getSignalVersionId() {
		return signalVersionId;
	}

	public void setSignalVersionId(Long signalVersionId) {
		this.signalVersionId = signalVersionId;
	}

	public String getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}

	public String getLevelId() {
		return levelId;
	}

	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}

}