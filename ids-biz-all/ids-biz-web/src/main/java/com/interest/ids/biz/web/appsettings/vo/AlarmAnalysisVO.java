package com.interest.ids.biz.web.appsettings.vo;

/**
 * @Author: sunbjx
 * @Description: 智能告警参数设置
 * @Date Created in 08:22 2018/1/15
 * @Modified By:
 */
public class AlarmAnalysisVO {

    private Byte id;
    // 告警定义名称
    private String alarmName;
    // 告警级别
    private String alarmLevel;
    // 修复建议
    private String repairSuggestion;

    public Byte getId() {
        return id;
    }

    public void setId(Byte id) {
        this.id = id;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public String getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(String alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public String getRepairSuggestion() {
        return repairSuggestion;
    }

    public void setRepairSuggestion(String repairSuggestion) {
        this.repairSuggestion = repairSuggestion;
    }
}
