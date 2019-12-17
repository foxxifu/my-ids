package com.interest.ids.common.project.bean.device;

import javax.persistence.Column;
import javax.persistence.Table;

import com.interest.ids.common.project.bean.BaseBean;

/**
 * 数据补采的实体表
 * @author wq
 *
 */
@Table(name = "ids_data_mining_t")
public class DataMining extends BaseBean{
	private static final long serialVersionUID = 8980234811458777L;
	@Column(name = "dev_id")
	private Long devId; // bigint(16) NOT NULL COMMENT '设备id',
	@Column(name = "dev_name")
	private String devName; // varchar(64) NOT NULL COMMENT '设备名称,这里取不能修改的名称',
	@Column(name = "start_time")
	private Long startTime; // bigint(16) NOT NULL COMMENT '补采的开始时间',
	@Column(name = "end_time")
	private Long endTime; // bigint(16) NOT NULL COMMENT '补采的结束时间',
	@Column(name = "progress_num")
	private Integer progressNum; // int(3) DEFAULT 0 COMMENT '补采的进度',
	@Column(name = "exe_status")
	private Integer exeStatus; // int(2) DEFAULT 0 COMMENT '执行的结果，补采任务的状态 0:未补采, 1：补采中, 2：补采成功,  -1：补采失败',
	@Column(name = "collect_time")
	private Long collectTime; // bigint(16) DEFAULT NULL COMMENT '当前补采进行到的时间节点',
	@Column(name = "kpi_re_comp_status")
	private Integer kpiReCompStatus; // int(2) DEFAULT 0 COMMENT 'KPI重计算的状态  -1：失败，0：未开始，默认，1:成功',
	@Column(name = "create_time")
	private Long createTime; // bigint(16) DEFAULT NULL COMMENT '创建任务的时间',
	@Column(name = "station_code")
	private String stationCode; // 电站编号
	@Column(name = "station_name")
	private String stationName; // 电站名称
	
	public Long getDevId() {
		return devId;
	}
	public void setDevId(Long devId) {
		this.devId = devId;
	}
	public String getDevName() {
		return devName;
	}
	public void setDevName(String devName) {
		this.devName = devName;
	}
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	public Integer getProgressNum() {
		return progressNum;
	}
	public void setProgressNum(Integer progressNum) {
		this.progressNum = progressNum;
	}
	public Integer getExeStatus() {
		return exeStatus;
	}
	public void setExeStatus(Integer exeStatus) {
		this.exeStatus = exeStatus;
	}
	public Long getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(Long collectTime) {
		this.collectTime = collectTime;
	}
	public Integer getKpiReCompStatus() {
		return kpiReCompStatus;
	}
	public void setKpiReCompStatus(Integer kpiReCompStatus) {
		this.kpiReCompStatus = kpiReCompStatus;
	}
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	public String getStationCode() {
		return stationCode;
	}
	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	
}
