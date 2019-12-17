package com.interest.ids.biz.web.appsettings.vo;

/**
 * @Author: sunbjx
 * @Description:
 * @Date: Created in 下午5:44 18-3-2
 * @Modified By:
 */
public class SignalInfoVO {

    // 信号id
    private Long id;
    // 设备名称
    private String deviceName;
    // 信号点名称
    private String sigName;
    // 增益
    private Double sigGain;
    // 偏移量
    private Double sigOffset;
    // 信号点类型
    private Integer sigType;
    // 版本号
    private String version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getSigName() {
        return sigName;
    }

    public void setSigName(String sigName) {
        this.sigName = sigName;
    }

    public Double getSigGain() {
        return sigGain;
    }

    public void setSigGain(Double sigGain) {
        this.sigGain = sigGain;
    }

    public Double getSigOffset() {
        return sigOffset;
    }

    public void setSigOffset(Double sigOffset) {
        this.sigOffset = sigOffset;
    }

    public Integer getSigType() {
        return sigType;
    }

    public void setSigType(Integer sigType) {
        this.sigType = sigType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
