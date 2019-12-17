package com.interest.ids.dev.network.modbus.netty;

import java.nio.ByteOrder;
import java.util.Collection;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import javax.net.ssl.SSLEngine;

import com.interest.ids.dev.network.modbus.bean.DisconnectFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;
import com.interest.ids.common.project.threadpool.NamedThreadFactory;
import com.interest.ids.common.project.threadpool.NatureThreadExecutor;
import com.interest.ids.dev.api.utils.ByteUtils;
import com.interest.ids.dev.network.modbus.bean.DeviceChannel;
import com.interest.ids.dev.network.modbus.command.ModbusHeartBeat;
import com.interest.ids.dev.network.modbus.message.ModbusMessageRunnable;
import com.interest.ids.dev.network.modbus.transfer.MobusLengthDecoder;
import com.interest.ids.dev.network.modbus.transfer.ModbusMessageEncoder;
import com.interest.ids.dev.network.modbus.transfer.ModbusSslContextFactory;
import com.interest.ids.dev.network.modbus.utils.ModbusUtils;
import com.interest.ids.dev.network.modbus.utils.RC4Utils;
import com.interest.ids.dev.network.remoting.Server;
import com.interest.ids.dev.network.util.RemotingUtil;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author yaokun
 */
public class ModbusServer implements Server {

    private static final Logger log = LoggerFactory.getLogger(ModbusServer.class);

    private Integer port = 7077;

    private ServerBootstrap bootstrap;

    private EventLoopGroup eventLoopGroupSelector;

    private EventLoopGroup eventLoopGroupBoss;

    private NettyServerConfig nettyServerConfig;

    private ExecutorService publicExecutor;

    private boolean isSSl;

    private final Timer timer = new Timer("checkChannel", true);

    private Map<Channel, DeviceChannel> channels = new ConcurrentHashMap<Channel, DeviceChannel>();

    public ModbusServer(final NettyServerConfig nettyServerConfig) {

        this.bootstrap = new ServerBootstrap();
        this.nettyServerConfig = nettyServerConfig;

        this.isSSl = nettyServerConfig.isSSl();
        int threadNums = nettyServerConfig.getServerCallbackExecutorThreads();
        if (threadNums <= 0) {
            threadNums = 4;
        }

        this.publicExecutor = new NatureThreadExecutor(threadNums, threadNums,
                2048, new NamedThreadFactory("modbus_protocol"));

        this.eventLoopGroupBoss = new NioEventLoopGroup(1,
                new NamedThreadFactory("modbus_boss"));

        if (useEpoll()) {
            this.eventLoopGroupSelector = new EpollEventLoopGroup(
                    nettyServerConfig.getServerSelectorThreads(),
                    new NamedThreadFactory(String.format("modbus_enpoll_%d",
                            nettyServerConfig.getServerSelectorThreads())));
        } else {
            this.eventLoopGroupSelector = new NioEventLoopGroup(
                    nettyServerConfig.getServerSelectorThreads(),
                    new NamedThreadFactory(String.format("modbus_nio_%d",
                            nettyServerConfig.getServerSelectorThreads())));
        }
    }

    @Override
    public void start() throws Exception {

        bootstrap
                .group(eventLoopGroupBoss, eventLoopGroupSelector)
                .channel(useEpoll() ? EpollServerSocketChannel.class
                        : NioServerSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_RCVBUF, nettyServerConfig.getServerSocketRcvBufSize())
                .option(ChannelOption.SO_SNDBUF, nettyServerConfig.getServerSocketSndBufSize())
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ModbusChannelInitializer());

        ChannelFuture future = bootstrap.bind(port).sync();

        if (future.isSuccess()) {
            log.info("modbus bind on port : {}", port);
        }
    }

    @Override
    public void shutdown() throws Exception {
        try {

            this.eventLoopGroupBoss.shutdownGracefully();

            this.eventLoopGroupSelector.shutdownGracefully();

        } catch (Exception e) {
            log.error("modbus server shutdown exception, ", e);
        }

        if (this.publicExecutor != null) {
            try {
                this.publicExecutor.shutdown();
            } catch (Exception e) {
                log.error("NettyRemotingServer shutdown exception, ", e);
            }
        }
    }

    private boolean useEpoll() {

        return RemotingUtil.isLinuxPlatform()
                && nettyServerConfig.isUseEpollNativeSelector()
                && Epoll.isAvailable();
    }

    public class ModbusChannelInitializer extends
            ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            if (isSSl) {
                SSLEngine engine = ModbusSslContextFactory.getServerContext()
                        .createSSLEngine();
                engine.setUseClientMode(false);
                engine.setNeedClientAuth(true);
                pipeline.addLast("ssl", new SslHandler(engine));
            }

            pipeline.addLast("idleHandler", new ModbusIdleHandler(180, 30, 180));
            pipeline.addLast("decoder", new MobusLengthDecoder(
                    ByteOrder.BIG_ENDIAN, 1024 * 100, 4, 2, 0, 0, true));
            pipeline.addLast("encoder", new ModbusMessageEncoder());
            pipeline.addLast("handler", new ModbusNettyHandler());

        }

        class ModbusIdleHandler extends IdleStateHandler {
            public ModbusIdleHandler(int readerIdleTimeSeconds,
                                     int writerIdleTimeSeconds, int allIdleTimeSeconds) {
                super(readerIdleTimeSeconds, writerIdleTimeSeconds,
                        allIdleTimeSeconds);
            }

            @Override
            protected void channelIdle(ChannelHandlerContext ctx,
                                       IdleStateEvent evt) {
                if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
                    IdleStateEvent event = evt;
                    if (event.state() == IdleState.READER_IDLE) {
                        log.error("the channel is read idle: {} will be closed", ctx.channel().remoteAddress());
                        ctx.close();
                    } else if (event.state() == IdleState.WRITER_IDLE) {
                        log.error("the channel is write idle: {} will be heart beat", ctx.channel().remoteAddress());
                        heartBeat(ctx.channel());
                    } else if (event.state() == IdleState.ALL_IDLE) {
                        log.error("the channel is all idle {}", ctx.channel().remoteAddress());
                    }
                }
            }

            private void heartBeat(Channel channel) {
                ModbusHeartBeat.heartBeat(channel);
                log.info("the dc heartbeat {}", channel.remoteAddress());
            }
        }
    }


    class ModbusNettyHandler extends SimpleChannelInboundHandler<UnSafeHeapBuffer> {

        @Override
        public void channelActive(final ChannelHandlerContext ctx)
                throws Exception {
            log.info("new modbus connection {}", ctx.channel().remoteAddress());
            channels.put(ctx.channel(), new DeviceChannel(ctx.channel()));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            log.error("exception {} {} ", ctx.channel().remoteAddress(), cause);
            RemotingUtil.closeChannel(ctx.channel());
        }


        @Override
        protected void channelRead0(ChannelHandlerContext ctx, UnSafeHeapBuffer msg)
                throws Exception {
            msg = RC4Utils.rc4Deal(msg);
            log.info("modbus server recv: {} {}", ModbusUtils.getSnByChannel(ctx.channel()), ByteUtils.formatHexString(msg.array()));
            publicExecutor.execute(new ModbusMessageRunnable(ctx.channel(), msg));
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            log.error("modbus dc disconnect  " + ModbusUtils.getSnByChannel(ctx.channel()));
            RemotingUtil.closeChannel(ctx.channel());
            DisconnectFilter.getInstance().executeDelyTask(ctx.channel());
            channels.remove(ctx.channel());
        }
    }


    @Override
    public int getBindPort() {

        return port;
    }
}
