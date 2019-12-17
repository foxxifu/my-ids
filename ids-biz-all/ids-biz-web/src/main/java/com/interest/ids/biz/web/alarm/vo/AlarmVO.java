package com.interest.ids.biz.web.alarm.vo;

/**
 * @Author: sunbjx
 * @Description:
 * @Date Created in 10:20 2018/1/3
 * @Modified By:
 */
public class AlarmVO {
    // ID
    private Long id;
    // 电站名称
    private String stationName;
    // 告警名称
    private String alarmName;
    // 发生时间
    private Long createDate;
    // 复位时间
    private Long recoverDate;
    // 告警级别
    private Byte alarmLevel;
    // 设备名称
    private String devAlias;
    // 设备类型
    private String DevType;
    // 告警状态
    private Byte alarmState;
    // 告警类型
    private Byte alarmType;
    // 修复建议
    private String repairSuggestion;
    //设备id
    private Long devId;
    //告警定位
    private String alarmLocation;
    
    private String devInterval;
    private String stationCode;
    // 智能告警模型的id
    private Long alarmId;
    
    public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public String getAlarmLocation() {
		return alarmLocation;
	}

	public void setAlarmLocation(String alarmLocation) {
		this.alarmLocation = alarmLocation;
	}

	public Long getDevId() {
		return devId;
	}

	public void setDevId(Long devId) {
		this.devId = devId;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public Long getRecoverDate() {
        return recoverDate;
    }

    public void setRecoverDate(Long recoverDate) {
        this.recoverDate = recoverDate;
    }

    public Byte getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(Byte alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public String getDevAlias() {
        return devAlias;
    }

    public void setDevAlias(String devAlias) {
        this.devAlias = devAlias;
    }

    public String getDevType() {
        return DevType;
    }

    public void setDevType(String devType) {
        DevType = devType;
    }

    public Byte getAlarmState() {
        return alarmState;
    }

    public void setAlarmState(Byte alarmState) {
        this.alarmState = alarmState;
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

	public String getDevInterval() {
		return devInterval;
	}

	public void setDevInterval(String devInterval) {
		this.devInterval = devInterval;
	}

	public Long getAlarmId() {
		return alarmId;
	}

	public void setAlarmId(Long alarmId) {
		this.alarmId = alarmId;
	}
    
    
}
