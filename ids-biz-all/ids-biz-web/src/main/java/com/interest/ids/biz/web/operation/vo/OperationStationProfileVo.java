package com.interest.ids.biz.web.operation.vo;

import java.io.Serializable;
import java.util.Map;

public class OperationStationProfileVo implements Serializable {

    private static final long serialVersionUID = 4207872435844963486L;

    // 装机容量
    private Double intalledCapacity;

    // 管理电站数
    private int stationNum;

    // 电站状态数量
    private Map<Integer, Integer> stationStatus;

    public Double getIntalledCapacity() {
        return intalledCapacity;
    }

    public void setIntalledCapacity(Double intalledCapacity) {
        this.intalledCapacity = intalledCapacity;
    }

    public int getStationNum() {
        return stationNum;
    }

    public void setStationNum(int stationNum) {
        this.stationNum = stationNum;
    }

    public Map<Integer, Integer> getStationStatus() {
        return stationStatus;
    }

    public void setStationStatus(Map<Integer, Integer> stationStatus) {
        this.stationStatus = stationStatus;
    }

}
