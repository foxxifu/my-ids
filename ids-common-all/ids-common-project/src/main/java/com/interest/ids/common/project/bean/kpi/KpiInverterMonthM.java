package com.interest.ids.common.project.bean.kpi;

import com.interest.ids.common.project.bean.KpiBaseModel;

public class KpiInverterMonthM extends KpiBaseModel {

    private static final long serialVersionUID = 5045035660654807713L;

    private String devName;

    private Integer inverterType;

    private Double realCapacity;

    private Double productPower;

    private Double equivalentHour;

    private Double peakPower;

    private Double efficiency;

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

    public Double getEquivalentHour() {
        return equivalentHour;
    }

    public void setEquivalentHour(Double equivalentHour) {
        this.equivalentHour = equivalentHour;
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

    public Long getStatisticsTime() {
        return statisticsTime;
    }

    public void setStatisticsTime(Long statisticsTime) {
        this.statisticsTime = statisticsTime;
    }
}