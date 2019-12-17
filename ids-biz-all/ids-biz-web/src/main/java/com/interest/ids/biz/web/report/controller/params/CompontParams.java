package com.interest.ids.biz.web.report.controller.params;

import java.io.Serializable;

public class CompontParams implements Serializable{

	private static final long serialVersionUID = -4340540905240440545L;
	
	private String stationCode;
	private Long countTime;
	private Integer index;
	private Integer pageSize;
	private Long matrixId;
	private String type;
	private Long time;
	private String stationName;
	
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStationCode() {
		return stationCode;
	}
	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}
	public Long getCountTime() {
		return countTime;
	}
	public void setCountTime(Long countTime) {
		this.countTime = countTime;
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
	public Long getMatrixId() {
		return matrixId;
	}
	public void setMatrixId(Long matrixId) {
		this.matrixId = matrixId;
	}
}
