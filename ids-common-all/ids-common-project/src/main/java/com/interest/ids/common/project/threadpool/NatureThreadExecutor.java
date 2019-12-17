package com.interest.ids.common.project.threadpool;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 
 * @author lhq
 * 
 *
 */
public class NatureThreadExecutor extends ThreadPoolExecutor {

	public static final int DEFAULT_MIN_THREADS = 10;
	public static final int DEFAULT_MAX_THREADS = 10;
	public static final int DEFAULT_MAX_IDLE_TIME = 60 * 1000; // 1 minutes
	public static final int DEFAULT_MAX_QUEUE_SIZE = 1024;

	protected AtomicInteger submittedTasksCount;	// 正在处理的任务数 
	private int maxSubmittedTaskCount;				// 最大允许同时处理的任务数

	public NatureThreadExecutor() {
		this(DEFAULT_MIN_THREADS, DEFAULT_MAX_THREADS);
	}

	public NatureThreadExecutor(int coreThread, int maxThreads) {
		this(coreThread, maxThreads, DEFAULT_MAX_QUEUE_SIZE);
	}

	public NatureThreadExecutor(int coreThread, int maxThreads, long keepAliveTime, TimeUnit unit) {
		this(coreThread, maxThreads, keepAliveTime, unit, maxThreads);
	}

	public NatureThreadExecutor(int coreThreads, int maxThreads, int queueCapacity) {
		this(coreThreads, maxThreads, queueCapacity, Executors.defaultThreadFactory());
	}

	public NatureThreadExecutor(int coreThreads, int maxThreads, int queueCapacity, ThreadFactory threadFactory) {
		this(coreThreads, maxThreads, DEFAULT_MAX_IDLE_TIME, TimeUnit.MILLISECONDS, queueCapacity, threadFactory);
	}

	public NatureThreadExecutor(int coreThreads, int maxThreads, long keepAliveTime, TimeUnit unit, int queueCapacity) {
		this(coreThreads, maxThreads, keepAliveTime, unit, queueCapacity, Executors.defaultThreadFactory());
	}

	public NatureThreadExecutor(int coreThreads, int maxThreads, long keepAliveTime, TimeUnit unit,
			int queueCapacity, ThreadFactory threadFactory) {
		this(coreThreads, maxThreads, keepAliveTime, unit, queueCapacity, threadFactory, new AbortPolicy());
	}

	public NatureThreadExecutor(int coreThreads, int maxThreads, long keepAliveTime, TimeUnit unit,
			int queueCapacity, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
		super(coreThreads, maxThreads, keepAliveTime, unit, new ExecutorQueue(), threadFactory, handler);
		((ExecutorQueue) getQueue()).setStandardThreadExecutor(this);

		submittedTasksCount = new AtomicInteger(0);
		
		// 最大并发任务限制： 队列buffer数 + 最大线程数 
		maxSubmittedTaskCount = queueCapacity + maxThreads; 
	}

	public void execute(Runnable command) {
		int count = submittedTasksCount.incrementAndGet();

		// 超过最大的并发任务限制，进行 reject
		// 依赖的LinkedTransferQueue没有长度限制，因此这里进行控制 
		if (count > maxSubmittedTaskCount) {
			submittedTasksCount.decrementAndGet();
			getRejectedExecutionHandler().rejectedExecution(command, this);
		}

		try {
			super.execute(command);
		} catch (RejectedExecutionException rx) {
			
			if (!((ExecutorQueue) getQueue()).force(command)) {
				submittedTasksCount.decrementAndGet();

				getRejectedExecutionHandler().rejectedExecution(command, this);
			}
		}
	}

	public int getSubmittedTasksCount() {
		return this.submittedTasksCount.get();
	}
	
	public int getMaxSubmittedTaskCount() {
		return maxSubmittedTaskCount;
	}

	protected void afterExecute(Runnable r, Throwable t) {
		submittedTasksCount.decrementAndGet();
	}
}


class ExecutorQueue extends LinkedTransferQueue<Runnable> {
	private static final long serialVersionUID = -265236426751004839L;
	NatureThreadExecutor threadPoolExecutor;

	public ExecutorQueue() {
		super();
	}

	public void setStandardThreadExecutor(NatureThreadExecutor threadPoolExecutor) {
		this.threadPoolExecutor = threadPoolExecutor;
	}

	// 注：代码来源于 tomcat 
	public boolean force(Runnable o) {
		if (threadPoolExecutor.isShutdown()) {
			throw new RejectedExecutionException("Executor not running, can't force a command into the queue");
		}
		// forces the item onto the queue, to be used if the task is rejected
		return super.offer(o);
	}

	// 注：tomcat的代码进行一些小变更 
	public boolean offer(Runnable o) {
		int poolSize = threadPoolExecutor.getPoolSize();

		
		if (poolSize == threadPoolExecutor.getMaximumPoolSize()) {
			return super.offer(o);
		}
		
		if (threadPoolExecutor.getSubmittedTasksCount() <= poolSize) {
			return super.offer(o);
		}
		
		if (poolSize < threadPoolExecutor.getMaximumPoolSize()) {
			return false;
		}
		
		return super.offer(o);
	}
}