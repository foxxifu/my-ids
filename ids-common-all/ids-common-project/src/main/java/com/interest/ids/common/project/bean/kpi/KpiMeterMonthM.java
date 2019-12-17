package com.interest.ids.common.project.bean.kpi;

import com.interest.ids.common.project.bean.KpiBaseModel;

public class KpiMeterMonthM extends KpiBaseModel {

    private static final long serialVersionUID = 8596542955462613637L;

    private String devName;

    private Integer meterType;
    
    private Double ongridPower;

    private Double buyPower;
    
    private Long statisticsTime;
    
    private Double equivalentHour;

    public Double getEquivalentHour() {
		return equivalentHour;
	}

	public void setEquivalentHour(Double equivalentHour) {
		this.equivalentHour = equivalentHour;
	}

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

    public Long getStatisticsTime() {
        return statisticsTime;
    }

    public void setStatisticsTime(Long statisticsTime) {
        this.statisticsTime = statisticsTime;
    }
}