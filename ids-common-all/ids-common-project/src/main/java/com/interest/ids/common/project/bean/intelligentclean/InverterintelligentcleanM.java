package com.interest.ids.common.project.bean.intelligentclean;

import java.io.Serializable;

import javax.persistence.Column;

public class InverterintelligentcleanM implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name = "station_name")
	private String stationName;
	@Column(name = "station_code")
	private String stationCode;
	@Column(name = "array_name")
	private String arrayName;
	@Column(name = "array_code")
	private String arrayCode;
	@Column(name = "dev_id")
	private Long devId;
	@Column(name = "capacity")
	private Double capacity;
	@Column(name = "base_pr")
	private Double basePr;
	@Column(name = "current_pr")
	private Double currentPr;
	@Column(name = "power_profit_thirty")
	private Double powerProfitThirty;
	@Column(name = "power_profit_sixty")
	private Double powerProfitSixty;

	@Column(name = "power_profit_thirty_ratio")
	private Double powerProfitThirtyRatio;
	@Column(name = "power_profit_sixty_ratio")
	private Double powerProfitSixtyRatio;

	@Column(name = "start_time")
	private Long startTime;
	@Column(name = "end_time")
	private Long endTime;

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public String getArrayName() {
		return arrayName;
	}

	public void setArrayName(String arrayName) {
		this.arrayName = arrayName;
	}

	public String getArrayCode() {
		return arrayCode;
	}

	public void setArrayCode(String arrayCode) {
		this.arrayCode = arrayCode;
	}

	public Long getDevId() {
		return devId;
	}

	public void setDevId(Long devId) {
		this.devId = devId;
	}

	public Double getCapacity() {
		return capacity;
	}

	public void setCapacity(Double capacity) {
		this.capacity = capacity;
	}

	public Double getBasePr() {
		return basePr;
	}

	public void setBasePr(Double basePr) {
		this.basePr = basePr;
	}

	public Double getCurrentPr() {
		return currentPr;
	}

	public void setCurrentPr(Double currentPr) {
		this.currentPr = currentPr;
	}

	public Double getPowerProfitThirty() {
		return powerProfitThirty;
	}

	public void setPowerProfitThirty(Double powerProfitThirty) {
		this.powerProfitThirty = powerProfitThirty;
	}

	public Double getPowerProfitSixty() {
		return powerProfitSixty;
	}

	public void setPowerProfitSixty(Double powerProfitSixty) {
		this.powerProfitSixty = powerProfitSixty;
	}

	public Double getPowerProfitThirtyRatio() {
		return powerProfitThirtyRatio;
	}

	public void setPowerProfitThirtyRatio(Double powerProfitThirtyRatio) {
		this.powerProfitThirtyRatio = powerProfitThirtyRatio;
	}

	public Double getPowerProfitSixtyRatio() {
		return powerProfitSixtyRatio;
	}

	public void setPowerProfitSixtyRatio(Double powerProfitSixtyRatio) {
		this.powerProfitSixtyRatio = powerProfitSixtyRatio;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

}
