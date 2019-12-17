package com.interest.ids.common.project.bean.kpi;

/**
 * Description : 设备组串电流电压数据
 */
public class DevVolCurrentDataDTO {

    public Long deviceId;

    public String stationCode;

    // 直流汇流箱电压
    private Double photcU;

    // 接入组串的组串电压和
    private Double totalConnectMaxPvU;
    // 连接的PV组串数
    private int connectPvCount;
    // 电流异常串数（电流小于等于0.3的组串数）
    private int faultPviCount;

    // 逆变器类型： 1组串式逆变器 2集中式逆变器
    private Integer inverterType;

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

    public Double getPhotcU() {
        return photcU;
    }

    public void setPhotcU(Double photcU) {
        this.photcU = photcU;
    }

    public Double getTotalConnectMaxPvU() {
        return totalConnectMaxPvU;
    }

    public void setTotalConnectMaxPvU(Double totalConnectMaxPvU) {
        this.totalConnectMaxPvU = totalConnectMaxPvU;
    }

    public int getConnectPvCount() {
        return connectPvCount;
    }

    public void setConnectPvCount(int connectPvCount) {
        this.connectPvCount = connectPvCount;
    }

    public int getFaultPviCount() {
        return faultPviCount;
    }

    public void setFaultPviCount(int faultPviCount) {
        this.faultPviCount = faultPviCount;
    }

    public Integer getInverterType() {
        return inverterType;
    }

    public void setInverterType(Integer inverterType) {
        this.inverterType = inverterType;
    }
}
