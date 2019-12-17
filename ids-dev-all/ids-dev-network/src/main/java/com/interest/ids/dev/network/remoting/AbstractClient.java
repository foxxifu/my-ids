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
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.utils.MessageFormatUtils;
import com.interest.ids.dev.network.iec.netty.NettyHandler;

/**
 * 
 * @author lhq
 *
 *
 */
public abstract class AbstractClient implements Client{
	
	private static final Logger log = LoggerFactory.getLogger(AbstractClient.class);
	
	private Channel channel;
	
	public final static int DEFAULT_CLIENT_THREAD_VALUE = 1;
	
	protected  EventLoopGroup group = new NioEventLoopGroup(DEFAULT_CLIENT_THREAD_VALUE);

	private final Lock  connectLock = new ReentrantLock();
	
	private final AtomicInteger reconnect_count = new AtomicInteger(0);
	
	private Bootstrap b;
	
	public Channel connect(InetSocketAddress socketAddress,final ByteToMessageDecoder decoder,final MessageToByteEncoder<?> encoder,final ChannelHandler master) throws Exception{
        Bootstrap b = new Bootstrap();
       
        try {
            b.group(group)
             .channel(NioSocketChannel.class)
             .option(ChannelOption.TCP_NODELAY, true)
             .option(ChannelOption.SO_KEEPALIVE, true)
             .option(ChannelOption.SO_SNDBUF, 65535)
             .option(ChannelOption.SO_RCVBUF, 65535)
             .handler(new ChannelInitializer<SocketChannel>(){
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    
                    pipeline.addLast("decoder", decoder);
                    
                    pipeline.addLast("encoder", encoder);

                    pipeline.addLast("handler", new NettyHandler(master));
                }
             });
            final ChannelFuture channelFuture = b.connect(socketAddress).sync();
            Channel channel = channelFuture.awaitUninterruptibly().channel();
            return channel;
        }catch(Exception e){
        	String msg = MessageFormatUtils.getMsg("connect master error:",e);
        	log.error(msg);
        }
        return null;
    }

	public AbstractClient(){
		
	}
	
	@Override
	public void reconnect() throws Exception {
		connectLock.lock();
		try {
			
		} catch (Exception e) {
			
		}
		finally{
			connectLock.unlock();
		}
		
	}

	@Override
	public InetSocketAddress getSocketAddress() {
		
		return null;
	}

	public Channel getChannel() {
		return channel;
	}
}
