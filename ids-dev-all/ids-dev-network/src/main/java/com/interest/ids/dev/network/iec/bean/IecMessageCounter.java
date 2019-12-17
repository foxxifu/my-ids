package com.interest.ids.dev.network.iec.bean;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 计数器
 * 
 * @author lhq
 *
 */
public class IecMessageCounter {

	private short sendSequence = 0;

	private short recvSequence = 0;

	// 接收到的I帧数量
	private AtomicLong recvINum = new AtomicLong();

	public short getSendSequence() {
		return sendSequence;
	}

	public void setSendSequence(short sendSequence) {
		this.sendSequence = sendSequence;
	}

	public short getRecvSequence() {
		return recvSequence;
	}

	public void setRecvSequence(short recvSequence) {
		this.recvSequence = recvSequence;
	}

	public IecMessageCounter() {
	}

	/**
	 * 为了增加可见性,做同步处理
	 * 
	 * @return
	 */
	public synchronized Sequenceinfo getSequence() {

		short sendCount = this.sendSequence;
		this.sendSequence++;

		return new Sequenceinfo(sendCount, this.recvSequence);
	}

	/**
	 * 主要用于更新发送序号
	 * 
	 * @param remoteSeriesInfo
	 *            Sequenceinfo
	 * @return
	 */
	public synchronized boolean receive(Sequenceinfo remoteSeriesInfo) {
		if (null == remoteSeriesInfo) {
			return false;
		}

		this.recvSequence++;
		recvINum.incrementAndGet();

		return true;
	}

	public long getRecvINum() {
		return recvINum.get();
	}

	public void setRecvINum(long recvINum) {
		this.recvINum.set(recvINum);
	}

	public void increaseRecvINum() {
		recvINum.incrementAndGet();
	}

	public synchronized void init() {
		sendSequence = 0;
		recvSequence = 0;
	}

}