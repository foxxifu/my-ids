package com.interest.ids.dev.network.remoting;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.collection.IntObjectHashMap;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.bean.signal.SignalInfo;
import com.interest.ids.dev.network.iec.bean.Url;
import com.interest.ids.dev.network.iec.netty.IecMessageDecoder;
import com.interest.ids.dev.network.iec.netty.IecMessageEncoder;
import com.interest.ids.dev.network.iec.netty.NettyHandler;

/**
 * 
 * @author lhq 主站抽象类
 *
 */
public abstract class AbstractMaster implements ChannelHandler {

	private static final Logger log = LoggerFactory.getLogger(AbstractMaster.class);
	
	// 默认给每个客户端分配一个线程
	public final static int DEFAULT_CLIENT_THREAD_VALUE = 1;

	protected EventLoopGroup group = new NioEventLoopGroup(DEFAULT_CLIENT_THREAD_VALUE);

	// 以寄存器地址作为key的map
	protected IntObjectHashMap<SignalInfo> signals;

	// 请求参数配置类
	protected Url url;

	// 协议类型
	protected String protocol;

	// 重试次数，6次失败后执行告警
	protected int reTryTime = 0;

	protected Channel channel;

	// 主机状态
	protected volatile MasterState state;

	public AbstractMaster(Url url, IntObjectHashMap<SignalInfo> signals) {
		this.url = url;
		this.protocol = url.getProtocol();
		this.signals = signals;
	}

	/**
	 * 停止
	 */
	public abstract void stop();

	/**
	 * 启动
	 */
	public abstract void start();

	public boolean isContinue = true;

	/**
	 * 重连
	 * 
	 * @return
	 */
	public abstract boolean reconnect();

	public Channel connect(InetSocketAddress socketAddress,
			final ChannelHandler master) throws Exception {
		Bootstrap b = new Bootstrap();

		try {
			b.group(group).channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)
					.option(ChannelOption.SO_KEEPALIVE, true)
					.option(ChannelOption.SO_SNDBUF, 65535)
					.option(ChannelOption.SO_RCVBUF, 65535)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel socketChannel)
								throws Exception {
							ChannelPipeline pipeline = socketChannel.pipeline();

							// 解码器
							pipeline.addLast("decoder", new IecMessageDecoder());

							// 编码器
							pipeline.addLast("encoder", new IecMessageEncoder());
							
							// SimpleChannelInboundHandler
							pipeline.addLast("handler", new NettyHandler(master));
						}
					});
			final ChannelFuture channelFuture = b.connect(socketAddress).sync();
			Channel channel = channelFuture.awaitUninterruptibly().channel();
			return channel;
		} catch (Exception e) {
			log.error("connect device failed! device info : "
					+ socketAddress.getAddress().getHostAddress(), e);
		}
		return null;
	}

	public MasterState getState() {
		return state;
	}

	public void setState(MasterState state) {
		this.state = state;
	}

	public Url getUrl() {
		return url;
	}

	public Channel getChannel() {
		return channel;
	}

	public IntObjectHashMap<SignalInfo> getSignals() {
		return signals;
	}

	public enum MasterState {
		DISCONNECTED, // 断连状态
		CONNECTED // 连接状态
	}
}
