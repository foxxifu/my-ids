package com.interest.ids.dev.db.manager.pool;

import java.util.concurrent.ThreadPoolExecutor;

import com.interest.ids.common.project.threadpool.NatureThreadExecutor;

public class DevDbPool {
	
	private static ThreadPoolExecutor pool = new NatureThreadExecutor();
	
	public static ThreadPoolExecutor getPool(){
		return pool;
	}

}
