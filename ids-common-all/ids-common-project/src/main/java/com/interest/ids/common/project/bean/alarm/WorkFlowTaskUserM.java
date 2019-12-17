package com.interest.ids.common.project.bean.alarm;

public class WorkFlowTaskUserM {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_task_user_t.id
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_task_user_t.proc_id
     *
     * @mbggenerated
     */
    private String procId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_task_user_t.task_key
     *
     * @mbggenerated
     */
    private String taskId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_task_user_t.user_id
     *
     * @mbggenerated
     */
    private Long userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_task_user_t.station_code
     *
     * @mbggenerated
     */
    private String stationCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ids_workflow_task_user_t.enterprise_id
     *
     * @mbggenerated
     */
    private Long enterpriseId;

    public String getProcId() {
		return procId;
	}

	public void setProcId(String procId) {
		this.procId = procId;
	}

	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_task_user_t.id
     *
     * @return the value of ids_workflow_task_user_t.id
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_task_user_t.id
     *
     * @param id the value for ids_workflow_task_user_t.id
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_task_user_t.user_id
     *
     * @return the value of ids_workflow_task_user_t.user_id
     *
     * @mbggenerated
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_task_user_t.user_id
     *
     * @param userId the value for ids_workflow_task_user_t.user_id
     *
     * @mbggenerated
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_task_user_t.station_code
     *
     * @return the value of ids_workflow_task_user_t.station_code
     *
     * @mbggenerated
     */
    public String getStationCode() {
        return stationCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_task_user_t.station_code
     *
     * @param stationCode the value for ids_workflow_task_user_t.station_code
     *
     * @mbggenerated
     */
    public void setStationCode(String stationCode) {
        this.stationCode = stationCode == null ? null : stationCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ids_workflow_task_user_t.enterprise_id
     *
     * @return the value of ids_workflow_task_user_t.enterprise_id
     *
     * @mbggenerated
     */
    public Long getEnterpriseId() {
        return enterpriseId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ids_workflow_task_user_t.enterprise_id
     *
     * @param enterpriseId the value for ids_workflow_task_user_t.enterprise_id
     *
     * @mbggenerated
     */
    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }
}