package com.interest.ids.biz.web.appsettings.controller.params;

import com.interest.ids.common.project.bean.Pagination;
import io.swagger.annotations.ApiModel;

/**
 * @Author: sunbjx
 * @Description: 告警 DTO
 * @Date Created in 16:33 2017/12/19
 * @Modified By:
 */
@ApiModel
public class AlarmConfigParams extends Pagination {

    private Long id;
    // 告警级别
    private Byte severityId;
    // 修复建议
    private String repairSuggestion;
    // 告警原因
    private String alarmCause;
    // 告警类型
    private Byte alarmType;
    // 告警名称
    private String alarmName;
    // 版本号
    private String version;
    // 设备类型
    private Byte devTypeId;
    
    private Byte alarmLevel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getSeverityId() {
        return severityId;
    }

    public void setSeverityId(Byte severityId) {
        this.severityId = severityId;
    }

    public String getRepairSuggestion() {
        return repairSuggestion;
    }

    public void setRepairSuggestion(String repairSuggestion) {
        this.repairSuggestion = repairSuggestion;
    }

    public String getAlarmCause() {
        return alarmCause;
    }

    public void setAlarmCause(String alarmCause) {
        this.alarmCause = alarmCause;
    }

    public Byte getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(Byte alarmType) {
        this.alarmType = alarmType;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Byte getDevTypeId() {
        return devTypeId;
    }

    public void setDevTypeId(Byte devTypeId) {
        this.devTypeId = devTypeId;
    }

	public Byte getAlarmLevel() {
		return alarmLevel;
	}

	public void setAlarmLevel(Byte alarmLevel) {
		this.alarmLevel = alarmLevel;
	}
    
    
}
