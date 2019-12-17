package com.interest.ids.biz.web.report.vo;

import java.io.Serializable;

/**
 * 逆变器运行日、月、年报表抽象
 * 
 * @author zl
 * @date 2018-1-22
 */
public class InverterReportVO implements Serializable {

    private static final long serialVersionUID = 403245470883242892L;

    /**
     * 采集时间
     */
    private Long collectTime;

    private String stationName;

    /**
     * 设备编号
     */
    private Long deviceId;

    /**
     * 设备名称
     */
    private String devName;

    /**
     * 设备类型
     */
    private Integer inverterType;

    /**
     * 逆变器发电量
     */
    private Double inverterPower;

    /**
     * 逆变器转换效率
     */
    private Double efficiency;

    /**
     * 峰值功率
     */
    private Double peakPower;

    /**
     * 通信可靠度
     */
    private Double aocRatio;

    /**
     * 生产可靠度
     */
    private Double aopRatio;

    /**
     * 生产偏差
     */
    private Double powerDeviation;

    private Double installedCapacity;

    public Long getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Long collectTime) {
        this.collectTime = collectTime;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public Integer getInverterType() {
        return inverterType;
    }

    public void setInverterType(Integer inverterType) {
        this.inverterType = inverterType;
    }

    public Double getInverterPower() {
        return inverterPower;
    }

    public void setInverterPower(Double inverterPower) {
        this.inverterPower = inverterPower;
    }

    public Double getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(Double efficiency) {
        this.efficiency = efficiency;
    }

    public Double getPeakPower() {
        return peakPower;
    }

    public void setPeakPower(Double peakPower) {
        this.peakPower = peakPower;
    }

    public Double getAocRatio() {
        return aocRatio;
    }

    public void setAocRatio(Double aocRatio) {
        this.aocRatio = aocRatio;
    }

    public Double getAopRatio() {
        return aopRatio;
    }

    public void setAopRatio(Double aopRatio) {
        this.aopRatio = aopRatio;
    }

    public Double getPowerDeviation() {
        return powerDeviation;
    }

    public void setPowerDeviation(Double powerDeviation) {
        this.powerDeviation = powerDeviation;
    }

    public Double getInstalledCapacity() {
        return installedCapacity;
    }

    public void setInstalledCapacity(Double installedCapacity) {
        this.installedCapacity = installedCapacity;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((collectTime == null) ? 0 : collectTime.hashCode());
        result = prime * result + ((deviceId == null) ? 0 : deviceId.hashCode());
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
        InverterReportVO other = (InverterReportVO) obj;
        if (collectTime == null) {
            if (other.collectTime != null)
                return false;
        } else if (!collectTime.equals(other.collectTime))
            return false;
        if (deviceId == null) {
            if (other.deviceId != null)
                return false;
        } else if (!deviceId.equals(other.deviceId))
            return false;
        return true;
    }

}
