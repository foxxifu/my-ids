package com.interest.ids.common.project.bean.ticket;

import java.io.Serializable;

public class WorkTicket implements Serializable{

	private static final long serialVersionUID = 8386239772248382906L;
	private Long id;
	/**负责人的id*/
	private Long chargeId; 
	/**负责人的名字*/
	private String chargeName;
	/**工作成员的id*/
	private String groupId;
	/**工作成员名字*/
	private String groupNames;
	/**工作内容*/
	private String workContent;
	/**关联告警的id*/
	private String alarmIds;
	/**告警类型-1设备告警2智能告警*/
	private String alarmType;
	private String alarmNames;
	/**计划开始时间*/
	private Long startWorkTime;
	/**计划结束时间*/
	private Long endWorkTime;
	/**安全技术措施*/
	private String safetyTechnical;
	/**个人安全措施*/
	private String personalSafetyTechnical;
	/**已安装接地线*/
	private String groundWire;
	/**已安设置安全警示标识牌*/
	private String safetyWarningSigns;
	/**工作地点保留带电设备*/
	private String liveElectrifiedEquipment;
	/**签发人id*/
	private Long issueId;
	/**签发人的名字*/
	private String issuerName;
	/**设备id*/
	private Long devId;
	/**流程的状态*/
	private String actState;
	
	private Long businessKey;
	private String definitionId;
	private Long taskId;
	private String devAlias;
	private String stationCode;
	private String workAddress;
	private Long createTime;
	private Short dealType;
	private Long userId;
	
	public String getActState() {
		return actState;
	}
	public void setActState(String actState) {
		this.actState = actState;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Short getDealType() {
		return dealType;
	}
	public void setDealType(Short dealType) {
		this.dealType = dealType;
	}
	public String getAlarmNames() {
		return alarmNames;
	}
	public void setAlarmNames(String alarmNames) {
		this.alarmNames = alarmNames;
	}
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	public String getWorkAddress() {
		return workAddress;
	}
	public void setWorkAddress(String workAddress) {
		this.workAddress = workAddress;
	}
	public String getDevAlias() {
		return devAlias;
	}
	public void setDevAlias(String devAlias) {
		this.devAlias = devAlias;
	}
	public String getStationCode() {
		return stationCode;
	}
	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public Long getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(Long businessKey) {
		this.businessKey = businessKey;
	}
	public String getDefinitionId() {
		return definitionId;
	}
	public void setDefinitionId(String definitionId) {
		this.definitionId = definitionId;
	}
	public Long getDevId() {
		return devId;
	}
	public void setDevId(Long devId) {
		this.devId = devId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getChargeId() {
		return chargeId;
	}
	public void setChargeId(Long chargeId) {
		this.chargeId = chargeId;
	}
	public String getChargeName() {
		return chargeName;
	}
	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupNames() {
		return groupNames;
	}
	public void setGroupNames(String groupNames) {
		this.groupNames = groupNames;
	}
	public String getWorkContent() {
		return workContent;
	}
	public void setWorkContent(String workContent) {
		this.workContent = workContent;
	}

	public String getAlarmIds() {
		return alarmIds;
	}
	public void setAlarmIds(String alarmIds) {
		this.alarmIds = alarmIds;
	}
	public String getAlarmType() {
		return alarmType;
	}
	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}
	public Long getStartWorkTime() {
		return startWorkTime;
	}
	public void setStartWorkTime(Long startWorkTime) {
		this.startWorkTime = startWorkTime;
	}
	public Long getEndWorkTime() {
		return endWorkTime;
	}
	public void setEndWorkTime(Long endWorkTime) {
		this.endWorkTime = endWorkTime;
	}
	public String getSafetyTechnical() {
		return safetyTechnical;
	}
	public void setSafetyTechnical(String safetyTechnical) {
		this.safetyTechnical = safetyTechnical;
	}
	public String getPersonalSafetyTechnical() {
		return personalSafetyTechnical;
	}
	public void setPersonalSafetyTechnical(String personalSafetyTechnical) {
		this.personalSafetyTechnical = personalSafetyTechnical;
	}
	public String getGroundWire() {
		return groundWire;
	}
	public void setGroundWire(String groundWire) {
		this.groundWire = groundWire;
	}
	
	public String getSafetyWarningSigns() {
		return safetyWarningSigns;
	}
	public void setSafetyWarningSigns(String safetyWarningSigns) {
		this.safetyWarningSigns = safetyWarningSigns;
	}
	public String getLiveElectrifiedEquipment() {
		return liveElectrifiedEquipment;
	}
	public void setLiveElectrifiedEquipment(String liveElectrifiedEquipment) {
		this.liveElectrifiedEquipment = liveElectrifiedEquipment;
	}

	public Long getIssueId() {
		return issueId;
	}
	public void setIssueId(Long issueId) {
		this.issueId = issueId;
	}
	public String getIssuerName() {
		return issuerName;
	}
	public void setIssuerName(String issuerName) {
		this.issuerName = issuerName;
	}
	
}
