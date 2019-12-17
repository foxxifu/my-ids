package com.interest.ids.common.project.bean.kpi;

import com.interest.ids.common.project.bean.KpiBaseModel;

public class KpiMeterHourM extends KpiBaseModel {

    private static final long serialVersionUID = 6172096743537731356L;

    private String devName;
    
    private Integer meterType;
    
    private Double ongridPower;
    
    private Double buyPower;
    
    private Double validProductPower;

    private Double validConsumePower;
    
    private Long statisticsTime;

    public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}

	public Integer getMeterType() {
        return meterType;
    }

    public void setMeterType(Integer meterType) {
        this.meterType = meterType;
    }

    public Double getOngridPower() {
        return ongridPower;
    }

    public void setOngridPower(Double ongridPower) {
        this.ongridPower = ongridPower;
    }

    public Double getBuyPower() {
        return buyPower;
    }

    public void setBuyPower(Double buyPower) {
        this.buyPower = buyPower;
    }

    public Double getValidProductPower() {
        return validProductPower;
    }

    public void setValidProductPower(Double validProductPower) {
        this.validProductPower = validProductPower;
    }

    public Double getValidConsumePower() {
        return validConsumePower;
    }

    public void setValidConsumePower(Double validConsumePower) {
        this.validConsumePower = validConsumePower;
    }

    public Long getStatisticsTime() {
        return statisticsTime;
    }

    public void setStatisticsTime(Long statisticsTime) {
        this.statisticsTime = statisticsTime;
    }
}