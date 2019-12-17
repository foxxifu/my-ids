package com.interest.ids.common.project.bean.signal;

import com.interest.ids.common.project.bean.BaseBean;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "ids_signal_version_t")
public class SignalVersionInfo extends BaseBean implements Serializable {

    private static final long serialVersionUID = -5680803865986680987L;

    /**
     * 父版本编码
     */
    @Column(name = "parent_id")
    private Long parentId;
    
    @Column(name = "name")
    private String name;

    /**
     * 版本编码
     */
    @Column(name = "signal_version")
    private String signalVersion;

    /**
     * 企业编码,归属企业,null表示属于共享信息
     */
    @Column(name = "enterprise_id")
    private Long enterpriseId;
    
    /**
     * 电站编码
     */
    @Column(name = "station_code")
    private String stationCode;

    /**
     * 设备类型id
     */
    @Column(name = "dev_type_id")
    private Integer devTypeId;

    /**
     * 设备供应商名称
     */
    @Column(name = "vender_name")
    private String venderName;

    /**
     * 协议编码:104 103 hwmodbus
     */
    @Column(name = "protocol_code")
    private String protocolCode;

    /**
     * 接口协议版本
     */
    @Column(name = "interface_version")
    private String interfaceVersion;


    /**
     * 创建日期
     */
    @Column(name = "create_date")
    private Date createDate;

    /**
     * 修改日期
     */
    @Column(name = "modified_date")
    private Date modifiedDate;

    /**
     * 数据创建类型
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 电站名称
     */
    @Transient
    private String stationName;
    /**
     * 设备的类型名称
     */
    @Transient
    private String devTypeName;

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getSignalVersion() {
		return signalVersion;
	}

	public void setSignalVersion(String signalVersion) {
		this.signalVersion = signalVersion;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public Integer getDevTypeId() {
		return devTypeId;
	}

	public void setDevTypeId(Integer devTypeId) {
		this.devTypeId = devTypeId;
	}

	public String getVenderName() {
		return venderName;
	}

	public void setVenderName(String venderName) {
		this.venderName = venderName;
	}

	public String getProtocolCode() {
		return protocolCode;
	}

	public void setProtocolCode(String protocolCode) {
		this.protocolCode = protocolCode;
	}

	public String getInterfaceVersion() {
		return interfaceVersion;
	}

	public void setInterfaceVersion(String interfaceVersion) {
		this.interfaceVersion = interfaceVersion;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDevTypeName() {
		return devTypeName;
	}

	public void setDevTypeName(String devTypeName) {
		this.devTypeName = devTypeName;
	}

	
	
 }