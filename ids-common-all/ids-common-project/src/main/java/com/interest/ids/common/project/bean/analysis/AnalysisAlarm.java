package com.interest.ids.common.project.bean.analysis;

import javax.persistence.*;

@Table(name = "ids_analysis_alarm_t")
public class AnalysisAlarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 设备id
     */
    @Column(name = "dev_id")
    private Long devId;

    /**
     * 电站ID
     */
    @Column(name = "station_id")
    private String stationId;


    /**
     * 告警编号
     */
    @Column(name = "alarm_id")
    private Byte alarmId;

    /**
     * 故障组串序号
     */
    @Column(name = "alarm_pv_num")
    private Integer alarmPvNum;

    /**
     * 告警名称
     */
    @Column(name = "alarm_name")
    private String alarmName;

    /**
     * 设备名称
     */
    @Column(name = "dev_alias")
    private String devAlias;

    /**
     * 设备类型ID
     */
    @Column(name = "dev_type_id")
    private Integer devTypeId;

    /**
     * 设备类型名称
     */
    @Column(name = "dev_type_name")
    private String devTypeName;

    /**
     * 入库时间
     */
    @Column(name = "create_time")
    private Long createTime;

    /**
     * 发生时间
     */
    @Column(name = "happen_time")
    private Long happenTime;

    /**
     * 处理时间
     */
    @Column(name = "deal_time")
    private Long dealTime;

    /**
     * 恢复时间
     */
    @Column(name = "recovered_time")
    private Long recoveredTime;

    /**
     * 告警状态
     */
    @Column(name = "alarm_state")
    private Byte alarmState;

    /**
     * 关联业务
     */
    @Column(name = "relate_table")
    private String relateTable;

    /**
     * 业务表Id
     */
    @Column(name = "relate_keyId")
    private Long relateKeyid;

    /**
     * esn号
     */
    @Column(name = "sn_code")
    private String snCode;


    /**
     * 修复建议
     */
    @Column(name = "repair_suggestion")
    private String repairSuggestion;

    /**
     * 告警级别id
     */
    @Column(name = "severity_id")
    private Byte severityId;

    /**
     * 告警定位
     */
    @Column(name = "alarm_location")
    private String alarmLocation;

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
     * 获取设备id
     *
     * @return dev_id - 设备id
     */
    public Long getDevId() {
        return devId;
    }

    /**
     * 设置设备id
     *
     * @param devId 设备id
     */
    public void setDevId(Long devId) {
        this.devId = devId;
    }

    /**
     * 获取电站ID
     *
     * @return station_id - 电站ID
     */
    public String getStationId() {
        return stationId;
    }

    /**
     * 设置电站ID
     *
     * @param stationId 电站ID
     */
    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

  
    /**
     * 获取告警编号
     *
     * @return alarm_id - 告警编号
     */
    public Byte getAlarmId() {
        return alarmId;
    }

    /**
     * 设置告警编号
     *
     * @param alarmId 告警编号
     */
    public void setAlarmId(Byte alarmId) {
        this.alarmId = alarmId;
    }

    /**
     * 获取故障组串序号
     *
     * @return alarm_pv_num - 故障组串序号
     */
    public Integer getAlarmPvNum() {
        return alarmPvNum;
    }

    /**
     * 设置故障组串序号
     *
     * @param alarmPvNum 故障组串序号
     */
    public void setAlarmPvNum(Integer alarmPvNum) {
        this.alarmPvNum = alarmPvNum;
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
     * 获取设备名称
     *
     * @return dev_name - 设备名称
     */
    public String getDevAlias() {
        return devAlias;
    }

    /**
     * 设置设备名称
     *
     * @param devName 设备名称
     */
    public void setDevName(String devAlias) {
        this.devAlias = devAlias;
    }

    /**
     * 获取设备类型ID
     *
     * @return dev_type_id - 设备类型ID
     */
    public Integer getDevTypeId() {
        return devTypeId;
    }

    /**
     * 设置设备类型ID
     *
     * @param devTypeId 设备类型ID
     */
    public void setDevTypeId(Integer devTypeId) {
        this.devTypeId = devTypeId;
    }

    /**
     * 获取设备类型名称
     *
     * @return dev_type_name - 设备类型名称
     */
    public String getDevTypeName() {
        return devTypeName;
    }

    /**
     * 设置设备类型名称
     *
     * @param devTypeName 设备类型名称
     */
    public void setDevTypeName(String devTypeName) {
        this.devTypeName = devTypeName;
    }

    /**
     * 获取入库时间
     *
     * @return create_time - 入库时间
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * 设置入库时间
     *
     * @param createTime 入库时间
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取发生时间
     *
     * @return happen_time - 发生时间
     */
    public Long getHappenTime() {
        return happenTime;
    }

    /**
     * 设置发生时间
     *
     * @param happenTime 发生时间
     */
    public void setHappenTime(Long happenTime) {
        this.happenTime = happenTime;
    }

    /**
     * 获取处理时间
     *
     * @return deal_time - 处理时间
     */
    public Long getDealTime() {
        return dealTime;
    }

    /**
     * 设置处理时间
     *
     * @param dealTime 处理时间
     */
    public void setDealTime(Long dealTime) {
        this.dealTime = dealTime;
    }

    /**
     * 获取恢复时间
     *
     * @return recovered_time - 恢复时间
     */
    public Long getRecoveredTime() {
        return recoveredTime;
    }

    /**
     * 设置恢复时间
     *
     * @param recoveredTime 恢复时间
     */
    public void setRecoveredTime(Long recoveredTime) {
        this.recoveredTime = recoveredTime;
    }

    /**
     * 获取告警状态
     *
     * @return alarm_state - 告警状态
     */
    public Byte getAlarmState() {
        return alarmState;
    }

    /**
     * 设置告警状态
     *
     * @param alarmState 告警状态
     */
    public void setAlarmState(Byte alarmState) {
        this.alarmState = alarmState;
    }

    /**
     * 获取关联业务
     *
     * @return relate_table - 关联业务
     */
    public String getRelateTable() {
        return relateTable;
    }

    /**
     * 设置关联业务
     *
     * @param relateTable 关联业务
     */
    public void setRelateTable(String relateTable) {
        this.relateTable = relateTable;
    }

    /**
     * 获取业务表Id
     *
     * @return relate_keyId - 业务表Id
     */
    public Long getRelateKeyid() {
        return relateKeyid;
    }

    /**
     * 设置业务表Id
     *
     * @param relateKeyid 业务表Id
     */
    public void setRelateKeyid(Long relateKeyid) {
        this.relateKeyid = relateKeyid;
    }

    /**
     * 获取esn号
     *
     * @return esn_code - esn号
     */
    public String getSnCode() {
        return snCode;
    }

    /**
     * 设置esn号
     *
     * @param esnCode esn号
     */
    public void setSnCode(String snCode) {
        this.snCode = snCode;
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
     * 获取告警级别id
     *
     * @return severity_id - 告警级别id
     */
    public Byte getSeverityId() {
        return severityId;
    }

    /**
     * 设置告警级别id
     *
     * @param severityId 告警级别id
     */
    public void setSeverityId(Byte severityId) {
        this.severityId = severityId;
    }

    /**
     * 获取告警定位
     *
     * @return alarm_location - 告警定位
     */
    public String getAlarmLocation() {
        return alarmLocation;
    }
    

    /**
     * 设置告警定位
     *
     * @param alarmLocation 告警定位
     */
    public void setAlarmLocation(String alarmLocation) {
        this.alarmLocation = alarmLocation;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alarmId == null) ? 0 : alarmId.hashCode());
		result = prime * result
				+ ((alarmPvNum == null) ? 0 : alarmPvNum.hashCode());
		result = prime * result + ((devId == null) ? 0 : devId.hashCode());
		result = prime * result
				+ ((stationId == null) ? 0 : stationId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnalysisAlarm other = (AnalysisAlarm) obj;
		if (alarmId == null) {
			if (other.alarmId != null)
				return false;
		} else if (!alarmId.equals(other.alarmId))
			return false;
		if (alarmPvNum == null) {
			if (other.alarmPvNum != null)
				return false;
		} else if (!alarmPvNum.equals(other.alarmPvNum))
			return false;
		if (devId == null) {
			if (other.devId != null)
				return false;
		} else if (!devId.equals(other.devId))
			return false;
		if (stationId == null) {
			if (other.stationId != null)
				return false;
		} else if (!stationId.equals(other.stationId))
			return false;
		return true;
	}
}