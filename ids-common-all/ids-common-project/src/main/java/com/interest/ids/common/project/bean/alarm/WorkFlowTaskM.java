package com.interest.ids.common.project.bean.alarm;

public class WorkFlowTaskM {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_task_t.id
     *
     * @mbggenerated
     */
    private Long id;
    
    private String fileId;
    /**上传文件的原始名字*/
    private String originalName;
    /**上传文件的后缀*/
    private String fileExt;
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_task_t.task_id
     *
     * @mbggenerated
     */
    private String taskId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_task_t.pre_task_id
     *
     * @mbggenerated
     */
    private String preTaskId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_task_t.task_key
     *
     * @mbggenerated
     */
    private String taskKey;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_task_t.proc_id
     *
     * @mbggenerated
     */
    private String procId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_task_t.task_start_time
     *
     * @mbggenerated
     */
    private Long taskStartTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_task_t.task_end_time
     *
     * @mbggenerated
     */
    private Long taskEndTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_task_t.task_state
     *
     * @mbggenerated
     */
    private String taskState;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_task_t.task_content
     *
     * @mbggenerated
     */
    private String taskContent;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_task_t.assignee
     *
     * @mbggenerated
     */
    private String transferorName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_task_t.assignee_name
     *
     * @mbggenerated
     */
    private String assigneeName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_task_t.op_state
     *
     * @mbggenerated
     */
    private String opState;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_task_t.opDesc
     *
     * @mbggenerated
     */
    private String opDesc;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_task_t.station_code
     *
     * @mbggenerated
     */
    private String stationCode;

    public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_task_t.enterprise_id
     *
     * @mbggenerated
     */
    private Long enterpriseId;

    public String getFileExt() {
		return fileExt;
	}

	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_task_t.id
     *
     * @return the value of ids_workflow_task_t.id
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_task_t.id
     *
     * @param id the value for ids_workflow_task_t.id
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_task_t.task_id
     *
     * @return the value of ids_workflow_task_t.task_id
     *
     * @mbggenerated
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_task_t.task_id
     *
     * @param taskId the value for ids_workflow_task_t.task_id
     *
     * @mbggenerated
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId == null ? null : taskId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_task_t.pre_task_id
     *
     * @return the value of ids_workflow_task_t.pre_task_id
     *
     * @mbggenerated
     */
    public String getPreTaskId() {
        return preTaskId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_task_t.pre_task_id
     *
     * @param preTaskId the value for ids_workflow_task_t.pre_task_id
     *
     * @mbggenerated
     */
    public void setPreTaskId(String preTaskId) {
        this.preTaskId = preTaskId == null ? null : preTaskId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_task_t.task_key
     *
     * @return the value of ids_workflow_task_t.task_key
     *
     * @mbggenerated
     */
    public String getTaskKey() {
        return taskKey;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_task_t.task_key
     *
     * @param taskKey the value for ids_workflow_task_t.task_key
     *
     * @mbggenerated
     */
    public void setTaskKey(String taskKey) {
        this.taskKey = taskKey == null ? null : taskKey.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_task_t.proc_id
     *
     * @return the value of ids_workflow_task_t.proc_id
     *
     * @mbggenerated
     */
    public String getProcId() {
        return procId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_task_t.proc_id
     *
     * @param procId the value for ids_workflow_task_t.proc_id
     *
     * @mbggenerated
     */
    public void setProcId(String procId) {
        this.procId = procId == null ? null : procId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_task_t.task_start_time
     *
     * @return the value of ids_workflow_task_t.task_start_time
     *
     * @mbggenerated
     */
    public Long getTaskStartTime() {
        return taskStartTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_task_t.task_start_time
     *
     * @param taskStartTime the value for ids_workflow_task_t.task_start_time
     *
     * @mbggenerated
     */
    public void setTaskStartTime(Long taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_task_t.task_end_time
     *
     * @return the value of ids_workflow_task_t.task_end_time
     *
     * @mbggenerated
     */
    public Long getTaskEndTime() {
        return taskEndTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_task_t.task_end_time
     *
     * @param taskEndTime the value for ids_workflow_task_t.task_end_time
     *
     * @mbggenerated
     */
    public void setTaskEndTime(Long taskEndTime) {
        this.taskEndTime = taskEndTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_task_t.task_state
     *
     * @return the value of ids_workflow_task_t.task_state
     *
     * @mbggenerated
     */
    public String getTaskState() {
        return taskState;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_task_t.task_state
     *
     * @param taskState the value for ids_workflow_task_t.task_state
     *
     * @mbggenerated
     */
    public void setTaskState(String taskState) {
        this.taskState = taskState == null ? null : taskState.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_task_t.task_content
     *
     * @return the value of ids_workflow_task_t.task_content
     *
     * @mbggenerated
     */
    public String getTaskContent() {
        return taskContent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_task_t.task_content
     *
     * @param taskContent the value for ids_workflow_task_t.task_content
     *
     * @mbggenerated
     */
    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent == null ? null : taskContent.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_task_t.assignee
     *
     * @return the value of ids_workflow_task_t.assignee
     *
     * @mbggenerated
     */
    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_task_t.assignee_name
     *
     * @return the value of ids_workflow_task_t.assignee_name
     *
     * @mbggenerated
     */
    public String getAssigneeName() {
        return assigneeName;
    }

    public String getTransferorName() {
		return transferorName;
	}

	public void setTransferorName(String transferorName) {
		this.transferorName = transferorName;
	}

	/**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_task_t.assignee_name
     *
     * @param assigneeName the value for ids_workflow_task_t.assignee_name
     *
     * @mbggenerated
     */
    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName == null ? null : assigneeName.trim();
    }

	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_task_t.operation
     *
     * @return the value of ids_workflow_task_t.operation
     *
     * @mbggenerated
     */
    public String getOpState() {
        return opState;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_task_t.operation
     *
     * @param operation the value for ids_workflow_task_t.operation
     *
     * @mbggenerated
     */
    public void setOpState(String opState) {
        this.opState = opState == null ? null : opState.trim();
    }

    public String getOpDesc() {
		return opDesc;
	}

	public void setOpDesc(String opDesc) {
		this.opDesc = opDesc;
	}

	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_task_t.station_code
     *
     * @return the value of ids_workflow_task_t.station_code
     *
     * @mbggenerated
     */
    public String getStationCode() {
        return stationCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_task_t.station_code
     *
     * @param stationCode the value for ids_workflow_task_t.station_code
     *
     * @mbggenerated
     */
    public void setStationCode(String stationCode) {
        this.stationCode = stationCode == null ? null : stationCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_task_t.enterprise_id
     *
     * @return the value of ids_workflow_task_t.enterprise_id
     *
     * @mbggenerated
     */
    public Long getEnterpriseId() {
        return enterpriseId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_task_t.enterprise_id
     *
     * @param enterpriseId the value for ids_workflow_task_t.enterprise_id
     *
     * @mbggenerated
     */
    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }
}