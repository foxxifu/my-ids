package com.interest.ids.dev.network.iec.parse;

import com.interest.ids.dev.network.iec.bean.Iec104Message;
import com.interest.ids.dev.network.remoting.ChannelHandler;



public interface Parser {
	
	
	void parse(ChannelHandler handler,Iec104Message message);

}
