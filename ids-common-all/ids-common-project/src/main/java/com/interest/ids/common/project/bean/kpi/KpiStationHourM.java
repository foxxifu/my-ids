package com.interest.ids.common.project.bean.kpi;

import com.interest.ids.common.project.bean.KpiBaseModel;

public class KpiStationHourM extends KpiBaseModel {

    private static final long serialVersionUID = 5409271402106062710L;

    private Double radiationIntensity;

    private Double theoryPower;

    private Double productPower;
    
    private Double ongridPower;

    private Double buyPower;

    private Double selfUsePower;

    private Double consumePower;

    private Double limitLossPower;

    private Double troubleLossPower;
    
    private Double performanceRatio;

    private Double powerProfit;

    private Long statisticTime;

    public Double getRadiationIntensity() {
        return radiationIntensity;
    }

    public void setRadiationIntensity(Double radiationIntensity) {
        this.radiationIntensity = radiationIntensity;
    }
    
    public Double getTheoryPower() {
        return theoryPower;
    }

    public void setTheoryPower(Double theoryPower) {
        this.theoryPower = theoryPower;
    }

    public Double getProductPower() {
        return productPower;
    }

    public void setProductPower(Double productPower) {
        this.productPower = productPower;
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

    public Double getSelfUsePower() {
        return selfUsePower;
    }

    public void setSelfUsePower(Double selfUsePower) {
        this.selfUsePower = selfUsePower;
    }

    public Double getLimitLossPower() {
        return limitLossPower;
    }

    public void setLimitLossPower(Double limitLossPower) {
        this.limitLossPower = limitLossPower;
    }

    public Double getTroubleLossPower() {
        return troubleLossPower;
    }

    public void setTroubleLossPower(Double troubleLossPower) {
        this.troubleLossPower = troubleLossPower;
    }

    public Double getPerformanceRatio() {
        return performanceRatio;
    }

    public void setPerformanceRatio(Double performanceRatio) {
        this.performanceRatio = performanceRatio;
    }

    public Double getConsumePower() {
        return consumePower;
    }

    public void setConsumePower(Double consumePower) {
        this.consumePower = consumePower;
    }

    public Double getPowerProfit() {
        return powerProfit;
    }

    public void setPowerProfit(Double powerProfit) {
        this.powerProfit = powerProfit;
    }


    public Long getStatisticTime() {
        return statisticTime;
    }

    public void setStatisticTime(Long statisticTime) {
        this.statisticTime = statisticTime;
    }
}