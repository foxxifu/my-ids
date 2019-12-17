package com.interest.ids.biz.web.alarm.controller.params;

import com.interest.ids.common.project.bean.Pagination;

/**
 * @Author: sunbjx
 * @Description: 告警查询参数
 * @Date Created in 16:06 2018/1/15
 * @Modified By:
 */
public class AlarmParams extends Pagination {

    // 电站名称
    private String stationName;
    // 告警名称
    private String alarmName;
    // 告警级别
    private Integer alarmLevel;
    // 设备名称
    private String devAlias;
    // 设备id
    private Long devId;
    private Long id;

    /**
     * 电站编号
     */
    private String stationCode;
    
    private int alarmState;

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

    public Integer getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(Integer alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public String getDevAlias() {
        return devAlias;
    }

    public void setDevAlias(String devAlias) {
        this.devAlias = devAlias;
    }

    public Long getDevId() {
        return devId;
    }

    public void setDevId(Long devId) {
        this.devId = devId;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

	public int getAlarmState() {
		return alarmState;
	}

	public void setAlarmState(int alarmState) {
		this.alarmState = alarmState;
	}

}
