package com.interest.ids.dev.network.iec.bean;

/**
 * 
 * @author lhq
 *
 */
public class Sequenceinfo {

	private short sendSeries = 0;

	private short receiveSeries = 0;

	public Sequenceinfo(short send, short receive) {
		this.sendSeries = send;
		this.receiveSeries = receive;
	}

	public short getSendSeries() {
		return sendSeries;
	}

	public void setSendSeries(short sendSeries) {
		this.sendSeries = sendSeries;
	}

	public short getRecvSeries() {
		return receiveSeries;
	}

	public void setRecvSeries(short recvSeries) {
		this.receiveSeries = recvSeries;
	}
}