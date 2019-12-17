package com.interest.ids.common.project.bean.alarm;


import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.interest.ids.common.project.bean.BaseBean;

@Table(name="ids_alarm_t")
public class AlarmM extends BaseBean {
	
    private static final long serialVersionUID = 7474600972486986197L;

    /**
     * 告警名称
     */
    private String alarmName;
    
    
    @Column(name="dev_type_id")
    protected Integer devTypeId;
    
    @Column(name="dev_id")
    protected Long devId;
    
    @Column(name="dev_alias")
    private String devName; 
    //1、未处理（活动）； 2、已确认（用户确认）； 3、处理中（转缺陷票）； 4：已处理（缺陷处理结束）； 5、已清除（用户清除）；6、已恢复（设备自动恢复）
    @Column(name="status_id")
    private Integer statusId;
    
    @Column(name="signal_version")
    protected String signalVersion;
    // 告警id
    @Column(name="alarm_id")
    protected Long alarmId;
    
    @Column(name="act_id")
    protected Integer actId;
    // 原因码
    
    @Column(name="caused_id")
    protected Long causeId;
    
    // 告警流水号
    @Column(name="sequence_num")
    protected Long sequenceNum;
    //电站编号
    @Column(name="station_code")
    protected String stationCode;
    //电站名称
    @Column(name="station_name")
    protected String stationName;
    //最后一次发生时间
    @Column(name="last_happen_time")
    protected Long lastHappenTime;
    //电站的发生时间
    @Column(name="station_happen_time")
    protected Long stationHappenTime;
    //第一次发生时间
    @Column(name="first_happen_time")
    protected Long firstHappenTime;
    //确认时间
    @Column(name="confirm_time")
    protected Long confirmTime;
    //恢复时间
    @Column(name="recover_time")
    protected Long recoverTime;
    //级别id
    @Column(name="level_id")
    protected Integer levelId;
    
    @Column(name="tele_type")
    protected Short teleTypeId;
    
    // 工作流编号
    @Column(name="work_flow_id")
    protected Long workFlowId;
    //1、告警 2、事件 3、自检',
    @Column(name="alarm_type")
    protected Integer alarmType;
    
    @Column(name="create_time")
    protected Long createTime;
    /**
     * 告警间隔
     */
    @Column(name="dev_interval")
    protected String devInterval;
    
    
    // 不持久化y
    @Transient 
    private String alarmCause;
    // 不持久化
    @Transient 
    private String repairSuggestion;
    // 不持久化
    @Transient 
    private String levName;
    // 不持久化
    @Transient 
    private String statusName;
    // 不持久化
    @Transient 
    private String alarmTypeName;
    @Transient
    private String teleTypeName;
    //不持久化，品联数采的下挂设备上报告警用到
    @Transient
    private Long sigAddress;
    @Transient
	private Integer bitIndex;
    @Transient
	private Timestamp updateDate;

	public String getAlarmName() {
		return alarmName;
	}

	public void setAlarmName(String alarmName) {
		this.alarmName = alarmName;
	}

	public Integer getDevTypeId() {
		return devTypeId;
	}

	public void setDevTypeId(Integer devTypeId) {
		this.devTypeId = devTypeId;
	}

	public Long getDevId() {
		return devId;
	}

	public void setDevId(Long devId) {
		this.devId = devId;
	}

	public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getSignalVersion() {
		return signalVersion;
	}

	public void setSignalVersion(String signalVersion) {
		this.signalVersion = signalVersion;
	}

	public Long getAlarmId() {
		return alarmId;
	}

	public void setAlarmId(Long alarmId) {
		this.alarmId = alarmId;
	}

	public Integer getActId() {
		return actId;
	}

	public void setActId(Integer actId) {
		this.actId = actId;
	}

	public Long getCauseId() {
		return causeId;
	}

	public void setCauseId(Long causeId) {
		this.causeId = causeId;
	}

	public Long getSequenceNum() {
		return sequenceNum;
	}

	public void setSequenceNum(Long sequenceNum) {
		this.sequenceNum = sequenceNum;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public Long getLastHappenTime() {
		return lastHappenTime;
	}

	public void setLastHappenTime(Long lastHappenTime) {
		this.lastHappenTime = lastHappenTime;
	}

	public Long getStationHappenTime() {
		return stationHappenTime;
	}

	public void setStationHappenTime(Long stationHappenTime) {
		this.stationHappenTime = stationHappenTime;
	}

	public Long getFirstHappenTime() {
		return firstHappenTime;
	}

	public void setFirstHappenTime(Long firstHappenTime) {
		this.firstHappenTime = firstHappenTime;
	}

	public Long getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Long confirmTime) {
		this.confirmTime = confirmTime;
	}

	public Long getRecoverTime() {
		return recoverTime;
	}

	public void setRecoverTime(Long recoverTime) {
		this.recoverTime = recoverTime;
	}

	public Integer getLevelId() {
		return levelId;
	}

	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}

	public Short getTeleTypeId() {
		return teleTypeId;
	}

	public void setTeleTypeId(Short teleTypeId) {
		this.teleTypeId = teleTypeId;
	}

	public Long getWorkFlowId() {
		return workFlowId;
	}

	public void setWorkFlowId(Long workFlowId) {
		this.workFlowId = workFlowId;
	}

	public Integer getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(Integer alarmType) {
		this.alarmType = alarmType;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	
	public String getDevInterval() {
		return devInterval;
	}

	public void setDevInterval(String devInterval) {
		this.devInterval = devInterval;
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

	public String getLevName() {
		return levName;
	}

	public void setLevName(String levName) {
		this.levName = levName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getAlarmTypeName() {
		return alarmTypeName;
	}

	public void setAlarmTypeName(String alarmTypeName) {
		this.alarmTypeName = alarmTypeName;
	}

	public String getTeleTypeName() {
		return teleTypeName;
	}

	public void setTeleTypeName(String teleTypeName) {
		this.teleTypeName = teleTypeName;
	}

	public Long getSigAddress() {
		return sigAddress;
	}

	public void setSigAddress(Long sigAddress) {
		this.sigAddress = sigAddress;
	}

	public Integer getBitIndex() {
		return bitIndex;
	}

	public void setBitIndex(Integer bitIndex) {
		this.bitIndex = bitIndex;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}
}
