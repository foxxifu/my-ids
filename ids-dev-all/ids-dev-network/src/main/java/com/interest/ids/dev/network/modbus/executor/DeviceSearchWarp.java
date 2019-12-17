package com.interest.ids.dev.network.modbus.executor;

import java.net.InetSocketAddress;

import io.netty.channel.Channel;


/**
 * 
 * @author lhq
 *
 */
public class DeviceSearchWarp {
	
	private Channel channel;
	
	private Long accessTime;
	
	private InetSocketAddress address;
	
	
	public DeviceSearchWarp(Channel channel){
		this.channel = channel;
		accessTime = System.currentTimeMillis();
		address = (InetSocketAddress) channel.remoteAddress();
	}
	
	
	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Long getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(Long accessTime) {
		this.accessTime = accessTime;
	}

	public InetSocketAddress getAddress() {
		return address;
	}

	public void setAddress(InetSocketAddress address) {
		this.address = address;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeviceSearchWarp other = (DeviceSearchWarp) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		return true;
	}
}
