package com.interest.ids.dev.alarm.analysis.bean;

/**
 * 
 * @author lhq
 * 低效分析
 *
 */
public class AnalysisInefficientBean {
	
	 public Long devId;

	 public String sid;

	 public Double dayCap;
	    // 位置ID
	 public String locId;
	    // 等效利用小时数（PPR）
	 private Double perpowerRatio;
	public Long getDevId() {
		return devId;
	}
	public void setDevId(Long devId) {
		this.devId = devId;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public Double getDayCap() {
		return dayCap;
	}
	public void setDayCap(Double dayCap) {
		this.dayCap = dayCap;
	}
	public String getLocId() {
		return locId;
	}
	public void setLocId(String locId) {
		this.locId = locId;
	}
	public Double getPerpowerRatio() {
		return perpowerRatio;
	}
	public void setPerpowerRatio(Double perpowerRatio) {
		this.perpowerRatio = perpowerRatio;
	}
}
