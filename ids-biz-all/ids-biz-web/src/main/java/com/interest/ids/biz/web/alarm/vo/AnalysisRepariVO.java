package com.interest.ids.biz.web.alarm.vo;

/**
 * @Author: sunbjx
 * @Description: 智能告警修复建议
 * @Date: Created in 上午11:38 18-2-10
 * @Modified By:
 */
public class AnalysisRepariVO {

    private Integer id;
    private Integer causeId;
    private String alarmCause;
    private String repairSuggestion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCauseId() {
        return causeId;
    }

    public void setCauseId(Integer causeId) {
        this.causeId = causeId;
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
