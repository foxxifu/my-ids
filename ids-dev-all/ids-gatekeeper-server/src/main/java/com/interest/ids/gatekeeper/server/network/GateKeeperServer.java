package com.interest.ids.gatekeeper.server.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.nio.ByteOrder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * netty主站server
 * 
 * @author lhq
 *
 */
public class GateKeeperServer {

	private static final Logger log = LoggerFactory
			.getLogger(GateKeeperServer.class);

	private int port;

	private ServerBootstrap bootstrap;

	private EventLoopGroup eventLoopGroupSelector;

	private EventLoopGroup eventLoopGroupBoss;

	public GateKeeperServer(Integer port) {
		this.port = port;
		this.eventLoopGroupBoss = new NioEventLoopGroup(1);
		this.eventLoopGroupSelector = new NioEventLoopGroup();
	}

	public void start() throws Exception {
		bootstrap = new ServerBootstrap();
		bootstrap
				.group(eventLoopGroupBoss, eventLoopGroupSelector)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.option(ChannelOption.SO_REUSEADDR, true)
				.childOption(ChannelOption.SO_KEEPALIVE, false)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.option(ChannelOption.SO_RCVBUF, 65535)
				.option(ChannelOption.SO_SNDBUF, 65535)
				.childOption(ChannelOption.ALLOCATOR,
						PooledByteBufAllocator.DEFAULT)
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel sc)
							throws Exception {
						ChannelPipeline pipeline = sc.pipeline();
						pipeline.addLast("decoder", new GateLengthDecoder(
								ByteOrder.BIG_ENDIAN, 1024 * 10, 0, 2, 0, 0,
								true));
						pipeline.addLast(new GateKeeperHandler());
					}
				});

		ChannelFuture future = bootstrap.bind(port).sync();

		if (future.isSuccess()) {
			log.info("gatekeeper server bind on port : " + port);
		}
	}

	public void shutdown() throws Exception {

	}

}
