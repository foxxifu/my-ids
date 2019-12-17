package com.interest.ids.common.project.bean.signal;

import javax.persistence.Table;

import com.interest.ids.common.project.bean.BaseBean;

@Table(name="ids_subscribe_signal_t")
public class SubscribeSignal extends BaseBean{
	
    private static final long serialVersionUID = -9046513486872711609L;

	private Integer signalAddress;
	
	private Integer registerNum;
	
	private String signalVersion;
	
	private Integer registerType;
	
	private Integer period;
	
	private Integer subscribeType;

	public Integer getSignalAddress() {
		return signalAddress;
	}

	public void setSignalAddress(Integer signalAddress) {
		this.signalAddress = signalAddress;
	}

	public Integer getRegisterNum() {
		return registerNum;
	}

	public void setRegisterNum(Integer registerNum) {
		this.registerNum = registerNum;
	}

	public String getSignalVersion() {
		return signalVersion;
	}

	public void setSignalVersion(String signalVersion) {
		this.signalVersion = signalVersion;
	}

	public Integer getRegisterType() {
		return registerType;
	}

	public void setRegisterType(Integer registerType) {
		this.registerType = registerType;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public Integer getSubscribeType() {
		return subscribeType;
	}

	public void setSubscribeType(Integer subscribeType) {
		this.subscribeType = subscribeType;
	}

}
