package com.interest.ids.common.project.bean.alarm;

import java.io.Serializable;
import java.math.BigDecimal;

import com.interest.ids.common.project.constant.DevTypeConstant;

public class AlarmToDefectVO implements Serializable
{
	private static final long serialVersionUID = 1L;

	/**告警id*/
	private String alarmIds;
	
	/**电站编码*/
	private String stationCode;
	
	/**电站名称*/
	private String stationName;
	
	/**设备id*/
	private Long devId;
	
	/**设备的名称*/
	private String devAlias;
	
	/**设备的类型*/
	private String devType;
	
	/**设备的型号*/
	private String devVersion;
	
	/**发现告警的时间*/
	private String firstHappenTime;
	
	/**缺陷描述*/
	private String description;
	
	/**设备类型id*/
	private byte devTypeId;
	
	/**告警的类型-普通告警1和智能告警2*/
	private String type;
	
	private String signalVersion;
	
	/**设备的esn号*/
	private String snCode;
	
	//替代alarmtype
	private Integer teleType;
	private Long alarmId;
	private String alarmName;
    private String alarmNames;
    private Integer levelId;
    private Long recoverTime;
    private Integer statusId;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String alarmCause;
    private String repairSuggestion;
    private String stationAddr;

	public String getDevType() {
		return devType;
	}

	public void setDevType(String devType) {
		this.devType = devType;
	}

	public String getStationAddr() {
		return stationAddr;
	}

	public void setStationAddr(String stationAddr) {
		this.stationAddr = stationAddr;
	}

	public Long getAlarmId() {
		return alarmId;
	}

	public void setAlarmId(Long alarmId) {
		this.alarmId = alarmId;
	}

	public String getAlarmName() {
		return alarmName;
	}

	public void setAlarmName(String alarmName) {
		this.alarmName = alarmName;
	}

	public String getAlarmIds() {
		return alarmIds;
	}

	public void setAlarmIds(String alarmIds) {
		this.alarmIds = alarmIds;
	}

	public String getAlarmNames() {
		return alarmNames;
	}

	public void setAlarmNames(String alarmNames) {
		this.alarmNames = alarmNames;
	}

	public String getSnCode() {
		return snCode;
	}

	public void setSnCode(String snCode) {
		this.snCode = snCode;
	}

	public Integer getTeleType() {
		return teleType;
	}

	public void setTeleType(Integer teleType) {
		this.teleType = teleType;
	}

	public Integer getLevelId() {
		return levelId;
	}

	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}

	public Long getRecoverTime() {
		return recoverTime;
	}

	public void setRecoverTime(Long recoverTime) {
		this.recoverTime = recoverTime;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public String getAlarmCause() {
		return alarmCause;
	}

	public void setAlarmCause(String alarmCause) {
		this.alarmCause = alarmCause;
	}

	public String getRepairSuggestion() {
		return repairSuggestion;
	}

	public void setRepairSuggestion(String repairSuggestion) {
		this.repairSuggestion = repairSuggestion;
	}

	public String getSignalVersion() {
		return signalVersion;
	}

	public void setSignalVersion(String signalVersion) {
		this.signalVersion = signalVersion;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public byte getDevTypeId() {
		return devTypeId;
	}

	public void setDevTypeId(byte devTypeId) {
		this.devTypeId = devTypeId;
		this.devType = DevTypeConstant.DEV_TYPE_I18N_ID.get(new Integer(this.getDevTypeId()));
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public Long getDevId() {
		return devId;
	}

	public void setDevId(Long devId) {
		this.devId = devId;
	}

	public String getDevAlias() {
		return devAlias;
	}

	public void setDevAlias(String devAlias) {
		this.devAlias = devAlias;
	}

	public String getDevVersion() {
		return devVersion;
	}

	public void setDevVersion(String devVersion) {
		this.devVersion = devVersion;
	}

	public String getFirstHappenTime() {
		return firstHappenTime;
	}

	public void setFirstHappenTime(String firstHappenTime) {
		this.firstHappenTime = firstHappenTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
