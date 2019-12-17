package com.interest.ids.dev.alarm.analysis.bean;

import java.util.Map;

/**
 * 
 * @author lhq
 * 离散率
 *
 */
public class DispersionRatioBean {
	
	/**
     * 离散率
     */
    private Double dispersion;
    
    private Double avgEfficiencyCap;

    /**
     * 组串最大电流
     */
    private Map<Integer, Double> maxPVxIMap;

    /**
     * 组串功率/组串容量 之和
     */
    private Map<Integer, Double> sumPVxPowerMap;

    /**
     * 组串平均电流
     */
    private Map<Integer, Double> avgPVxIMap;

    /**
     * 组串平均电压
     */
    private Map<Integer, Double> avgPVxUMap;

    /**
     * 直流汇流箱平均光伏电压
     */
    private Double avgPhotcU;

	public Double getDispersion() {
		return dispersion;
	}

	public void setDispersion(Double dispersion) {
		this.dispersion = dispersion;
	}

	public Double getAvgEfficiencyCap() {
		return avgEfficiencyCap;
	}

	public void setAvgEfficiencyCap(Double avgEfficiencyCap) {
		this.avgEfficiencyCap = avgEfficiencyCap;
	}

	public Map<Integer, Double> getMaxPVxIMap() {
		return maxPVxIMap;
	}

	public void setMaxPVxIMap(Map<Integer, Double> maxPVxIMap) {
		this.maxPVxIMap = maxPVxIMap;
	}

	public Map<Integer, Double> getSumPVxPowerMap() {
		return sumPVxPowerMap;
	}

	public void setSumPVxPowerMap(Map<Integer, Double> sumPVxPowerMap) {
		this.sumPVxPowerMap = sumPVxPowerMap;
	}

	public Map<Integer, Double> getAvgPVxIMap() {
		return avgPVxIMap;
	}

	public void setAvgPVxIMap(Map<Integer, Double> avgPVxIMap) {
		this.avgPVxIMap = avgPVxIMap;
	}

	public Map<Integer, Double> getAvgPVxUMap() {
		return avgPVxUMap;
	}

	public void setAvgPVxUMap(Map<Integer, Double> avgPVxUMap) {
		this.avgPVxUMap = avgPVxUMap;
	}

	public Double getAvgPhotcU() {
		return avgPhotcU;
	}

	public void setAvgPhotcU(Double avgPhotcU) {
		this.avgPhotcU = avgPhotcU;
	}
}
