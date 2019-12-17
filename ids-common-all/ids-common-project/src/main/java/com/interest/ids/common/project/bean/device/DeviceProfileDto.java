package com.interest.ids.common.project.bean.device;

import java.util.Map;

public class DeviceProfileDto {

    private Integer devTypeId;

    private Integer devCount;

    private Map<Integer, Integer> devStatus;

    public Integer getDevTypeId() {
        return devTypeId;
    }

    public void setDevTypeId(Integer devTypeId) {
        this.devTypeId = devTypeId;
    }

    public Integer getDevCount() {
        return devCount;
    }

    public void setDevCount(Integer devCount) {
        this.devCount = devCount;
    }

    public Map<Integer, Integer> getDevStatus() {
        return devStatus;
    }

    public void setDevStatus(Map<Integer, Integer> devStatus) {
        this.devStatus = devStatus;
    }

}
