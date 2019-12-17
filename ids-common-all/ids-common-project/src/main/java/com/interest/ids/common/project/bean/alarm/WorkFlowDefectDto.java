package com.interest.ids.common.project.bean.alarm;

import java.io.Serializable;
import java.util.List;

import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.DevTypeConstant;

public class WorkFlowDefectDto implements Serializable
{
	private String createUserName;
	/**
	 * 告警名称
	 */
	private String alarmNames;
	/**
	 * 查询的类型
	 */
	private String queryType;
	/**
	 * 当前任务id
	 */
	private String currentTaskId;
	/**
	 * 操作状态
	 * 1 : 同意
	 * 2 : 不同意
	 */
	private String optType;
    /**
     * 接收人
     */
    private String reviceUserName;
    /**
     * 操作描述
     */
    private String operationDesc;
    /**
     * 当前处理人
     */
    private String currentUserName;
    /**
     * 任务内容
     */
    private String taskContent;
    /**
     * 分页的数据 分别是 页数 和 每页显示条数 start
     */
    private Integer index;
    private Integer pageSize;
    /** 
     * 分页的数据 分别是 页数 和 每页显示条数 end
     */
    private Long userId;
    private String type_;
    /**任务id*/
    private String taskId; 
    private Integer reback ;
    private Integer notsure;
    private Integer waiting;
    private Integer dealing;
    private Integer today;
    private Integer finished;
    private Long taskStartTime;
    /**
     * 执行任务的状态
     */
    private String taskState;
    private String transferorName;
    private Integer devTypeId;
    
    public Integer getFinished() {
		return finished;
	}

	public void setFinished(Integer finished) {
		this.finished = finished;
	}

	public Integer getToday() {
		return today;
	}

	public void setToday(Integer today) {
		this.today = today;
	}

	public Integer getDevTypeId() {
		return devTypeId;
	}

	public void setDevTypeId(Integer devTypeId) {
		this.devTypeId = devTypeId;
		if(null != devTypeId)
		{
			this.devTypeName = DevTypeConstant.DEV_TYPE_I18N_ID.get(devTypeId);
		}
	}

	public String getTransferorName() {
		return transferorName;
	}

	public void setTransferorName(String transferorName) {
		this.transferorName = transferorName;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getAlarmNames() {
		return alarmNames;
	}

	public void setAlarmNames(String alarmNames) {
		this.alarmNames = alarmNames;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
     * 任务的当前处理人的id
     */
    private Long currentUserId;
    /**
     * 当前缺陷的所有的缺陷
     */
    private List<WorkFlowTaskM> tasks;
    
    public String getType_() {
		return type_;
	}

	public void setType_(String type_) {
		this.type_ = type_;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getCurrentTaskId() {
		return currentTaskId;
	}

	public void setCurrentTaskId(String currentTaskId) {
		this.currentTaskId = currentTaskId;
	}

	public String getOptType() {
		return optType;
	}

	public void setOptType(String optType) {
		this.optType = optType;
	}

	public String getReviceUserName() {
        return reviceUserName;
    }

    public void setReviceUserName(String reviceUserName) {
        this.reviceUserName = reviceUserName;
    }

    public String getOperationDesc() {
        return operationDesc;
    }

    public void setOperationDesc(String operationDesc) {
        this.operationDesc = operationDesc;
    }

    public Long getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(Long taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    public Long getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(Long currentUserId) {
        this.currentUserId = currentUserId;
    }

    public List<WorkFlowTaskM> getTasks() {
        return tasks;
    }

    public void setTasks(List<WorkFlowTaskM> tasks) {
        this.tasks = tasks;
    }
    /**
     * 设备的名称
     */
    private String devAlias;
    /**
     * 总数
     */
    private Integer count;
    /**
     * 发生开始时间
     */
    private Long startFindTime;
    private Long endFindTime;

    public String getDevAlias() {
		return devAlias;
	}

	public void setDevAlias(String devAlias) {
		this.devAlias = devAlias;
	}

	public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getStartFindTime() {
        return startFindTime;
    }

    public void setStartFindTime(Long startFindTime) {
        this.startFindTime = startFindTime;
    }

    public Long getEndFindTime() {
        return endFindTime;
    }

    public void setEndFindTime(Long endFindTime) {
        this.endFindTime = endFindTime;
    }

    private static final long serialVersionUID = 1L;
    
    public Integer getReback() {
        return reback;
    }

    public void setReback(Integer reback) {
        this.reback = reback;
    }

    public Integer getNotsure() {
        return notsure;
    }

    public void setNotsure(Integer notsure) {
        this.notsure = notsure;
    }

    public Integer getWaiting() {
        return waiting;
    }

    public void setWaiting(Integer waiting) {
        this.waiting = waiting;
    }

    public Integer getDealing() {
        return dealing;
    }

    public void setDealing(Integer dealing) {
        this.dealing = dealing;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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
    
    private String devTypeName;

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
    private String alarmNum;

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
    
    private String originalName; // 文件的原文件名
    
    private String fileExt; // 文件的后缀

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

    public String getFileExt() {
		return fileExt;
	}

	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public String getAlarmIds() {
		return alarmIds;
	}

	public void setAlarmIds(String alarmIds) {
		this.alarmIds = alarmIds;
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

    public String getDevTypeName() {
		return devTypeName;
	}

	public void setDevTypeName(String devTypeName) {
		this.devTypeName = devTypeName;
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
