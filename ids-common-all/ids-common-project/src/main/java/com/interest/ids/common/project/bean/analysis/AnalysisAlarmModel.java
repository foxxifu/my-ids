package com.interest.ids.common.project.bean.analysis;

import javax.persistence.*;

@Table(name = "ids_analysis_alarm_model_t")
public class AnalysisAlarmModel {
    /**
     * 告警编号
     */
    @Id
    @Column(name = "alarm_id")
    private Byte alarmId;

    /**
     * 告警定义名称
     */
    @Column(name = "alarm_name")
    private String alarmName;

    /**
     * 修复建议
     */
    @Column(name = "repair_suggestion")
    private String repairSuggestion;

    /**
     * 告警级别
     */
    @Column(name = "severity_id")
    private Byte severityId;
    
    @Column(name = "alarm_cause")
    private String alarmCause;

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
     * 获取告警定义名称
     *
     * @return alarm_name - 告警定义名称
     */
    public String getAlarmName() {
        return alarmName;
    }

    /**
     * 设置告警定义名称
     *
     * @param alarmName 告警定义名称
     */
    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
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
     * 获取告警级别
     *
     * @return severity_id - 告警级别
     */
    public Byte getSeverityId() {
        return severityId;
    }

    /**
     * 设置告警级别
     *
     * @param severityId 告警级别
     */
    public void setSeverityId(Byte severityId) {
        this.severityId = severityId;
    }

	public String getAlarmCause() {
		return alarmCause;
	}

	public void setAlarmCause(String alarmCause) {
		this.alarmCause = alarmCause;
	}
    
    
}