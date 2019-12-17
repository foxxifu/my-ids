package com.interest.ids.common.project.bean.device;

import javax.persistence.Column;
import javax.persistence.Table;

import com.interest.ids.common.project.bean.BaseBean;

@Table(name = "ids_dc_config_t")
public class DcConfig extends BaseBean {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 设备ID
     */
    @Column(name = "dev_id")
    private Long devId;

    /**
     * 通道类型 1: TCP 客户端；2: TCP服务端
     */
    @Column(name = "channel_type")
    private Byte channelType;

    private String ip;

    private Integer port;

    /**
     * 逻辑地址
     */
    @Column(name = "logical_addres")
    private Byte logicalAddres;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取设备ID
     *
     * @return dev_id - 设备ID
     */
    public Long getDevId() {
        return devId;
    }

    /**
     * 设置设备ID
     *
     * @param devId 设备ID
     */
    public void setDevId(Long devId) {
        this.devId = devId;
    }

    /**
     * 获取通道类型 1: TCP 客户端；2: TCP服务端
     *
     * @return channel_type - 通道类型 1: TCP 客户端；2: TCP服务端
     */
    public Byte getChannelType() {
        return channelType;
    }

    /**
     * 设置通道类型 1: TCP 客户端；2: TCP服务端
     *
     * @param channelType 通道类型 1: TCP 客户端；2: TCP服务端
     */
    public void setChannelType(Byte channelType) {
        this.channelType = channelType;
    }

    /**
     * @return ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return port
     */
    public Integer getPort() {
        return port;
    }

    /**
     * @param port
     */
    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * 获取逻辑地址
     *
     * @return logical_addres - 逻辑地址
     */
    public Byte getLogicalAddres() {
        return logicalAddres;
    }

    /**
     * 设置逻辑地址
     *
     * @param logicalAddres 逻辑地址
     */
    public void setLogicalAddres(Byte logicalAddres) {
        this.logicalAddres = logicalAddres;
    }
}