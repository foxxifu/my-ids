package com.interest.ids.biz.web.station.DTO;

import java.io.Serializable;

public class StationShareemiDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	// 共享的电站编码
	private String shareStationCode;

	// 无环境检测仪的电站编号
	private String stationCode;

	// 共享环境检测仪设备id
	private Long shareDeviceId;

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getShareStationCode() {
		return shareStationCode;
	}

	public void setShareStationCode(String shareStationCode) {
		this.shareStationCode = shareStationCode;
	}

	public Long getShareDeviceId() {
		return shareDeviceId;
	}

	public void setShareDeviceId(Long shareDeviceId) {
		this.shareDeviceId = shareDeviceId;
	}

}
