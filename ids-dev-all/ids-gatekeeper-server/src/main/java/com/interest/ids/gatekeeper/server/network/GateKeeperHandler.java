package com.interest.ids.gatekeeper.server.network;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;
import com.interest.ids.gatekeeper.server.parse.ProtocolParse;

/**
 * netty handler
 * 
 * @author lhq
 *
 */
@Sharable
public class GateKeeperHandler extends
		SimpleChannelInboundHandler<UnSafeHeapBuffer> {

	@Override
	protected void channelRead0(ChannelHandlerContext arg0, UnSafeHeapBuffer msg)
			throws Exception {

		ProtocolParse.getParser().parse(msg);
	}

}
