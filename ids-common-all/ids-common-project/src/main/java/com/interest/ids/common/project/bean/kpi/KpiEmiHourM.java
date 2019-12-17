package com.interest.ids.common.project.bean.kpi;

import com.interest.ids.common.project.bean.KpiBaseModel;

public class KpiEmiHourM extends KpiBaseModel {

    private static final long serialVersionUID = -4902167539876787971L;

    private String devName;
    
    private Double temperature;
    
    private Double pvTemperature;
    
    private Double windSpeed;
    
    private Double totalRadiant;
    
    private Double horizRadiant;
    
    private Double maxRadiantPoint;
    
    private Double minRadiantPoint;

    private Long statisticsTime;

    public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}

	public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getPvTemperature() {
        return pvTemperature;
    }

    public void setPvTemperature(Double pvTemperature) {
        this.pvTemperature = pvTemperature;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Double getTotalRadiant() {
        return totalRadiant;
    }

    public void setTotalRadiant(Double totalRadiant) {
        this.totalRadiant = totalRadiant;
    }

    public Double getHorizRadiant() {
        return horizRadiant;
    }

    public void setHorizRadiant(Double horizRadiant) {
        this.horizRadiant = horizRadiant;
    }

    public Double getMaxRadiantPoint() {
        return maxRadiantPoint;
    }

    public void setMaxRadiantPoint(Double maxRadiantPoint) {
        this.maxRadiantPoint = maxRadiantPoint;
    }

    public Double getMinRadiantPoint() {
        return minRadiantPoint;
    }

    public void setMinRadiantPoint(Double minRadiantPoint) {
        this.minRadiantPoint = minRadiantPoint;
    }

    public Long getStatisticsTime() {
        return statisticsTime;
    }

    public void setStatisticsTime(Long statisticsTime) {
        this.statisticsTime = statisticsTime;
    }
}