package com.interest.ids.dev.network.iec.netty;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import com.interest.ids.dev.network.iec.bean.AbstractMessage;
import com.interest.ids.dev.network.remoting.ChannelHandler;

/**
 * 
 * @author lhq
 *
 *
 */
@Sharable
public class NettyHandler extends SimpleChannelInboundHandler<AbstractMessage> {

	private final ChannelHandler handler;

	public NettyHandler(ChannelHandler handler) {
		this.handler = handler;
	}

	/**
	 * 断开链接
	 */
	@Override
	public void channelInactive(ChannelHandlerContext context) throws Exception {
		handler.disconnected(context.channel());
	}

	/**
	 * 异常处理
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		ctx.close();
	}

	/**
	 * 覆盖channelActive 方法在channel被启用的时候触发（在建立连接的时候） 覆盖了 channelActive()
	 * 事件处理方法。服务端监听到客户端活动
	 */
	@Override
	public void channelActive(ChannelHandlerContext context) throws Exception {

		handler.connect(context.channel());
	}

	/**
	 * 覆盖了 channelRead0() 事件处理方法。 每当从服务端读到客户端写入信息时， 其中如果你使用的是 Netty 5.x 版本时， 需要把
	 * channelRead0() 重命名为messageReceived()
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext context,
			AbstractMessage msg) throws Exception {
		handler.recvMsg(context.channel(), msg);

	}
}