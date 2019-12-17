package com.interest.ids.biz.web.operation.vo;

import java.io.Serializable;

public class OperationAlarmVo implements Serializable {

    private static final long serialVersionUID = -4850216569889108269L;

    // 告警编号
    private Long alarmId;

    // 告警级别
    private Integer alarmLevel;

    // 告警名称
    private String alarmName;

    // 告警原因
    private String alarmCause;

    // 告警类型（1：设备告警 2：智能分析告警）
    private Byte alarmType;

    // 告警状态
    private Integer alarmState;

    // 设备纬度
    private Double devLatitude;

    // 设备经度
    private Double devLongitude;

    // 设备安装地址
    private String devAddr;

    // 设备名称
    private String devName;

    // 设备序列号
    private String snCode;

    // 第一次发生时间
    private Long firstHappenTime;

    // 恢复时间
    private Long recoverTime;

    // 修复建议
    private String repairSuggestion;

    private String stationCode;

    private String stationName;
    
    private String devInterval;
    // 智能告警的告警模型id
    private String alarmModelId;

    public Long getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(Long alarmId) {
        this.alarmId = alarmId;
    }

    public Integer getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(Integer alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public String getAlarmCause() {
        return alarmCause;
    }

    public void setAlarmCause(String alarmCause) {
        this.alarmCause = alarmCause;
    }

    public Byte getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(Byte alarmType) {
        this.alarmType = alarmType;
    }

    public String getRepairSuggestion() {
        return repairSuggestion;
    }

    public void setRepairSuggestion(String repairSuggestion) {
        this.repairSuggestion = repairSuggestion;
    }

    public Integer getAlarmState() {
        return alarmState;
    }

    public void setAlarmState(Integer alarmState) {
        this.alarmState = alarmState;
    }

    public Double getDevLatitude() {
        return devLatitude;
    }

    public void setDevLatitude(Double devLatitude) {
        this.devLatitude = devLatitude;
    }

    public Double getDevLongitude() {
        return devLongitude;
    }

    public void setDevLongitude(Double devLongitude) {
        this.devLongitude = devLongitude;
    }

    public String getDevAddr() {
        return devAddr;
    }

    public void setDevAddr(String deviceAddr) {
        this.devAddr = deviceAddr;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String deviceName) {
        this.devName = deviceName;
    }

    public String getSnCode() {
        return snCode;
    }

    public void setSnCode(String snCode) {
        this.snCode = snCode;
    }

    public Long getFirstHappenTime() {
        return firstHappenTime;
    }

    public void setFirstHappenTime(Long firstHappenTime) {
        this.firstHappenTime = firstHappenTime;
    }

    public Long getRecoverTime() {
        return recoverTime;
    }

    public void setRecoverTime(Long recoverTime) {
        this.recoverTime = recoverTime;
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

	public String getDevInterval() {
		return devInterval;
	}

	public void setDevInterval(String devInterval) {
		this.devInterval = devInterval;
	}

	public String getAlarmModelId() {
		return alarmModelId;
	}

	public void setAlarmModelId(String alarmModelId) {
		this.alarmModelId = alarmModelId;
	}
	
	

}
