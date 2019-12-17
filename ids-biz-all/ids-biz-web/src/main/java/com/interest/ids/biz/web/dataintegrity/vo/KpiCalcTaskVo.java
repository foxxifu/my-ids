package com.interest.ids.biz.web.dataintegrity.vo;

import java.io.Serializable;

public class KpiCalcTaskVo implements Serializable {

    private static final long serialVersionUID = -1456166484862408320L;

    private Long id;

    private String taskName;

    private String stationCode;

    private String stationName;

    private byte taskStatus;

    private Long startTime;

    private Long endTime;

    public KpiCalcTaskVo() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long taskId) {
        this.id = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
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

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public byte getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(byte taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public String toString() {
        return "KpiCalcManualM [taskName=" + taskName + ", stationCode=" + stationCode + ", taskStatus=" + taskStatus
                + ", startTime=" + startTime + ", endTime=" + endTime + "]";
    }
}
