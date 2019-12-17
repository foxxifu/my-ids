package com.interest.ids.biz.web.appsettings.vo;

/**
 * @Author: sunbjx
 * @Description: 告警设置 VO
 * @Date Created in 18:32 2018/1/2
 * @Modified By:
 */
public class AlarmConfigVO {
    // ID
    private Long id;
    // 设备类型
    private String devTypeName;
    // 版本号
    private String versionCode;
    // 告警名称
    private String alarmName;
    // 告警类型名称
    private Byte alarmType;
    // 告警级别
    private Byte alarmLevel;
    // 告警原因
    private String alarmCause;
    // 原因码
    private Integer causeId;
    // 修复建议
    private String repairSuggestion;
    
    private String stationName;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDevTypeName() {
        return devTypeName;
    }

    public void setDevTypeName(String devTypeName) {
        this.devTypeName = devTypeName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public Byte getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(Byte alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public String getAlarmCause() {
        return alarmCause;
    }

    public void setAlarmCause(String alarmCause) {
        this.alarmCause = alarmCause;
    }

    public Integer getCauseId() {
        return causeId;
    }

    public void setCauseId(Integer causeId) {
        this.causeId = causeId;
    }

    public String getRepairSuggestion() {
        return repairSuggestion;
    }

    public void setRepairSuggestion(String repairSuggestion) {
        this.repairSuggestion = repairSuggestion;
    }

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public Byte getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(Byte alarmType) {
		this.alarmType = alarmType;
	}
    
    
}
