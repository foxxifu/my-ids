package com.interest.ids.biz.web.report.vo;

import java.io.Serializable;

/**
 * 电站逆变器报表，设备信息类
 * @author zengl
 *
 */
public class DeviceVO implements Serializable{
	private static final long serialVersionUID = -2308758239195929596L;
	private Long devId; // 设备Id
	private String devName; // 设备名称
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
}
