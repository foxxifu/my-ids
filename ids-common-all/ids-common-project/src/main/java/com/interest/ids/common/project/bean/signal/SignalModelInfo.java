package com.interest.ids.common.project.bean.signal;


import java.io.Serializable;
import java.util.Date;

import com.interest.ids.common.project.bean.BaseBean;

import javax.persistence.*;


/**
 *
 */
@Table(name = "ids_signal_model_t")
public class SignalModelInfo extends BaseBean implements Serializable {
    private static final long serialVersionUID = -8296233868675138259L;

    @Column(name = "signal_name")
    private String signalName;

    @Column(name = "signal_version")
    private String signalVersion;

    @Column(name = "signal_alias")
    private String signalAlias;

    @Column(name = "signal_unit")
    private String signalUnit;

    /**
     * 1：遥测；2：单点遥信；3：双点遥信；4：单点遥控；5：双点遥控,6：遥脉；7：遥调；8：事件；9：告警
     */
    @Column(name = "signal_type")
    private Integer signalType;

    /**
     * 1:变位信号  2：异常告警  3：保护事件  4：通知状态  5：告警信息
     */
    @Column(name = "tele_type")
    private Integer teleType;

    /**
     * 1：无符号整数；2：有符号整数；3：浮点数；4：字符串；5：时间
     */
    @Column(name = "data_type")
    private Integer dataType;

    @Column(name = "gain")
    private Double gain;

    @Column(name = "offset")
    private Double offset;

    /**
     * 信息体地址
     */
    @Column(name = "signal_address")
    private Integer signalAddress;

    /**
     * 寄存器个数
     */
    @Column(name = "register_num")
    private Short registerNum;

    /**
     * 主信号地址，103的groupid
     */
    @Column(name = "signal_group")
    private Integer signalGroup;

    private Integer bit;


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
     * 协议编码:104 103 hwmodbus
     */
    @Column(name = "protocol_code")
    private String protocolCode;

	public String getSignalName() {
		return signalName;
	}

	public void setSignalName(String signalName) {
		this.signalName = signalName;
	}

	public String getSignalVersion() {
		return signalVersion;
	}

	public void setSignalVersion(String signalVersion) {
		this.signalVersion = signalVersion;
	}

	public String getSignalAlias() {
		return signalAlias;
	}

	public void setSignalAlias(String signalAlias) {
		this.signalAlias = signalAlias;
	}

	public String getSignalUnit() {
		return signalUnit;
	}

	public void setSignalUnit(String signalUnit) {
		this.signalUnit = signalUnit;
	}

	public Integer getSignalType() {
		return signalType;
	}

	public void setSignalType(Integer signalType) {
		this.signalType = signalType;
	}

	public Integer getTeleType() {
		return teleType;
	}

	public void setTeleType(Integer teleType) {
		this.teleType = teleType;
	}

	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public Double getGain() {
		return gain;
	}

	public void setGain(Double gain) {
		this.gain = gain;
	}

	public Double getOffset() {
		return offset;
	}

	public void setOffset(Double offset) {
		this.offset = offset;
	}

	public Integer getSignalAddress() {
		return signalAddress;
	}

	public void setSignalAddress(Integer signalAddress) {
		this.signalAddress = signalAddress;
	}

	public Short getRegisterNum() {
		return registerNum;
	}

	public void setRegisterNum(Short registerNum) {
		this.registerNum = registerNum;
	}

	public Integer getSignalGroup() {
		return signalGroup;
	}

	public void setSignalGroup(Integer signalGroup) {
		this.signalGroup = signalGroup;
	}

	public Integer getBit() {
		return bit;
	}

	public void setBit(Integer bit) {
		this.bit = bit;
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

	public String getProtocolCode() {
		return protocolCode;
	}

	public void setProtocolCode(String protocolCode) {
		this.protocolCode = protocolCode;
	}
}
