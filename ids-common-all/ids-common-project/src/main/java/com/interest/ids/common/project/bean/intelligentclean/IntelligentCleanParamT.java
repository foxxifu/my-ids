package com.interest.ids.common.project.bean.intelligentclean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "ids_sm_intelligentclean_param_t")
public class IntelligentCleanParamT implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	@Column(name = "param_key")
	private String paramKey;

	@Column(name = "param_name")
	private String paramName;

	@Column(name = "description")
	private String description;

	@Column(name = "param_value")
	private String paramValue;

	@Column(name = "param_unit")
	private String paramUnit;

	@Column(name = "param_type")
	private String paramType;

	@Column(name = "param_order")
	private Byte paramOrder;

	@Column(name = "station_code")
	private String stationCode;

	@Column(name = "enterprise_id")
	private Long enterpriseId;

	@Column(name = "modify_user_id")
	private Long modifyUserId;

	@Column(name = "modify_date")
	private Date modifyDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getParamKey() {
		return paramKey;
	}

	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getParamUnit() {
		return paramUnit;
	}

	public void setParamUnit(String paramUnit) {
		this.paramUnit = paramUnit;
	}

	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}

	public Byte getParamOrder() {
		return paramOrder;
	}

	public void setParamOrder(Byte paramOrder) {
		this.paramOrder = paramOrder;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Long getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(Long modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

}
