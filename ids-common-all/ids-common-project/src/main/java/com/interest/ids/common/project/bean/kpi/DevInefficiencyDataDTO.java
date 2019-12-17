package com.interest.ids.common.project.bean.kpi;

/**
 * Description : 设备低效分析数据
 */
public class DevInefficiencyDataDTO {

    public Long deviceId;

    public String stationCode;

    public Double dayCap;
    // 位置ID
    public String locId;
    // 等效利用小时数（PPR）
    private Double perpowerRatio;

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public Double getDayCap() {
        return dayCap;
    }

    public void setDayCap(Double dayCap) {
        this.dayCap = dayCap;
    }

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public Double getPerpowerRatio() {
        return perpowerRatio;
    }

    public void setPerpowerRatio(Double perpowerRatio) {
        this.perpowerRatio = perpowerRatio;
    }

}
