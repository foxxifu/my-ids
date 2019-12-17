package com.interest.ids.dev.network.iec.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.collection.IntObjectHashMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.signal.SignalInfo;
import com.interest.ids.dev.network.iec.bean.Url;
import com.interest.ids.dev.network.remoting.ChannelHandler;
import com.interest.ids.dev.network.remoting.Server;

/**
 * 
 * @author lhq
 * 
 *         104公共服务端，接入铁牛数采
 *
 */
public class Iec104Server implements Server, ChannelHandler {

	private static final Logger log = LoggerFactory.getLogger(Iec104Server.class);

	private int port;

	private Url url;

	private ServerBootstrap bootstrap;

	private EventLoopGroup eventLoopGroupSelector;

	private EventLoopGroup eventLoopGroupBoss;
	/**
	 *  连接通道和对于处理消息的对象
	 */
	private Map<Channel, ChannelHandler> channels;

	private static Map<Long, Iec104Server> map = new ConcurrentHashMap<>();

	public Iec104Server(int port) {
		this.port = port;
		channels = new ConcurrentHashMap<Channel, ChannelHandler>();
		bootstrap = new ServerBootstrap();
		eventLoopGroupBoss = new NioEventLoopGroup(1);
		eventLoopGroupSelector = new NioEventLoopGroup();
	}

	public Iec104Server(Url url) {
		this.url = url;
		channels = new ConcurrentHashMap<Channel, ChannelHandler>();
		bootstrap = new ServerBootstrap();
		eventLoopGroupBoss = new NioEventLoopGroup(1);
		eventLoopGroupSelector = new NioEventLoopGroup();
		map.put(url.getDev().getId(), this);
	}

	@Override
	public void start() throws Exception {
		final ChannelHandler handler = this;
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
						pipeline.addLast(new IecMessageDecoder(),
								new IecMessageEncoder(), 
								new NettyHandler(handler));
					}

				});

		ChannelFuture future = bootstrap.bind(port).sync();

		if (future.isSuccess()) {
			log.info("iec104 server bind on port : " + port);
		}

	}

	@Override
	public void shutdown() throws Exception {
		if (eventLoopGroupBoss != null) {
			eventLoopGroupBoss.shutdownGracefully();
		}

		if (eventLoopGroupSelector != null) {
			eventLoopGroupSelector.shutdownGracefully();
		}
	}

	@Override
	public void connect(Channel channel) throws Exception {
		// 这一步做异步处理，加载点表
		ChannelHandler wapper = channels.get(channel);
		if (wapper == null) {
			wapper = new Iec104MasterWapperChannel(url);
			channels.put(channel, wapper);
			wapper.connect(channel);
		} else {
			wapper.connect(channel);
		}
	}

	@Override
	public void disconnected(Channel channel) throws Exception {
		log.error("the device disconnect {}", channel.remoteAddress());

		ChannelHandler wapper = channels.remove(channel);
		if (wapper != null) {
			wapper.disconnected(channel);
		}
	}

	@Override
	public void recvMsg(Channel channel, Object message) throws Exception {
		ChannelHandler wapper = channels.get(channel);
		if (wapper != null) {
			wapper.recvMsg(channel, message);
		} else { 
			// 使用CountDownLatch等待完成这个任务
			final CountDownLatch cdl = new CountDownLatch(1);
			final Channel cl = channel;
			final Object msg = message;
			new Thread(new Runnable() {
				@Override
				public void run() { // 处理连接的操作
					ChannelHandler wa = new Iec104MasterWapperChannel(url);
					channels.put(cl, wa);
					try {
						wa.connect(cl);
						wa.recvMsg(cl, msg);
					} catch (Exception e) {
						log.error("has exception::", e);
					}
					cdl.countDown(); // 执行创建连接完成了
				}
			}).start();
			cdl.await(); // 等待连接上再执行下面的事情
		}

	}

	@Override
	public void sendMsg(Object message) {

		// donothing
	}

	@Override
	public Channel getChannel() {

		return null;
	}

	@Override
	public Url getUrl() {

		return url;
	}
	/**
	 * 这里使用主动删除对应的连接
	 * @param devId 设备id
	 * @param type  1：新增; 2:删除；3:修改；
	 */
	public void updateTNIec104MasterWapperChannel(Long devId, int type){
		Channel current = getChannelByDevId(devId);
		log.info("update Url of devId = {}, type={}", devId, type);
		if(current != null) { // 如果有对应的连接
			channels.remove(current); // 移除掉,如果收到消息做重新连接的事情,具体的是在recvMsg(Channel channel, Object message)方法中去创建的
			// if (type == 1 || type == 3){ // 对于修改、删除的就直接移除这个连接，页不考虑之前的告警信息
			// 	current.close(); // 关闭连接
			// 	channels.remove(current);
			// } else { // 新增的应该不能获取到能获取到这个还是做一个处理正常不能够获取，这里还是写一个
			// 	current.close(); // 关闭连接
			// 	channels.remove(current);
			// }
		}
	}
	// 根据设备获取铁牛数采的连接的channel
	private Channel getChannelByDevId(Long devId) {
		if (devId == null) {
			log.error("no update devId");
			return null;
		}
		Channel current = null;
		for (Channel cl : channels.keySet()) { // 循环获取当前连接的channel
			ChannelHandler ch = channels.get(cl);
			Url u = ch.getUrl();
			if (u == null) {
				continue;
			}
			DeviceInfo dev = u.getDev();
			if (dev == null) {
				continue;
			}
			if(devId.equals(dev.getId())){
				current = cl;
				break;
			}
		}
		return current;
	}
	/**
	 * 更新连接设备的信号点
	 * @param devId
	 */
	public void updateSignal(Long devId, IntObjectHashMap<SignalInfo> signals) {
		Channel current = getChannelByDevId(devId);
		if (current == null) {
			log.warn("dev is not connect,devId = {}", devId);
			return;
		}
		ChannelHandler wapper = channels.get(current);
		if (wapper == null) {
			return;
		}
		if (wapper instanceof Iec104MasterWapperChannel) { // 只有这个对象才去执行对应的设置
			Iec104MasterWapperChannel wa = (Iec104MasterWapperChannel)wapper;
			wa.setSignals(signals);
		}
	}

	public static Iec104Server get104Server(Long id) {
		return map.get(id);
	}

	@Override
	public int getBindPort() {

		return port;
	}

}
