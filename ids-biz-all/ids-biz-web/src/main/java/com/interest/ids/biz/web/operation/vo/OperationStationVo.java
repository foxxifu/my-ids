package com.interest.ids.biz.web.operation.vo;

import java.io.Serializable;
import java.util.Map;

/**
 * 运维工作台初始界面返回结果封装对象
 * 
 * @author zl
 * 
 */
public class OperationStationVo implements Serializable {

    private static final long serialVersionUID = -6256514895115008251L;

    /**
     * 电站编号
     */
    private String stationCode;

    /**
     * 电站名称
     */
    private String stationName;

    /**
     * 并网类型
     */
    private Integer onlineType;

    /**
     * 装机容量
     */
    private Double installedCapacity;

    /**
     * 电站详细地址
     */
    private String stationAddr;

    /**
     * 电站健康状态（ 1：故障 2：通讯中断 3：正常）
     */
    private int stationStatus;

    /**
     * 各设备类型健康状态 Key：设备类型ID Value： 某类设备状态（1：通讯中断 2：故障 3：正常）
     */
    private Map<Integer, Integer> deviceStatus;

    /**
     * 电站实时功率
     */
    private double activePower;

    /**
     * 当日发电量
     */
    private double dayCapacity;

    /**
     * 当日活动告警数
     */
    private int totalAlarm;

    /**
     * 当日活动任务数
     */
    private int totalTask;

    /**
     * 当日未完成任务
     */
    private int undoTask;

    public OperationStationVo() {

    }

    public OperationStationVo(String stationCode, String stationName, String stationFileId) {
        this.stationCode = stationCode;
        this.stationName = stationName;
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

    public Integer getOnlineType() {
        return onlineType;
    }

    public void setOnlineType(Integer onlineType) {
        this.onlineType = onlineType;
    }

    public Double getInstalledCapacity() {
        return installedCapacity;
    }

    public void setInstalledCapacity(Double installedCapacity) {
        this.installedCapacity = installedCapacity;
    }

    public String getStationAddr() {
        return stationAddr;
    }

    public void setStationAddr(String stationAddr) {
        this.stationAddr = stationAddr;
    }

    public int getStationStatus() {
        return stationStatus;
    }

    public void setStationStatus(int stationStatus) {
        this.stationStatus = stationStatus;
    }

    public Map<Integer, Integer> getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(Map<Integer, Integer> deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public double getActivePower() {
        return activePower;
    }

    public void setActivePower(double activePower) {
        this.activePower = activePower;
    }

    public double getDayCapacity() {
        return dayCapacity;
    }

    public void setDayCapacity(double dayCapacity) {
        this.dayCapacity = dayCapacity;
    }

    public int getTotalAlarm() {
        return totalAlarm;
    }

    public void setTotalAlarm(int totalAlarm) {
        this.totalAlarm = totalAlarm;
    }

    public int getTotalTask() {
        return totalTask;
    }

    public void setTotalTask(int totalTask) {
        this.totalTask = totalTask;
    }

    public int getUndoTask() {
        return undoTask;
    }

    public void setUndoTask(int undoTask) {
        this.undoTask = undoTask;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((stationCode == null) ? 0 : stationCode.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OperationStationVo other = (OperationStationVo) obj;
        if (stationCode == null) {
            if (other.stationCode != null)
                return false;
        } else if (!stationCode.equals(other.stationCode))
            return false;
        return true;
    }

}
