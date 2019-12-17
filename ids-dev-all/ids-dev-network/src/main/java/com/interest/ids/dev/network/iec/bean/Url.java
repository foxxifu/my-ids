package com.interest.ids.dev.network.iec.bean;

import java.io.Serializable;
import java.util.Map;

import com.interest.ids.common.project.bean.signal.DeviceInfo;

/**
 * 上下文唯一标识
 * 
 * @author claude
 *
 */
public final class Url implements Serializable {

	private static final long serialVersionUID = -1985165475234910535L;

	private String protocol;

	private String userName;

	private String password;

	private String ip;

	private Integer port;

	private DeviceInfo dev;

	private Object identify;

	private Map<Object, Object> parameters;

	private Short rtuAddress;

	public Url(DeviceInfo dev) {
		this.dev = dev;
		this.identify = dev;
	}

	public Url(Object identify) {
		this.identify = identify;
	}

	public Url() {
		this.protocol = null;
		this.ip = null;
		this.port = null;
		this.identify = null;
		this.parameters = null;
	}

	public Url(String protocol, String ip, Integer port, Object identity) {
		this.protocol = protocol;
		this.ip = ip;
		this.port = port;
		this.identify = identity;
	}

	public Url(String protocol, String ip, Integer port, Object identity,
			DeviceInfo dev) {
		this.protocol = protocol;
		this.ip = ip;
		this.port = port;
		this.identify = identity;
		this.dev = dev;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Map<Object, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<Object, Object> parameters) {
		this.parameters = parameters;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getIp() {
		return ip;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Integer getPort() {
		return port;
	}

	public Object getIdentify() {
		return identify;
	}

	public DeviceInfo getDev() {
		return dev;
	}

	public void setDev(DeviceInfo dev) {
		this.dev = dev;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public Short getRtuAddress() {
		return rtuAddress;
	}

	public void setRtuAddress(Short rtuAddress) {
		this.rtuAddress = rtuAddress;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((identify == null) ? 0 : identify.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Url other = (Url) obj;
		if (identify == null) {
			if (other.identify != null) {
				return false;
			}
		} else if (!identify.equals(other.identify)) {
			return false;
		}
		return true;
	}

	public static final String ESN_CODE = "esnCode";

	public static final String RTU_ADDRESS = "rtuAddress";

}
