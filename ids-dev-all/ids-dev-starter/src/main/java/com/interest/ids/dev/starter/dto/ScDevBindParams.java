package com.interest.ids.dev.starter.dto;

/**
 * 数采设备绑定的参数信息
 * @author wq
 *
 */
public class ScDevBindParams {
	/**
	 * 数采设备ID
	 */
	private Long scDevId;
	/**
	 * 需要绑定到的电站编号
	 */
	private String stationCode;
	
	public Long getScDevId() {
		return scDevId;
	}
	public void setScDevId(Long scDevId) {
		this.scDevId = scDevId;
	}
	public String getStationCode() {
		return stationCode;
	}
	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}
	
}
