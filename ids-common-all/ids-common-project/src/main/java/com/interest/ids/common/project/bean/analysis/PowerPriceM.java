package com.interest.ids.common.project.bean.analysis;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 电站分时电价表
 * 
 * @author claude
 *
 */
@Table(name = "ids_power_price_t")
public class PowerPriceM implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@Id
	private String id;

	/**
	 * 企业id
	 */
	@Column(name = "enterprise_id")
	private Long enterpriseId;

	/**
	 * 区域id
	 */
	@Column(name = "domain_id")
	private Long domainId;

	/**
	 * 电站编号
	 */
	@Column(name = "station_code")
	private String stationCode;

	/**
	 * 分时电价开始日期（时间戳）
	 */
	@Column(name = "start_date")
	private long startDate;

	/**
	 * 分时电价结束日期（时间戳）
	 */
	@Column(name = "end_date")
	private long endDate;

	/**
	 * 分时电价开始时间（整点小时数）
	 */
	@Column(name = "start_time")
	private int startTime;

	/**
	 * 分时电价结束时间（整点小时数）
	 */
	@Column(name = "end_time")
	private int endTime;

	/**
	 * 电价
	 */
	@Column(name = "price")
	private Double price;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Long getDomainId() {
		return domainId;
	}

	public void setDomainId(Long domainId) {
		this.domainId = domainId;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public long getStartDate() {
		return startDate;
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	public long getEndDate() {
		return endDate;
	}

	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

}
