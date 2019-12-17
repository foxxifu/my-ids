package com.interest.ids.common.project.bean.signal;

import java.io.Serializable;
import java.util.Date;

import com.interest.ids.common.project.bean.BaseBean;

import javax.persistence.*;

/**
 * 
 * @author lhq
 *
 */
@Table(name = "ids_signal_info_t")
public class SignalInfo extends BaseBean implements Serializable {
	private static final long serialVersionUID = -2382248178586709385L;

	/**
	 * 设备id
	 */
	@Column(name = "device_id")
	private Long deviceId;

	/**
	 * 信号实例名称
	 */
	@Column(name = "signal_name")
	private String signalName;

	/**
	 * 信号实例名称
	 */
	@Column(name = "signal_alias")
	private String signalAlias;
	/**
	 * 单位
	 */
	@Column(name = "signal_unit")
	private String signalUnit;

	/**
	 * bit位
	 */
	@Column(name = "register_type")
	private Integer registerType;
	
	/**
	 * bit位
	 */
	@Column(name = "bit")
	private Integer bit;

	/**
	 * signal_version
	 */
	@Column(name = "signal_version")
	private String signalVersion;
	
	/**
	 * 主信号地址，103的groupid
	 */
	@Column(name = "signal_group")
	private Integer signalGroup;

	/**
	 * 信号点地址
	 */
	@Column(name = "signal_address")
	private Integer signalAddress;

	/**
	 * modbus协议的寄存器个数
	 */
	@Column(name = "register_num")
	private Short registerNum;

	/**
	 * 增益
	 */
	@Column(name = "gain")
	private Double gain;

	/**
	 * 偏移量
	 */
	@Column(name = "offset")
	private Double offset;
	
	/**
	 * 1：遥测；2：单点遥信；3：双点遥信；4：单点遥控；5：双点遥控,6：遥脉；7：遥调；8：事件；9：告警
	 */
	@Column(name = "signal_type")
	private Integer signalType;


	/**
	 * 模型点表的id
	 */
	@Column(name = "model_id")
	private Long modelId;

	/**
	 * 1：无符号整数；2：有符号整数；3：浮点数；4：字符串；5：时间
	 */
	@Column(name = "data_type")
	private Integer dataType;

	/**
	 * 1表示该信号点为遥测告警标志位， 0表示否
	 */
	@Column(name = "is_alarm_flag")
	private Boolean isAlarmFlag;

	/**
	 * 1表示该信号点为遥测告警有效值，0表示否
	 */
	@Column(name = "is_alarm_val")
	private Boolean isAlarmVal;

	/**
	 * 是否存在数据范围设置
	 */
	@Column(name = "is_limited")
	private Boolean isLimited;


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

	@Transient
	private String devName;

	@Transient
	private String protocolCode;
	
	public Long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	public String getSignalName() {
		return signalName;
	}
	public void setSignalName(String signalName) {
		this.signalName = signalName;
	}
	public String getSignalUnit() {
		return signalUnit;
	}
	public void setSignalUnit(String signalUnit) {
		this.signalUnit = signalUnit;
	}
	public Integer getBit() {
		return bit;
	}
	public void setBit(Integer bit) {
		this.bit = bit;
	}
	public Integer getSignalGroup() {
		return signalGroup;
	}
	public void setSignalGroup(Integer signalGroup) {
		this.signalGroup = signalGroup;
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
	public Double getGain() {
		return gain;
	}
	public void setGain(Double gain) {
		this.gain = gain;
	}
	public Integer getSignalType() {
		return signalType;
	}
	public void setSignalType(Integer signalType) {
		this.signalType = signalType;
	}
	public Double getOffset() {
		return offset;
	}
	public void setOffset(Double offset) {
		this.offset = offset;
	}
	public Long getModelId() {
		return modelId;
	}
	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}
	public Integer getDataType() {
		return dataType;
	}
	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}
	public Boolean getIsAlarmFlag() {
		return isAlarmFlag;
	}
	public void setIsAlarmFlag(Boolean isAlarmFlag) {
		this.isAlarmFlag = isAlarmFlag;
	}
	public Boolean getIsAlarmVal() {
		return isAlarmVal;
	}
	public void setIsAlarmVal(Boolean isAlarmVal) {
		this.isAlarmVal = isAlarmVal;
	}
	public Boolean getIsLimited() {
		return isLimited;
	}
	public void setIsLimited(Boolean isLimited) {
		this.isLimited = isLimited;
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
	public String getSignalVersion() {
		return signalVersion;
	}
	public void setSignalVersion(String signalVersion) {
		this.signalVersion = signalVersion;
	}
	public String getDevName() {
		return devName;
	}
	public void setDevName(String devName) {
		this.devName = devName;
	}
	public String getSignalAlias() {
		return signalAlias;
	}
	public void setSignalAlias(String signalAlias) {
		this.signalAlias = signalAlias;
	}
	public String getProtocolCode() {
		return protocolCode;
	}
	public void setProtocolCode(String protocolCode) {
		this.protocolCode = protocolCode;
	}
	public Integer getRegisterType() {
		return registerType;
	}
	public void setRegisterType(Integer registerType) {
		this.registerType = registerType;
	}
	
	
}