package com.interest.ids.common.project.bean.alarm;

import java.util.List;

public class WorkFlowDefectM 
{
    private WorkFlowProcessM process; // 当前缺陷的处理进度
    private List<WorkFlowTaskM> tasks;
    private String taskContent;
    private String taskState;
    
    public String getTaskState() {
		return taskState;
	}

	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}

	public String getTaskContent() {
		return taskContent;
	}

	public void setTaskContent(String taskContent) {
		this.taskContent = taskContent;
	}

	public List<WorkFlowTaskM> getTasks() {
        return tasks;
    }

    public void setTasks(List<WorkFlowTaskM> tasks) {
        this.tasks = tasks;
    }

    public WorkFlowProcessM getProcess() {
        return process;
    }

    public void setProcess(WorkFlowProcessM process) {
        this.process = process;
    }

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_defect_t.defect_id
     *
     * @mbggenerated
     */
    private Long defectId;
    
    private String defectName;
    private String alarmIds;
    private String alarmNum;
    private String createUserName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_defect_t.defect_code
     *
     * @mbggenerated
     */
    private String defectCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_defect_t.device_id
     *
     * @mbggenerated
     */
    private String devId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_defect_t.defect_grade
     *
     * @mbggenerated
     */
    private String defectGrade;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_defect_t.defect_desc
     *
     * @mbggenerated
     */
    private String description;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_defect_t.proc_id
     *
     * @mbggenerated
     */
    private String procId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_defect_t.proc_state
     *
     * @mbggenerated
     */
    private String procState;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_defect_t.deal_result
     *
     * @mbggenerated
     */
    private String dealResult;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_defect_t.device_type
     *
     * @mbggenerated
     */
    private String devType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_defect_t.device_vender
     *
     * @mbggenerated
     */
    private String devVender;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_defect_t.device_version
     *
     * @mbggenerated
     */
    private String devVersion;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_defect_t.fault_type
     *
     * @mbggenerated
     */
    private String faultType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_defect_t.alarm_num
     *
     * @mbggenerated
     */

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_defect_t.alarm_type
     *
     * @mbggenerated
     */
    private String alarmType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_defect_t.station_code
     *
     * @mbggenerated
     */
    private String stationCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_defect_t.station_name
     *
     * @mbggenerated
     */
    private String stationName;
    private String stationAddr;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_defect_t.enterprise_id
     *
     * @mbggenerated
     */
    private Long enterpriseId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_defect_t.file_id
     *
     * @mbggenerated
     */
    private String fileId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_defect_t.owner_id
     *
     * @mbggenerated
     */
    private Long createUserId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_defect_t.create_time
     *
     * @mbggenerated
     */
    private Long createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_defect_t.update_time
     *
     * @mbggenerated
     */
    private Long updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_defect_t.start_time
     *
     * @mbggenerated
     */
    private Long startTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_defect_t.station_start_time
     *
     * @mbggenerated
     */
    private Long stationStartTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_defect_t.end_time
     *
     * @mbggenerated
     */
    private Long endTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_defect_t.time_zone
     *
     * @mbggenerated
     */
    private Integer timeZone;

    public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_defect_t.defect_id
     *
     * @return the value of ids_workflow_defect_t.defect_id
     *
     * @mbggenerated
     */
    public Long getDefectId() {
        return defectId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_defect_t.defect_id
     *
     * @param defectId the value for ids_workflow_defect_t.defect_id
     *
     * @mbggenerated
     */
    public void setDefectId(Long defectId) {
        this.defectId = defectId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_defect_t.defect_code
     *
     * @return the value of ids_workflow_defect_t.defect_code
     *
     * @mbggenerated
     */
    public String getDefectCode() {
        return defectCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_defect_t.defect_code
     *
     * @param defectCode the value for ids_workflow_defect_t.defect_code
     *
     * @mbggenerated
     */
    public void setDefectCode(String defectCode) {
        this.defectCode = defectCode == null ? null : defectCode.trim();
    }

    public String getDevId() {
		return devId;
	}

	public void setDevId(String devId) {
		this.devId = devId;
	}

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_defect_t.defect_grade
     *
     * @return the value of ids_workflow_defect_t.defect_grade
     *
     * @mbggenerated
     */
    public String getDefectGrade() {
        return defectGrade;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_defect_t.defect_grade
     *
     * @param defectGrade the value for ids_workflow_defect_t.defect_grade
     *
     * @mbggenerated
     */
    public void setDefectGrade(String defectGrade) {
        this.defectGrade = defectGrade == null ? null : defectGrade.trim();
    }
    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_defect_t.proc_id
     *
     * @return the value of ids_workflow_defect_t.proc_id
     *
     * @mbggenerated
     */
    public String getProcId() {
        return procId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_defect_t.proc_id
     *
     * @param procId the value for ids_workflow_defect_t.proc_id
     *
     * @mbggenerated
     */
    public void setProcId(String procId) {
        this.procId = procId == null ? null : procId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_defect_t.proc_state
     *
     * @return the value of ids_workflow_defect_t.proc_state
     *
     * @mbggenerated
     */
    public String getProcState() {
        return procState;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_defect_t.proc_state
     *
     * @param procState the value for ids_workflow_defect_t.proc_state
     *
     * @mbggenerated
     */
    public void setProcState(String procState) {
        this.procState = procState == null ? null : procState.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_defect_t.deal_result
     *
     * @return the value of ids_workflow_defect_t.deal_result
     *
     * @mbggenerated
     */
    public String getDealResult() {
        return dealResult;
    }

    public String getAlarmIds() {
		return alarmIds;
	}

	public void setAlarmIds(String alarmIds) {
		this.alarmIds = alarmIds;
	}

	/**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_defect_t.deal_result
     *
     * @param dealResult the value for ids_workflow_defect_t.deal_result
     *
     * @mbggenerated
     */
    public void setDealResult(String dealResult) {
        this.dealResult = dealResult == null ? null : dealResult.trim();
    }

    public String getDevType() {
		return devType;
	}

	public void setDevType(String devType) {
		this.devType = devType;
	}

    public String getDevVender() {
		return devVender;
	}

	public void setDevVender(String devVender) {
		this.devVender = devVender;
	}

    public String getDevVersion() {
		return devVersion;
	}

	public void setDevVersion(String devVersion) {
		this.devVersion = devVersion;
	}

	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_defect_t.fault_type
     *
     * @return the value of ids_workflow_defect_t.fault_type
     *
     * @mbggenerated
     */
    public String getFaultType() {
        return faultType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_defect_t.fault_type
     *
     * @param faultType the value for ids_workflow_defect_t.fault_type
     *
     * @mbggenerated
     */
    public void setFaultType(String faultType) {
        this.faultType = faultType == null ? null : faultType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_defect_t.alarm_num
     *
     * @return the value of ids_workflow_defect_t.alarm_num
     *
     * @mbggenerated
     */
    public String getAlarmNum() {
        return alarmNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_defect_t.alarm_num
     *
     * @param alarmNum the value for ids_workflow_defect_t.alarm_num
     *
     * @mbggenerated
     */
    public void setAlarmNum(String alarmNum) {
        this.alarmNum = alarmNum == null ? null : alarmNum.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_defect_t.alarm_type
     *
     * @return the value of ids_workflow_defect_t.alarm_type
     *
     * @mbggenerated
     */
    public String getAlarmType() {
        return alarmType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_defect_t.alarm_type
     *
     * @param alarmType the value for ids_workflow_defect_t.alarm_type
     *
     * @mbggenerated
     */
    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType == null ? null : alarmType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_defect_t.station_code
     *
     * @return the value of ids_workflow_defect_t.station_code
     *
     * @mbggenerated
     */
    public String getStationCode() {
        return stationCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_defect_t.station_code
     *
     * @param stationCode the value for ids_workflow_defect_t.station_code
     *
     * @mbggenerated
     */
    public void setStationCode(String stationCode) {
        this.stationCode = stationCode == null ? null : stationCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_defect_t.station_name
     *
     * @return the value of ids_workflow_defect_t.station_name
     *
     * @mbggenerated
     */
    public String getStationName() {
        return stationName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_defect_t.station_name
     *
     * @param stationName the value for ids_workflow_defect_t.station_name
     *
     * @mbggenerated
     */
    public void setStationName(String stationName) {
        this.stationName = stationName == null ? null : stationName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_defect_t.enterprise_id
     *
     * @return the value of ids_workflow_defect_t.enterprise_id
     *
     * @mbggenerated
     */
    public Long getEnterpriseId() {
        return enterpriseId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_defect_t.enterprise_id
     *
     * @param enterpriseId the value for ids_workflow_defect_t.enterprise_id
     *
     * @mbggenerated
     */
    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_defect_t.file_id
     *
     * @return the value of ids_workflow_defect_t.file_id
     *
     * @mbggenerated
     */
    public String getFileId() {
        return fileId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_defect_t.file_id
     *
     * @param fileId the value for ids_workflow_defect_t.file_id
     *
     * @mbggenerated
     */
    public void setFileId(String fileId) {
        this.fileId = fileId == null ? null : fileId.trim();
    }


    public String getDefectName() {
		return defectName;
	}

	public void setDefectName(String defectName) {
		this.defectName = defectName;
	}

	public String getStationAddr() {
		return stationAddr;
	}

	public void setStationAddr(String stationAddr) {
		this.stationAddr = stationAddr;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_defect_t.create_time
     *
     * @return the value of ids_workflow_defect_t.create_time
     *
     * @mbggenerated
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_defect_t.create_time
     *
     * @param createTime the value for ids_workflow_defect_t.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_defect_t.update_time
     *
     * @return the value of ids_workflow_defect_t.update_time
     *
     * @mbggenerated
     */
    public Long getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_defect_t.update_time
     *
     * @param updateTime the value for ids_workflow_defect_t.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_defect_t.start_time
     *
     * @return the value of ids_workflow_defect_t.start_time
     *
     * @mbggenerated
     */
    public Long getStartTime() {
        return startTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_defect_t.start_time
     *
     * @param startTime the value for ids_workflow_defect_t.start_time
     *
     * @mbggenerated
     */
    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_defect_t.station_start_time
     *
     * @return the value of ids_workflow_defect_t.station_start_time
     *
     * @mbggenerated
     */
    public Long getStationStartTime() {
        return stationStartTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_defect_t.station_start_time
     *
     * @param stationStartTime the value for ids_workflow_defect_t.station_start_time
     *
     * @mbggenerated
     */
    public void setStationStartTime(Long stationStartTime) {
        this.stationStartTime = stationStartTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_defect_t.end_time
     *
     * @return the value of ids_workflow_defect_t.end_time
     *
     * @mbggenerated
     */
    public Long getEndTime() {
        return endTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_defect_t.end_time
     *
     * @param endTime the value for ids_workflow_defect_t.end_time
     *
     * @mbggenerated
     */
    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_defect_t.time_zone
     *
     * @return the value of ids_workflow_defect_t.time_zone
     *
     * @mbggenerated
     */
    public Integer getTimeZone() {
        return timeZone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_defect_t.time_zone
     *
     * @param timeZone the value for ids_workflow_defect_t.time_zone
     *
     * @mbggenerated
     */
    public void setTimeZone(Integer timeZone) {
        this.timeZone = timeZone;
    }
}