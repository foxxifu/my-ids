package com.interest.ids.dev.network.remoting;

import com.interest.ids.dev.network.iec.bean.Url;

import io.netty.channel.Channel;

/**
 * 
 * @author lhq
 *
 *
 */
public interface ChannelHandler {

	/**
	 * 连接设备
	 * 
	 * @param channel
	 * @throws Exception
	 */
	void connect(Channel channel) throws Exception;

	/**
	 * 断开设备链接
	 * 
	 * @param channel
	 * @throws Exception
	 */
	void disconnected(Channel channel) throws Exception;

	/**
	 * 接收消息
	 * 
	 * @param channel
	 *            Channel
	 * @param message
	 *            消息体
	 * @throws Exception
	 */
	void recvMsg(Channel channel, Object message) throws Exception;

	/**
	 * 发送消息
	 * 
	 * @param message
	 */
	void sendMsg(Object message);

	/**
	 * 获取设备通道
	 * 
	 * @return
	 */
	Channel getChannel();

	/**
	 * 获取设备配置信息
	 * 
	 * @return
	 */
	Url getUrl();
}