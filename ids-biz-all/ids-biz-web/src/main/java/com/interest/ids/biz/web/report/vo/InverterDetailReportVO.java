package com.interest.ids.biz.web.report.vo;

import java.io.Serializable;

/**
 * 逆变器详细运行报表，5分钟数据
 * 
 * @author zengls
 * @date 2018-08-07
 */
public class InverterDetailReportVO implements Serializable {

	private static final long serialVersionUID = -5297095549293293043L;

	/**
     * 采集时间
     */
    private Long collectTime;
    private String stationName;
    private String stationCode;
    private Long devId;
    private String devName;
    
    private Double au;
    private Double bu;
    private Double cu;
    private Double ai;
    private Double bi;
    private Double ci;
    
    private Double pv1u;
    private Double pv2u;
    private Double pv3u;
    private Double pv4u;
    private Double pv5u;
    private Double pv6u;
    private Double pv7u;
    private Double pv8u;
    private Double pv9u;
    private Double pv10u;
    private Double pv11u;
    private Double pv12u;
    private Double pv13u;
    private Double pv14u;
    
    private Double pv1i;
    private Double pv2i;
    private Double pv3i;
    private Double pv4i;
    private Double pv5i;
    private Double pv6i;
    private Double pv7i;
    private Double pv8i;
    private Double pv9i;
    private Double pv10i;
    private Double pv11i;
    private Double pv12i;
    private Double pv13i;
    private Double pv14i;
    
    private Double temperature;
    private Double gridFrequency;
    private Double activePower;
    private Double dayCapacity;
    private Double totalCapacity;
    
	public Long getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(Long collectTime) {
		this.collectTime = collectTime;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getStationCode() {
		return stationCode;
	}
	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}
	public Long getDevId() {
		return devId;
	}
	public void setDevId(Long devId) {
		this.devId = devId;
	}
	public String getDevName() {
		return devName;
	}
	public void setDevName(String devName) {
		this.devName = devName;
	}
	public Double getAu() {
		return au;
	}
	public void setAu(Double au) {
		this.au = au;
	}
	public Double getBu() {
		return bu;
	}
	public void setBu(Double bu) {
		this.bu = bu;
	}
	public Double getCu() {
		return cu;
	}
	public void setCu(Double cu) {
		this.cu = cu;
	}
	public Double getAi() {
		return ai;
	}
	public void setAi(Double ai) {
		this.ai = ai;
	}
	public Double getBi() {
		return bi;
	}
	public void setBi(Double bi) {
		this.bi = bi;
	}
	public Double getCi() {
		return ci;
	}
	public void setCi(Double ci) {
		this.ci = ci;
	}
	public Double getPv1u() {
		return pv1u;
	}
	public void setPv1u(Double pv1u) {
		this.pv1u = pv1u;
	}
	public Double getPv2u() {
		return pv2u;
	}
	public void setPv2u(Double pv2u) {
		this.pv2u = pv2u;
	}
	public Double getPv3u() {
		return pv3u;
	}
	public void setPv3u(Double pv3u) {
		this.pv3u = pv3u;
	}
	public Double getPv4u() {
		return pv4u;
	}
	public void setPv4u(Double pv4u) {
		this.pv4u = pv4u;
	}
	public Double getPv5u() {
		return pv5u;
	}
	public void setPv5u(Double pv5u) {
		this.pv5u = pv5u;
	}
	public Double getPv6u() {
		return pv6u;
	}
	public void setPv6u(Double pv6u) {
		this.pv6u = pv6u;
	}
	public Double getPv7u() {
		return pv7u;
	}
	public void setPv7u(Double pv7u) {
		this.pv7u = pv7u;
	}
	public Double getPv8u() {
		return pv8u;
	}
	public void setPv8u(Double pv8u) {
		this.pv8u = pv8u;
	}
	public Double getPv9u() {
		return pv9u;
	}
	public void setPv9u(Double pv9u) {
		this.pv9u = pv9u;
	}
	public Double getPv10u() {
		return pv10u;
	}
	public void setPv10u(Double pv10u) {
		this.pv10u = pv10u;
	}
	public Double getPv11u() {
		return pv11u;
	}
	public void setPv11u(Double pv11u) {
		this.pv11u = pv11u;
	}
	public Double getPv12u() {
		return pv12u;
	}
	public void setPv12u(Double pv12u) {
		this.pv12u = pv12u;
	}
	public Double getPv13u() {
		return pv13u;
	}
	public void setPv13u(Double pv13u) {
		this.pv13u = pv13u;
	}
	public Double getPv14u() {
		return pv14u;
	}
	public void setPv14u(Double pv14u) {
		this.pv14u = pv14u;
	}
	public Double getPv1i() {
		return pv1i;
	}
	public void setPv1i(Double pv1i) {
		this.pv1i = pv1i;
	}
	public Double getPv2i() {
		return pv2i;
	}
	public void setPv2i(Double pv2i) {
		this.pv2i = pv2i;
	}
	public Double getPv3i() {
		return pv3i;
	}
	public void setPv3i(Double pv3i) {
		this.pv3i = pv3i;
	}
	public Double getPv4i() {
		return pv4i;
	}
	public void setPv4i(Double pv4i) {
		this.pv4i = pv4i;
	}
	public Double getPv5i() {
		return pv5i;
	}
	public void setPv5i(Double pv5i) {
		this.pv5i = pv5i;
	}
	public Double getPv6i() {
		return pv6i;
	}
	public void setPv6i(Double pv6i) {
		this.pv6i = pv6i;
	}
	public Double getPv7i() {
		return pv7i;
	}
	public void setPv7i(Double pv7i) {
		this.pv7i = pv7i;
	}
	public Double getPv8i() {
		return pv8i;
	}
	public void setPv8i(Double pv8i) {
		this.pv8i = pv8i;
	}
	public Double getPv9i() {
		return pv9i;
	}
	public void setPv9i(Double pv9i) {
		this.pv9i = pv9i;
	}
	public Double getPv10i() {
		return pv10i;
	}
	public void setPv10i(Double pv10i) {
		this.pv10i = pv10i;
	}
	public Double getPv11i() {
		return pv11i;
	}
	public void setPv11i(Double pv11i) {
		this.pv11i = pv11i;
	}
	public Double getPv12i() {
		return pv12i;
	}
	public void setPv12i(Double pv12i) {
		this.pv12i = pv12i;
	}
	public Double getPv13i() {
		return pv13i;
	}
	public void setPv13i(Double pv13i) {
		this.pv13i = pv13i;
	}
	public Double getPv14i() {
		return pv14i;
	}
	public void setPv14i(Double pv14i) {
		this.pv14i = pv14i;
	}
	public Double getTemperature() {
		return temperature;
	}
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
	public Double getGridFrequency() {
		return gridFrequency;
	}
	public void setGridFrequency(Double gridFrequency) {
		this.gridFrequency = gridFrequency;
	}
	public Double getActivePower() {
		return activePower;
	}
	public void setActivePower(Double activePower) {
		this.activePower = activePower;
	}
	public Double getDayCapacity() {
		return dayCapacity;
	}
	public void setDayCapacity(Double dayCapacity) {
		this.dayCapacity = dayCapacity;
	}
	public Double getTotalCapacity() {
		return totalCapacity;
	}
	public void setTotalCapacity(Double totalCapacity) {
		this.totalCapacity = totalCapacity;
	}
	
}
