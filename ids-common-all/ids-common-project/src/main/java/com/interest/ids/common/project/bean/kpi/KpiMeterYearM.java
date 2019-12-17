package com.interest.ids.common.project.bean.kpi;

import com.interest.ids.common.project.bean.KpiBaseModel;

public class KpiMeterYearM extends KpiBaseModel {

    private static final long serialVersionUID = 3065967334415263715L;

    private String deviceName;
    
    private Integer meterType;

    private Double ongridPower;

    private Double buyPower;

    private Long statisticsTime;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
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