package com.interest.ids.common.project.dto;


/**
 * 
 * @author lhq
 *
 *
 */
public class SearchDeviceDto {
	
	private String ip;
	//二级地址
	private int unitId;
	//厂家
	private String vender;
	//数据采集器/棒型号
	private String model;
	//sn
	private String sn;
	//地理位置
	private String location;
	//父设备SN
	private String parentSn;


	public SearchDeviceDto() {
		super();
	}
	public SearchDeviceDto(String ip, int unitId, String vender, String model,
			String sn, String location,String parentSn) {
		super();
		this.ip = ip;
		this.unitId = unitId;
		this.vender = vender;
		this.model = model;
		this.sn = sn;
		this.location = location;
		this.parentSn = parentSn;
	}
	public SearchDeviceDto(String ip, int unitId, String vender, String model,
			String sn,String parentSn) {
		super();
		this.ip = ip;
		this.unitId = unitId;
		this.vender = vender;
		this.model = model;
		this.sn = sn;
		this.parentSn = parentSn;
	}
	

	public SearchDeviceDto(String ip, int unitId, String vender, String model,String sn) {
		super();
		this.ip = ip;
		this.unitId = unitId;
		this.vender = vender;
		this.model = model;
		this.sn = sn;
	}

	public String getVender() {
		return vender;
	}

	public void setVender(String vender) {
		this.vender = vender;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}


	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getUnitId() {
		return unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}

	public String getParentSn() {
		return parentSn;
	}

	public void setParentSn(String parentSn) {
		this.parentSn = parentSn;
	}
}
