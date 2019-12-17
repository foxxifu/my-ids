package com.interest.ids.biz.web.report.vo;

import java.io.Serializable;

/**
 * 子阵运行日、月、年报表抽象
 * 
 * @author zengls
 * @date 2018-08-07
 */
public class SubarrayReportVO implements Serializable {

    private static final long serialVersionUID = 403245470883242892L;

    /**
     * 采集时间
     */
    private Long collectTime;

    private String stationName;
    
    private String stationCode;
    
    private String matrixId;
    
    private String subarrayName;
    
    private Double realCapacity;

    private Double productPower;
    
    private Double perPowerRatio;
    
    /**
     * 峰值功率
     */
    private Double peakPower;

	public Long getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(Long collectTime) {
		this.collectTime = collectTime;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getSubarrayName() {
		return subarrayName;
	}

	public void setSubarrayName(String subarrayName) {
		this.subarrayName = subarrayName;
	}

	public Double getPeakPower() {
		return peakPower;
	}

	public void setPeakPower(Double peakPower) {
		this.peakPower = peakPower;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public String getMatrixId() {
		return matrixId;
	}

	public void setMatrixId(String matrixId) {
		this.matrixId = matrixId;
	}

	public Double getRealCapacity() {
		return realCapacity;
	}

	public void setRealCapacity(Double realCapacity) {
		this.realCapacity = realCapacity;
	}

	public Double getProductPower() {
		return productPower;
	}

	public void setProductPower(Double productPower) {
		this.productPower = productPower;
	}

	public Double getPerPowerRatio() {
		return perPowerRatio;
	}

	public void setPerPowerRatio(Double perPowerRatio) {
		this.perPowerRatio = perPowerRatio;
	}
}
