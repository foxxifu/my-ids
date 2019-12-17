package com.interest.ids.dev.starter.controller.params;

import com.interest.ids.common.project.bean.Pagination;

/**
 * @Author: sunbjx
 * @Description: 设备端口配置列表查询
 * @Date Created in 11:11 2018/1/18
 * @Modified By:
 */
public class DevConfigParams extends Pagination {

    // 设备名称
    private String devAlias;
    // 设备类型ID
    private Integer devTypeId;
    // 通道类型
    private Byte channelType;
    // 端口号
    private Integer port;

    public String getDevAlias() {
        return devAlias;
    }

    public void setDevAlias(String devAlias) {
        this.devAlias = devAlias;
    }

    public Integer getDevTypeId() {
        return devTypeId;
    }

    public void setDevTypeId(Integer devTypeId) {
        this.devTypeId = devTypeId;
    }

    public Byte getChannelType() {
        return channelType;
    }

    public void setChannelType(Byte channelType) {
        this.channelType = channelType;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
