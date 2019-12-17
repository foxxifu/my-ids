package com.interest.ids.common.project.bean.device;

import java.util.List;

public class DeviceDetail {
    private Long id;// 设备id
    private String devAlias;// 设备名称
    private String signalVersion;
    private String venderName; // 厂家ids_signal_version_info_t表中
    private Integer deviceTypeId;// 设备类型id
    private String deviceType; // 设备类型名称-非数据库字段
    private String devIp;// 设备ip
    private String snCode; // esn
    private Integer devStatus;// 设备状态------
    private String devAddr;// 设备地址(电站名称+设备的名称- 后面可能需要修改)
    private Double longitude;// 经度
    private Double latitude;// 纬度
    private String stationCode;// 电站编号
    private String stationName;
    private Integer pvNum;// 配置的组串个数
    private List<DevicePvModuleDto> pvInfo;

    private Double installedCapacity;

    private List<DeviceDetail> childDevices;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDevAlias() {
        return devAlias;
    }

    public void setDevAlias(String devAlias) {
        this.devAlias = devAlias;
    }

    public String getSignalVersion() {
        return signalVersion;
    }

    public void setSignalVersion(String signalVersion) {
        this.signalVersion = signalVersion;
    }

    public String getVenderName() {
        return venderName;
    }

    public void setVenderName(String venderName) {
        this.venderName = venderName;
    }

    public Integer getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(Integer deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDevIp() {
        return devIp;
    }

    public void setDevIp(String devIp) {
        this.devIp = devIp;
    }

    public String getSnCode() {
        return snCode;
    }

    public void setSnCode(String snCode) {
        this.snCode = snCode;
    }

    public Integer getDevStatus() {
        return devStatus;
    }

    public void setDevStatus(Integer devStatus) {
        this.devStatus = devStatus;
    }

    public String getDevAddr() {
        return devAddr;
    }

    public void setDevAddr(String devAddr) {
        this.devAddr = devAddr;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
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

    public Integer getPvNum() {
        return pvNum;
    }

    public void setPvNum(Integer pvNum) {
        this.pvNum = pvNum;
    }

    public List<DevicePvModuleDto> getPvInfo() {
        return pvInfo;
    }

    public void setPvInfo(List<DevicePvModuleDto> pvInfo) {
        this.pvInfo = pvInfo;
    }

    public List<DeviceDetail> getChildDevices() {
        return childDevices;
    }

    public void setChildDevices(List<DeviceDetail> childDevices) {
        this.childDevices = childDevices;
    }

    public Double getInstalledCapacity() {
        return installedCapacity;
    }

    public void setInstalledCapacity(Double installedCapacity) {
        this.installedCapacity = installedCapacity;
    }

}
