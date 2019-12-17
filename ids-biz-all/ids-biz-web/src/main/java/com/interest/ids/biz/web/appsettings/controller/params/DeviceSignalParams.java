package com.interest.ids.biz.web.appsettings.controller.params;

import com.interest.ids.common.project.bean.Pagination;

/**
 * @Author: sunbjx
 * @Description:
 * @Date: Created in 下午3:25 18-3-2
 * @Modified By:
 */
public class DeviceSignalParams extends Pagination{

    private Long versionId;
    // 设备名称
    private String deviceName;
    // 信号点名称
    private String signalName;
    // 版本号
    private String version;
    // 信号点类型
    private Integer signalType;

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getSignalName() {
        return signalName;
    }

    public void setSignalName(String signalName) {
        this.signalName = signalName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getSignalType() {
        return signalType;
    }

    public void setSignalType(Integer signalType) {
        this.signalType = signalType;
    }
}
