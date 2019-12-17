package com.interest.ids.biz.web.operation.vo;

import java.io.Serializable;

public class OperationTaskVo implements Serializable {

    private static final long serialVersionUID = 9179198895018527983L;

    // 任务编号
    private Long taskId;

    // 任务描述
    private String taskDesc;

    // 任务状态
    private String taskStatus;

    // 当前处理人
    private String currDealUser;

    // 电站名
    private String stationName;

    private String stationCode;

    private Long userId;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getCurrDealUser() {
        return currDealUser;
    }

    public void setCurrDealUser(String currDealUser) {
        this.currDealUser = currDealUser;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
