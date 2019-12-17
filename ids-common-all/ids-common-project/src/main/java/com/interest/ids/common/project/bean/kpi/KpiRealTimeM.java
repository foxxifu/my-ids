package com.interest.ids.common.project.bean.kpi;

import java.math.BigDecimal;

import com.interest.ids.common.project.bean.KpiBaseModel;

/**
 * 实时Kpi数据模型
 * 
 * @author zl
 * 
 */
public class KpiRealTimeM extends KpiBaseModel {

    private static final long serialVersionUID = 776916852900253327L;

    /**
     * 辐照量
     */
    private Double radiationIntensity;

    /**
     * 累计发电量
     */
    private Double productPower;

    /**
     * 当日发电量
     */
    private Double dayCap;

    /**
     * 有功功率
     */
    private Double activePower;

    /**
     * 当日收益
     */
    private BigDecimal dayIncome;

    /**
     * 累计收益
     */
    private BigDecimal totalIncome;

    public Double getRadiationIntensity() {
        return radiationIntensity;
    }

    public void setRadiationIntensity(Double radiationIntensity) {
        this.radiationIntensity = radiationIntensity;
    }

    public Double getProductPower() {
        return productPower;
    }

    public void setProductPower(Double productPower) {
        this.productPower = productPower;
    }

    public Double getDayCap() {
        return dayCap;
    }

    public void setDayCap(Double dayCap) {
        this.dayCap = dayCap;
    }

    public Double getActivePower() {
        return activePower;
    }

    public void setActivePower(Double activePower) {
        this.activePower = activePower;
    }

    public BigDecimal getDayIncome() {
        return dayIncome;
    }

    public void setDayIncome(BigDecimal dayIncome) {
        this.dayIncome = dayIncome;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }
}
