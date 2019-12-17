package com.interest.ids.dev.alarm.analysis.bean;

/**
 * 
 * @author lhq
 *
 *
 */
public class DevVolCurrentDataBean {

    public Long devId;

    public String sid;

    // 直流汇流箱电压
    private Double photcU;

    // 接入组串的组串电压和
    private Double totalConnectMaxPvU;
    // 连接的PV组串数
    private int connectPvCount;
    // 电流异常串数（电流小于等于0.3的组串数）
    private int faultPviCount;

    // 逆变器类型： 1组串式逆变器 2集中式逆变器
    private Integer inverterType;

    public Long getDevId() {
		return devId;
	}

	public void setDevId(Long devId) {
		this.devId = devId;
	}

	public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public Double getPhotcU() {
        return photcU;
    }

    public void setPhotcU(Double photcU) {
        this.photcU = photcU;
    }

    public Double getTotalConnectMaxPvU() {
        return totalConnectMaxPvU;
    }

    public void setTotalConnectMaxPvU(Double totalConnectMaxPvU) {
        this.totalConnectMaxPvU = totalConnectMaxPvU;
    }

    public int getConnectPvCount() {
        return connectPvCount;
    }

    public void setConnectPvCount(int connectPvCount) {
        this.connectPvCount = connectPvCount;
    }

    public int getFaultPviCount() {
        return faultPviCount;
    }

    public void setFaultPviCount(int faultPviCount) {
        this.faultPviCount = faultPviCount;
    }

    public Integer getInverterType() {
        return inverterType;
    }

    public void setInverterType(Integer inverterType) {
        this.inverterType = inverterType;
    }
}
