package com.interest.ids.common.project.threadpool;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author lhq
 * 
 */
public class NamedThreadFactory implements ThreadFactory{

	private  ThreadGroup group;
	private  String namePrefix;
	private  AtomicInteger threadId;
	private  boolean isDaemon;

	public NamedThreadFactory(String name, boolean isDaemon) {
		SecurityManager s = System.getSecurityManager();
		this.group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		this.namePrefix = name;
		this.threadId = new AtomicInteger(1);
		this.isDaemon = isDaemon;
	}
	
	public NamedThreadFactory(String name) {
		SecurityManager s = System.getSecurityManager();
		this.group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		this.namePrefix = name;
		this.threadId = new AtomicInteger(1);
		this.isDaemon = false;
	}
		
	
	public Thread newThread(Runnable r) {
		int i = threadId.getAndIncrement();
		Thread t = new Thread(group, r, namePrefix +i);
		t.setDaemon(isDaemon);
		return t;
	}
	
	

}
