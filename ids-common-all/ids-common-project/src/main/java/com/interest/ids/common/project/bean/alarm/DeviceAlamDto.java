package com.interest.ids.common.project.bean.alarm;

import java.io.Serializable;

import com.interest.ids.common.project.constant.DevTypeConstant;

public class DeviceAlamDto implements Serializable {
    private static final long serialVersionUID = 1L;
    // 告警名称
    private String alarmName;
    // 设备id
    private Long devId;
    // 设备名称
    private String devAlias;
    // 告警级别的id
    private Integer levelId;

    private Integer statusId;

    private Double longitude;

    private Double latitude;

    private Long firstHappenTime;
    // 复位直接
    private Long recoverTime;
    // 设备类型id
    private Integer devTypeId;
    // 设备类型名称
    private String devTypeName;
    // 告警类型
    private Integer alarmType;
    // 告警类型的名称
    private String alarmTypeName;
    // sn编码
    private String snCode;
    // 设备地址
    private String devAddress;
    // 告警原因
    private String alarmCause;
    // 修复建议
    private String repairSuggestion;

    private Long startTime;

    private Long endTime;

    // 当前页
    private Long index;
    // 每页显示的条数
    private Integer pageSize;
    // 数据总条数
    private Long count;

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Long getFirstHappenTime() {
        return firstHappenTime;
    }

    public void setFirstHappenTime(Long firstHappenTime) {
        this.firstHappenTime = firstHappenTime;
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

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getDevId() {
        return devId;
    }

    public void setDevId(Long devId) {
        this.devId = devId;
    }

    public String getDevAlias() {
        return devAlias;
    }

    public void setDevAlias(String devAlias) {
        this.devAlias = devAlias;
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public Long getRecoverTime() {
        return recoverTime;
    }

    public void setRecoverTime(Long recoverTime) {
        this.recoverTime = recoverTime;
    }

    public Integer getDevTypeId() {
        return devTypeId;
    }

    public void setDevTypeId(Integer devTypeId) {
        this.devTypeId = devTypeId;
        this.devTypeName = DevTypeConstant.DEV_TYPE_I18N_ID.get(this.devTypeId);
    }

    public String getDevTypeName() {
        return devTypeName;
    }

    public void setDevTypeName(String devTypeName) {
        this.devTypeName = devTypeName;
    }

    public Integer getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(Integer alarmType) {
        this.alarmType = alarmType;
        this.alarmTypeName = this.alarmType == 1 ? "告警告警" : (this.alarmType == 2 ? "事件告警" : "自检告警");
    }

    public String getAlarmTypeName() {
        return alarmTypeName;
    }

    public void setAlarmTypeName(String alarmTypeName) {
        this.alarmTypeName = alarmTypeName;
    }

    public String getSnCode() {
        return snCode;
    }

    public void setSnCode(String snCode) {
        this.snCode = snCode;
    }

    public String getDevAddress() {
        return devAddress;
    }

    public void setDevAddress(String devAddress) {
        this.devAddress = devAddress;
    }

    public String getAlarmCause() {
        return alarmCause;
    }

    public void setAlarmCause(String alarmCause) {
        this.alarmCause = alarmCause;
    }

    public String getRepairSuggestion() {
        return repairSuggestion;
    }

    public void setRepairSuggestion(String repairSuggestion) {
        this.repairSuggestion = repairSuggestion;
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

}
