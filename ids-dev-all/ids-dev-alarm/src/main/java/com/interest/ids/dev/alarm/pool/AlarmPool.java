package com.interest.ids.dev.alarm.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import com.interest.ids.common.project.threadpool.NamedThreadFactory;
import com.interest.ids.common.project.threadpool.NatureThreadExecutor;

public class AlarmPool {
	
	public static final String THREAD_NAME = "alarm";
	
	private final static ThreadPoolExecutor pool = 
			new NatureThreadExecutor(1, 4, 2048, new NamedThreadFactory(THREAD_NAME));
	
	
	public static ExecutorService getPool(){
		return pool;
	}
}
