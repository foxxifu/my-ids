package com.interest.ids.dev.network.iec.bean;

import io.netty.channel.Channel;

/**
 * netty Handler
 * 
 * @author lhq
 *
 */
public interface ChannelHandler {

	/**
	 * 建立通讯链接
	 * 
	 * @param channel
	 *            通道
	 * @throws Exception
	 */
	void connect(Channel channel) throws Exception;

	/**
	 * 断开通讯链接
	 * 
	 * @param channel
	 * @throws Exception
	 */
	void disconnected(Channel channel) throws Exception;

	/**
	 * 接收消息
	 * 
	 * @param channel
	 *            通道
	 * @param message
	 *            消息
	 * @throws Exception
	 */
	void recvMsg(Channel channel, Object message) throws Exception;

	/**
	 * 发送消息
	 * 
	 * @param message
	 *            消息
	 */
	void sendMsg(Object message);

	/**
	 * 获取通道
	 * 
	 * @return 通道
	 */
	Channel getChannel();

	/**
	 * 获取上下文信息
	 */
	Url getUrl();
}