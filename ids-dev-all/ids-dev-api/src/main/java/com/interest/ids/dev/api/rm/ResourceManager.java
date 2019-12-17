package com.interest.ids.dev.api.rm;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadPoolExecutor;

import com.interest.ids.common.project.threadpool.NamedThreadFactory;
import com.interest.ids.common.project.threadpool.NatureThreadExecutor;


/**
 * 
 * @author lhq
 *
 *
 */
public class ResourceManager {
	
	
	CopyOnWriteArrayList<ThreadPoolExecutor> pools = new CopyOnWriteArrayList<>();
	
	public void register(ThreadPoolExecutor pool){
		
	}
	
	public ThreadPoolExecutor createThreadPool(String name,int size,int queueSize){
		ThreadPoolExecutor pool = new NatureThreadExecutor(size, size, queueSize, new NamedThreadFactory(name));
		return pool;
	}
	
	public void executeTask(Runnable task){
		for(int i=0;i<pools.size();i++){
			ThreadPoolExecutor pool = pools.get(i);
		}
	}
	
	private boolean isFree(ThreadPoolExecutor pool){
		
		int activeCount = pool.getActiveCount();
		int max = pool.getMaximumPoolSize();
		int queueSize = pool.getQueue().size();
		int coreSize = pool.getCorePoolSize();
		int poolSize = pool.getPoolSize();
		
		
		return true;
		
	}
	
	
	public void stop(){
		
	}
}
