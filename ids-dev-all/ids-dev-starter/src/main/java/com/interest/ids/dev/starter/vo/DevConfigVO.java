package com.interest.ids.dev.starter.vo;

/**
 * @Author: sunbjx
 * @Description:
 * @Date Created in 10:54 2018/1/18
 * @Modified By:
 */
public class DevConfigVO {

    // 设备ID
    private Long devId;
    // 设备名称
    private String devAlias;
    // 设备版本号
    private String devVersion;
    // 通道类型
    private Byte channelType;
    // IP
    private String ip;
    // port
    private Integer port;
    // 逻辑地址
    private Byte logicalAddres;

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

    public String getDevVersion() {
        return devVersion;
    }

    public void setDevVersion(String devVersion) {
        this.devVersion = devVersion;
    }

    public Byte getChannelType() {
        return channelType;
    }

    public void setChannelType(Byte channelType) {
        this.channelType = channelType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Byte getLogicalAddres() {
        return logicalAddres;
    }

    public void setLogicalAddres(Byte logicalAddres) {
        this.logicalAddres = logicalAddres;
    }
}
