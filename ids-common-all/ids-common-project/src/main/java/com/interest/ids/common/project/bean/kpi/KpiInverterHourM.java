package com.interest.ids.common.project.bean.kpi;

import com.interest.ids.common.project.bean.KpiBaseModel;

public class KpiInverterHourM extends KpiBaseModel {

    private static final long serialVersionUID = -9079373738716272132L;

    private String devName;

    private Integer inverterType;

    private Double productPower;

    private Double totalPower;

    private Double peakPower;

    private Double efficiency;

    private Integer aocConnNum;

    private Integer aopNumZero;

    private Integer aopNumOne;

    private Long statisticsTime;

    public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}

	public Integer getInverterType() {
        return inverterType;
    }

    public void setInverterType(Integer inverterType) {
        this.inverterType = inverterType;
    }

    public Double getProductPower() {
        return productPower;
    }

    public void setProductPower(Double productPower) {
        this.productPower = productPower;
    }

    public Double getTotalPower() {
        return totalPower;
    }

    public void setTotalPower(Double totalPower) {
        this.totalPower = totalPower;
    }

    public Double getPeakPower() {
        return peakPower;
    }

    public void setPeakPower(Double peakPower) {
        this.peakPower = peakPower;
    }

    public Double getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(Double efficiency) {
        this.efficiency = efficiency;
    }

    public Integer getAocConnNum() {
        return aocConnNum;
    }

    public void setAocConnNum(Integer aocConnNum) {
        this.aocConnNum = aocConnNum;
    }

    public Integer getAopNumZero() {
        return aopNumZero;
    }

    public void setAopNumZero(Integer aopNumZero) {
        this.aopNumZero = aopNumZero;
    }

    public Integer getAopNumOne() {
        return aopNumOne;
    }

    public void setAopNumOne(Integer aopNumOne) {
        this.aopNumOne = aopNumOne;
    }

    public Long getStatisticsTime() {
        return statisticsTime;
    }

    public void setStatisticsTime(Long statisticsTime) {
        this.statisticsTime = statisticsTime;
    }
}