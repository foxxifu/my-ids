package com.interest.ids.biz.web.alarm.vo;

/**
 * @Author: sunbjx
 * @Description: 告警修复建议VO
 * @Date: Created in 下午2:29 18-1-8
 * @Modified By:
 */
public class AlarmRepariVO {

    // 告警名称
    private String alarmName;
    // 告警级别
    private String alarmLeve;
    // 告警第一次发生时间
    private String firstDate;
    // 告警最后一次发生时间
    private String lastDate;
    // 告警次数
    private int alarmNumber;
    // 版本名称
    private String version;
    // 告警定位
    private String alarmPosition;
    // 设备名称
    private String devAlias;
    // 告警原因
    private String alarmCause;
    // 修复建议
    private String repairSuggestion;

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public String getAlarmLeve() {
        return alarmLeve;
    }

    public void setAlarmLeve(String alarmLeve) {
        this.alarmLeve = alarmLeve;
    }

    public String getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(String firstDate) {
        this.firstDate = firstDate;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public int getAlarmNumber() {
        return alarmNumber;
    }

    public void setAlarmNumber(int alarmNumber) {
        this.alarmNumber = alarmNumber;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAlarmPosition() {
        return alarmPosition;
    }

    public void setAlarmPosition(String alarmPosition) {
        this.alarmPosition = alarmPosition;
    }

    public String getDevAlias() {
        return devAlias;
    }

    public void setDevAlias(String devAlias) {
        this.devAlias = devAlias;
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
}
