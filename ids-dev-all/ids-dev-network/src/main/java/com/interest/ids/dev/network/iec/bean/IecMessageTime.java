package com.interest.ids.dev.network.iec.bean;

/**
 * 计时器
 * 
 * @author lhq
 *
 */
public class IecMessageTime {

	// 默认T1超时时间
	public static final long DEFAULT_T1 = 15 * 1000;

	// 默认T2超时时间
	public static final long DEFAULT_T2 = 10 * 1000;

	// 默认T3超时时间
	public static final long DEFAULT_T3 = 20 * 1000;

	private volatile boolean test = false;

	private volatile long recvT1Iframe = 0L;

	private volatile long recvT1Uframe = 0;

	private volatile long recvT2 = 0L;

	private volatile long recvT3 = 0L;

	/**
	 * 由于在单线程中，不用做同步，不用担心可见性问题；
	 */
	public void refreshT1U() {
		test = false;
		recvT1Uframe = System.currentTimeMillis();
	}

	public void refreshT2() {
		recvT2 = System.currentTimeMillis();
	}

	public void refreshT3() {
		recvT3 = System.currentTimeMillis();
	}

	/**
	 * 初始化时间
	 */
	public void init() {
		recvT1Iframe = 0L;
		recvT1Uframe = 0L;
		recvT2 = 0L;
		recvT3 = 0L;
	}

	public boolean isTest() {
		return test;
	}

	public void setTest(boolean test) {
		this.test = test;
	}

	public long getRecvT3() {
		return recvT3;
	}

	public void setRecvT3(long recvT3) {
		this.recvT3 = recvT3;
	}

	public long getRecvT1Iframe() {
		return recvT1Iframe;
	}

	public void setRecvT1Iframe(long recvT1Iframe) {
		this.recvT1Iframe = recvT1Iframe;
	}

	public long getRecvT1Uframe() {
		return recvT1Uframe;
	}

	public void setRecvT1Uframe(long recvT1Uframe) {
		this.recvT1Uframe = recvT1Uframe;
	}

	public long getRecvT2() {
		return recvT2;
	}

	public void setRecvT2(long recvT2) {
		this.recvT2 = recvT2;
	}

}
