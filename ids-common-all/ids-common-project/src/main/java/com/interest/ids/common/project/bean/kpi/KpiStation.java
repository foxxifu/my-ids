package com.interest.ids.common.project.bean.kpi;

import java.io.Serializable;

public class KpiStation implements Serializable {

    private static final long serialVersionUID = -7206468748928480082L;
    
    private Long collection_time; // 采集时间
    private String station_code;
    private Long domain_id;
    private Double real_capacity;// 实际运行容量
    private Double radiation_intensity;// 辐照量
    private Double theory_power;// 理论发电量
    private Double product_power;// 总发电量
    private Double inverter_power;// 逆变器发电量
    private Double ongrid_power;// 关口表输出电量（上网电量）
    private Double performance_ratio;// 转换效率
    private Double consume_power;// 用电量
    private Double equivalent_hour;// 等效利用小时
    private Double power_profit;// 收益
    private Double co2_reduction;// 二氧化碳减排量
    private Double coal_reduction;// 标准煤节约
    private Long tree_reduction;// 等效植树棵数
    private Long statistic_time;// 统计时间

    public Long getCollection_time() {
        return collection_time;
    }

    public void setCollection_time(Long collection_time) {
        this.collection_time = collection_time;
    }

    public String getStation_code() {
        return station_code;
    }

    public void setStation_code(String station_code) {
        this.station_code = station_code;
    }

    public Long getDomain_id() {
        return domain_id;
    }

    public void setDomain_id(Long domain_id) {
        this.domain_id = domain_id;
    }

    public Double getReal_capacity() {
        return real_capacity;
    }

    public void setReal_capacity(Double real_capacity) {
        this.real_capacity = real_capacity;
    }

    public Double getRadiation_intensity() {
        return radiation_intensity;
    }

    public void setRadiation_intensity(Double radiation_intensity) {
        this.radiation_intensity = radiation_intensity;
    }

    public Double getTheory_power() {
        return theory_power;
    }

    public void setTheory_power(Double theory_power) {
        this.theory_power = theory_power;
    }

    public Double getProduct_power() {
        return product_power;
    }

    public void setProduct_power(Double product_power) {
        this.product_power = product_power;
    }

    public Double getInverter_power() {
        return inverter_power;
    }

    public void setInverter_power(Double inverter_power) {
        this.inverter_power = inverter_power;
    }

    public Double getOngrid_power() {
        return ongrid_power;
    }

    public void setOngrid_power(Double ongrid_power) {
        this.ongrid_power = ongrid_power;
    }

    public Double getPerformance_ratio() {
        return performance_ratio;
    }

    public void setPerformance_ratio(Double performance_ratio) {
        this.performance_ratio = performance_ratio;
    }

    public Double getConsume_power() {
        return consume_power;
    }

    public void setConsume_power(Double consume_power) {
        this.consume_power = consume_power;
    }

    public Double getEquivalent_hour() {
        return equivalent_hour;
    }

    public void setEquivalent_hour(Double equivalent_hour) {
        this.equivalent_hour = equivalent_hour;
    }

    public Double getPower_profit() {
        return power_profit;
    }

    public void setPower_profit(Double power_profit) {
        this.power_profit = power_profit;
    }

    public Double getCo2_reduction() {
        return co2_reduction;
    }

    public void setCo2_reduction(Double co2_reduction) {
        this.co2_reduction = co2_reduction;
    }

    public Double getCoal_reduction() {
        return coal_reduction;
    }

    public void setCoal_reduction(Double coal_reduction) {
        this.coal_reduction = coal_reduction;
    }

    public Long getTree_reduction() {
        return tree_reduction;
    }

    public void setTree_reduction(Long tree_reduction) {
        this.tree_reduction = tree_reduction;
    }

    public Long getStatistic_time() {
        return statistic_time;
    }

    public void setStatistic_time(Long statistic_time) {
        this.statistic_time = statistic_time;
    }
}
