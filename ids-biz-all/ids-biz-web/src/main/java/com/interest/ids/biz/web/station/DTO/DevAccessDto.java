package com.interest.ids.biz.web.station.DTO;

import java.io.Serializable;

public class DevAccessDto implements Serializable {

	private static final long serialVersionUID = 1L;

	// 设备id
	private Long id;

	// 电站编号
	private String stationCode;

	// sn号
	private String snCode;

	// 设备名称
	private String devName;

	// 信号点版本
	private String signalVersion;

	// 父设备id
	private Long parentId;

	// 父设备sn
	private String parentSn;

	// 二级地址
	private Integer secondAddress;

	// 设备类型
	private Integer devTypeId;

	// 设备类型名称
	private String devTypeName;

	// 电站名称
	private String stationName;

	// 经度
	private Double longitude;

	// 纬度
	private Double latitude;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public String getSnCode() {
		return snCode;
	}

	public void setSnCode(String snCode) {
		this.snCode = snCode;
	}

	public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}

	public String getSignalVersion() {
		return signalVersion;
	}

	public void setSignalVersion(String signalVersion) {
		this.signalVersion = signalVersion;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentSn() {
		return parentSn;
	}

	public void setParentSn(String parentSn) {
		this.parentSn = parentSn;
	}

	public Integer getSecondAddress() {
		return secondAddress;
	}

	public void setSecondAddress(Integer secondAddress) {
		this.secondAddress = secondAddress;
	}

	public Integer getDevTypeId() {
		return devTypeId;
	}

	public void setDevTypeId(Integer devTypeId) {
		this.devTypeId = devTypeId;
	}

	public String getDevTypeName() {
		return devTypeName;
	}

	public void setDevTypeName(String devTypeName) {
		this.devTypeName = devTypeName;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

}
