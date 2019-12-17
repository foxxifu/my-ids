package com.interest.ids.biz.web.station.DTO;

import java.io.Serializable;

public class StationDevDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String stationCode;
	private String esn;
	private String devName;
	private String devModelVersion;
	/** 企业版本号 */
	private Long enterpriseId;
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public String getEsn() {
		return esn;
	}

	public void setEsn(String esn) {
		this.esn = esn;
	}

	public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}

	public String getDevModelVersion() {
		return devModelVersion;
	}

	public void setDevModelVersion(String devModelVersion) {
		this.devModelVersion = devModelVersion;
	}
}
