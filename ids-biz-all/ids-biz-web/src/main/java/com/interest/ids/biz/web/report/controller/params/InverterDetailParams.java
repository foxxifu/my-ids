package com.interest.ids.biz.web.report.controller.params;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * 逆变器详细报表查询参数
 * @author zengl
 *
 */
public class InverterDetailParams implements Serializable {
	private static final long serialVersionUID = 4148729876971449580L;
	@NotNull
	private String stationCode; // 电站Id，单电站
	private Long beginTime; // 采集时间
	private Long endTime;
	private List<String> deviceIds = new ArrayList<String>(); // 需要查询的设备ids
	private List<String> kpis = new ArrayList<String>(); // 需要展示的KPI指标
	public String getStationCode() {
		return stationCode;
	}
	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}
	public List<String> getDeviceIds() {
		return deviceIds;
	}
	public void setDeviceIds(List<String> deviceIds) {
		this.deviceIds = deviceIds;
	}
	public List<String> getKpis() {
		return kpis;
	}
	public void setKpis(List<String> kpis) {
		this.kpis = kpis;
	}
	public Long getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Long beginTime) {
		this.beginTime = beginTime;
	}
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
}
