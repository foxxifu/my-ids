package com.interest.ids.common.project.bean.alarm;

import com.interest.ids.common.project.bean.BaseBean;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "ids_alarm_model_t")
public class AlarmModel extends BaseBean implements Serializable {
    /**
     * 映射建，唯一识别。用于系统内部。
     */
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 企业编号
     */
    @Column(name = "enterprise_id")
    private Long enterpriseId;

    /**
     * 电站编号
     */
    @Column(name = "station_code")
    private String stationCode;

    /**
     * 归属型号版本编号
     */
    @Column(name = "signal_version")
    private String signalVersion;

    /**
     * 告警编号:点表中提供的告警编号，该编号不是由系统生成的，而是来源于点表的，稳定的字典信息
     */
    @Column(name = "alarm_id")
    private Integer alarmId;

    /**
     * 模型id
     */
    @Column(name = "model_id")
    private Long modelId;

    /**
     * 原因id:来源于点表
     */
    @Column(name = "cause_id")
    private Integer causeId;

    /**
     * 设备类型id
     */
    @Column(name = "dev_type_id")
    private Short devTypeId;

    /**
     * 告警级别
     */
    @Column(name = "severity_id")
    private Byte severityId;

    /**
     * 计量单位:要求使用英文规范的计量单位信息
     */
    @Column(name = "metro_unit")
    private String metroUnit;

    /**
     * 告警名称
     */
    @Column(name = "alarm_name")
    private String alarmName;

    @Column(name = "alarm_sub_name")
    private String alarmSubName;

    /**
     * 信号地址：信号地址是和具体设备相关的某一个信号的存取地址，一般的规范，比如104，modbus等都有这个概念
     */
    @Column(name = "sig_address")
    private Long sigAddress;

    /**
     * 信号bit位位置：有的信号会占据某一个寄存器的一个位，这个时候需要用到该信息。确定一个信号到底是取一个bit位还是取几个寄存器，需要根据信号的数据类型来确定
     */
    @Column(name = "bit_index")
    private Byte bitIndex;

    @Column(name = "alarm_cause")
    private String alarmCause;

    /**
     * 修复建议
     */
    @Column(name = "repair_suggestion")
    private String repairSuggestion;

    /**
     * 告警类型, 1:告警，2：时间，3：自检
     */
    @Column(name = "alarm_type")
    private Byte alarmType;

    /**
     * 替代alarmtype 展示，遥信信号拆分为  1:变位信号  2：异常告警  3：保护事件  4：通知状态  5：告警信息, 原有告警模型归一化为 2：异常告警
     */
    @Column(name = "tele_type")
    private Byte teleType;

    /**
     * 是否推送
     */
    @Column(name = "is_subscribed")
    private Boolean isSubscribed;

    /**
     * 是否被删除，用户删除信号点模型时该列置为true
     */
    @Column(name = "is_deleted")
    private Boolean isDeleted;

    /**
     * 是否为电站级别设置，该级别具有最高优先级，不会被托管域及二级域设置覆盖
     */
    @Column(name = "is_station_lev")
    private Boolean isStationLev;

    /**
     * 告警来源， 一般告警，遥测告警，bit位告警等
     */
    @Column(name = "source_id")
    private Byte sourceId;

    /**
     * 更新用户
     */
    @Column(name = "update_user_id")
    private Long updateUserId;

    /**
     * 最后更新时间
     */
    @Column(name = "update_date")
    private Date updateDate;

    /**
     * 获取映射建，唯一识别。用于系统内部。
     *
     * @return id - 映射建，唯一识别。用于系统内部。
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置映射建，唯一识别。用于系统内部。
     *
     * @param id 映射建，唯一识别。用于系统内部。
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取企业编号
     *
     * @return enterprise_id - 企业编号
     */
    public Long getEnterpriseId() {
        return enterpriseId;
    }

    /**
     * 设置企业编号
     *
     * @param enterpriseId 企业编号
     */
    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    /**
     * 获取电站编号
     *
     * @return station_code - 电站编号
     */
    public String getStationCode() {
        return stationCode;
    }

    /**
     * 设置电站编号
     *
     * @param stationCode 电站编号
     */
    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public String getSignalVersion() {
        return signalVersion;
    }

    public void setSignalVersion(String signalVersion) {
        this.signalVersion = signalVersion;
    }

    /**
     * 获取告警编号:点表中提供的告警编号，该编号不是由系统生成的，而是来源于点表的，稳定的字典信息
     *
     * @return alarm_id - 告警编号:点表中提供的告警编号，该编号不是由系统生成的，而是来源于点表的，稳定的字典信息
     */
    public Integer getAlarmId() {
        return alarmId;
    }

    /**
     * 设置告警编号:点表中提供的告警编号，该编号不是由系统生成的，而是来源于点表的，稳定的字典信息
     *
     * @param alarmId 告警编号:点表中提供的告警编号，该编号不是由系统生成的，而是来源于点表的，稳定的字典信息
     */
    public void setAlarmId(Integer alarmId) {
        this.alarmId = alarmId;
    }

    /**
     * 获取原因id:来源于点表
     *
     * @return cause_id - 原因id:来源于点表
     */
    public Integer getCauseId() {
        return causeId;
    }

    /**
     * 设置原因id:来源于点表
     *
     * @param causeId 原因id:来源于点表
     */
    public void setCauseId(Integer causeId) {
        this.causeId = causeId;
    }

    /**
     * 获取设备类型id
     *
     * @return device_type_id - 设备类型id
     */

    /**
     * 获取告警级别
     *
     * @return severity_id - 告警级别
     */
    public Byte getSeverityId() {
        return severityId;
    }

    public Short getDevTypeId() {
		return devTypeId;
	}

	public void setDevTypeId(Short devTypeId) {
		this.devTypeId = devTypeId;
	}

	/**
     * 设置告警级别
     *
     * @param severityId 告警级别
     */
    public void setSeverityId(Byte severityId) {
        this.severityId = severityId;
    }

    /**
     * 获取计量单位:要求使用英文规范的计量单位信息
     *
     * @return metro_unit - 计量单位:要求使用英文规范的计量单位信息
     */
    public String getMetroUnit() {
        return metroUnit;
    }

    /**
     * 设置计量单位:要求使用英文规范的计量单位信息
     *
     * @param metroUnit 计量单位:要求使用英文规范的计量单位信息
     */
    public void setMetroUnit(String metroUnit) {
        this.metroUnit = metroUnit;
    }

    /**
     * 获取告警名称
     *
     * @return alarm_name - 告警名称
     */
    public String getAlarmName() {
        return alarmName;
    }

    /**
     * 设置告警名称
     *
     * @param alarmName 告警名称
     */
    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    /**
     * @return alarm_sub_name
     */
    public String getAlarmSubName() {
        return alarmSubName;
    }

    /**
     * @param alarmSubName
     */
    public void setAlarmSubName(String alarmSubName) {
        this.alarmSubName = alarmSubName;
    }

    /**
     * 获取信号地址：信号地址是和具体设备相关的某一个信号的存取地址，一般的规范，比如104，modbus等都有这个概念
     *
     * @return sig_address - 信号地址：信号地址是和具体设备相关的某一个信号的存取地址，一般的规范，比如104，modbus等都有这个概念
     */
    public Long getSigAddress() {
        return sigAddress;
    }

    /**
     * 设置信号地址：信号地址是和具体设备相关的某一个信号的存取地址，一般的规范，比如104，modbus等都有这个概念
     *
     * @param sigAddress 信号地址：信号地址是和具体设备相关的某一个信号的存取地址，一般的规范，比如104，modbus等都有这个概念
     */
    public void setSigAddress(Long sigAddress) {
        this.sigAddress = sigAddress;
    }

    /**
     * 获取信号bit位位置：有的信号会占据某一个寄存器的一个位，这个时候需要用到该信息。确定一个信号到底是取一个bit位还是取几个寄存器，需要根据信号的数据类型来确定
     *
     * @return bit_index - 信号bit位位置：有的信号会占据某一个寄存器的一个位，这个时候需要用到该信息。确定一个信号到底是取一个bit位还是取几个寄存器，需要根据信号的数据类型来确定
     */
    public Byte getBitIndex() {
        return bitIndex;
    }

    /**
     * 设置信号bit位位置：有的信号会占据某一个寄存器的一个位，这个时候需要用到该信息。确定一个信号到底是取一个bit位还是取几个寄存器，需要根据信号的数据类型来确定
     *
     * @param bitIndex 信号bit位位置：有的信号会占据某一个寄存器的一个位，这个时候需要用到该信息。确定一个信号到底是取一个bit位还是取几个寄存器，需要根据信号的数据类型来确定
     */
    public void setBitIndex(Byte bitIndex) {
        this.bitIndex = bitIndex;
    }

    /**
     * @return alarm_cause
     */
    public String getAlarmCause() {
        return alarmCause;
    }

    /**
     * @param alarmCause
     */
    public void setAlarmCause(String alarmCause) {
        this.alarmCause = alarmCause;
    }

    /**
     * 获取修复建议
     *
     * @return repair_suggestion - 修复建议
     */
    public String getRepairSuggestion() {
        return repairSuggestion;
    }

    /**
     * 设置修复建议
     *
     * @param repairSuggestion 修复建议
     */
    public void setRepairSuggestion(String repairSuggestion) {
        this.repairSuggestion = repairSuggestion;
    }

    /**
     * 获取告警类型, 1:告警，2：时间，3：自检
     *
     * @return alarm_type - 告警类型, 1:告警，2：时间，3：自检
     */
    public Byte getAlarmType() {
        return alarmType;
    }

    /**
     * 设置告警类型, 1:告警，2：时间，3：自检
     *
     * @param alarmType 告警类型, 1:告警，2：时间，3：自检
     */
    public void setAlarmType(Byte alarmType) {
        this.alarmType = alarmType;
    }

    /**
     * 获取替代alarmtype 展示，遥信信号拆分为  1:变位信号  2：异常告警  3：保护事件  4：通知状态  5：告警信息, 原有告警模型归一化为 2：异常告警
     *
     * @return tele_type - 替代alarmtype 展示，遥信信号拆分为  1:变位信号  2：异常告警  3：保护事件  4：通知状态  5：告警信息, 原有告警模型归一化为 2：异常告警
     */
    public Byte getTeleType() {
        return teleType;
    }

    /**
     * 设置替代alarmtype 展示，遥信信号拆分为  1:变位信号  2：异常告警  3：保护事件  4：通知状态  5：告警信息, 原有告警模型归一化为 2：异常告警
     *
     * @param teleType 替代alarmtype 展示，遥信信号拆分为  1:变位信号  2：异常告警  3：保护事件  4：通知状态  5：告警信息, 原有告警模型归一化为 2：异常告警
     */
    public void setTeleType(Byte teleType) {
        this.teleType = teleType;
    }

    /**
     * 获取是否推送
     *
     * @return is_subscribed - 是否推送
     */
    public Boolean getIsSubscribed() {
        return isSubscribed;
    }

    /**
     * 设置是否推送
     *
     * @param isSubscribed 是否推送
     */
    public void setIsSubscribed(Boolean isSubscribed) {
        this.isSubscribed = isSubscribed;
    }

    /**
     * 获取是否被删除，用户删除信号点模型时该列置为true
     *
     * @return is_deleted - 是否被删除，用户删除信号点模型时该列置为true
     */
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    /**
     * 设置是否被删除，用户删除信号点模型时该列置为true
     *
     * @param isDeleted 是否被删除，用户删除信号点模型时该列置为true
     */
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * 获取是否为电站级别设置，该级别具有最高优先级，不会被托管域及二级域设置覆盖
     *
     * @return is_station_lev - 是否为电站级别设置，该级别具有最高优先级，不会被托管域及二级域设置覆盖
     */
    public Boolean getIsStationLev() {
        return isStationLev;
    }

    /**
     * 设置是否为电站级别设置，该级别具有最高优先级，不会被托管域及二级域设置覆盖
     *
     * @param isStationLev 是否为电站级别设置，该级别具有最高优先级，不会被托管域及二级域设置覆盖
     */
    public void setIsStationLev(Boolean isStationLev) {
        this.isStationLev = isStationLev;
    }

    /**
     * 获取告警来源， 一般告警，遥测告警，bit位告警等
     *
     * @return source_id - 告警来源， 一般告警，遥测告警，bit位告警等
     */
    public Byte getSourceId() {
        return sourceId;
    }

    /**
     * 设置告警来源， 一般告警，遥测告警，bit位告警等
     *
     * @param sourceId 告警来源， 一般告警，遥测告警，bit位告警等
     */
    public void setSourceId(Byte sourceId) {
        this.sourceId = sourceId;
    }

    /**
     * 获取更新用户
     *
     * @return update_user_id - 更新用户
     */
    public Long getUpdateUserId() {
        return updateUserId;
    }

    /**
     * 设置更新用户
     *
     * @param updateUserId 更新用户
     */
    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    /**
     * 获取最后更新时间
     *
     * @return update_date - 最后更新时间
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 设置最后更新时间
     *
     * @param updateDate 最后更新时间
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

	public Long getModelId() {
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}
    
}