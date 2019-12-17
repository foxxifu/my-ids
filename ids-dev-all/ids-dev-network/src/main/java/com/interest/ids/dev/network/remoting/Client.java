package com.interest.ids.dev.network.remoting;

import java.net.InetSocketAddress;

/**
 * 
 * @author lhq
 *
 *
 */
public interface Client {
	
	void reconnect() throws Exception;
	
	InetSocketAddress getSocketAddress();

}
