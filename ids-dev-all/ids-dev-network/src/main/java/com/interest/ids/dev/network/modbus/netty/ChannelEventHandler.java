package com.interest.ids.dev.network.modbus.netty;

import io.netty.channel.Channel;


/**
 * 
 * @author zl
 *
 */
public interface ChannelEventHandler {
	
	void onMessage(Channel channel,Object message);
	
	void onConnect(Channel channel);
	
	void onClose(Channel channel);
	
	void onActive(Channel channel);
	
	void onException(Channel channel,Throwable cause);

}
