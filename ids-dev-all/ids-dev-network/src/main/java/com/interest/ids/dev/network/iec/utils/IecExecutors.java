package com.interest.ids.dev.network.iec.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.interest.ids.common.project.threadpool.AbortPolicyWithReport;
import com.interest.ids.common.project.threadpool.NamedThreadFactory;




/**
 * 
 * @author lhq
 *
 *
 */
public class IecExecutors {
	
	public static IecExecutors pool = null;
	
	public IecExecutors(){
		init();
	}
	
	public synchronized static IecExecutors getInstance() {
		if(pool == null){
			pool = new IecExecutors();
		}
        return pool;
    }
	//执行定时任务的线程
	private ScheduledThreadPoolExecutor sharedScheduledPool = null;
	//执行104任务的线程
	private static ThreadPoolExecutor masterExecutor = null;
	
	
	public void init(){
		sharedScheduledPool = new ScheduledThreadPoolExecutor(5);
		masterExecutor = new ThreadPoolExecutor(20, 20, 
				0, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(1000), 
				new NamedThreadFactory("IEC104_POOL"),
				new AbortPolicyWithReport("IEC104_POOL"));
	}
	
	public void executeTask(Runnable task){
		masterExecutor.execute(task);
	}
	
	public ScheduledThreadPoolExecutor getSharedScheduledPool(){
		return sharedScheduledPool;
	}

}

