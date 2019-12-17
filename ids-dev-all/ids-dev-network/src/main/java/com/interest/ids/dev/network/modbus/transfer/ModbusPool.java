package com.interest.ids.dev.network.modbus.transfer;

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
public class ModbusPool {
	
	private static ThreadPoolExecutor important_task = null;
	
	private static ThreadPoolExecutor longTimePool = null;
	
	private static ScheduledThreadPoolExecutor schedulerPool = null;
	
	private static ThreadPoolExecutor asynMessagePool = null;
	
	private static ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(10,new NamedThreadFactory("modbusSharedPool"));

	
	private final static int coreSize = Runtime.getRuntime().availableProcessors();
	
	
	public static void schedule(Runnable runnable,long delay){
		pool.schedule(runnable, delay, TimeUnit.MILLISECONDS);
	}
	
	public static void executeImportTask(Runnable runnable){
		
		if(important_task == null){
			initImportPool();
		}
		important_task.execute(runnable);
	}
	
	public static void executeLongTimeTask(Runnable runnable){
		if(longTimePool == null){
			initLongTimePool();
		}
		longTimePool.execute(runnable);
	}
	
	public static void executeSchedulerPool(Runnable runnable,Long delay,TimeUnit unit){
		if(schedulerPool==null){
			initSchedulerPool();
		}
		schedulerPool.schedule(runnable, delay, unit);
	}
	
	public static void executeAsynTask(Runnable runnable){
        
        if(asynMessagePool == null){
            initAsynMessagePool();
        }
        asynMessagePool.execute(runnable);
    }
	
	
	public static void initSchedulerPool(){
		schedulerPool = new ScheduledThreadPoolExecutor(10);
	}
	
	public static void initImportPool(){
		synchronized(ModbusPool.class){
			if(important_task == null){
				important_task = new ThreadPoolExecutor(coreSize*2, coreSize*2, 3, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(1000),
						new NamedThreadFactory("import_modbus_pool"),
						new AbortPolicyWithReport("import_modbus_pool"));
			}
		}
		
	}
	
	public static void initLongTimePool(){
		synchronized(ModbusPool.class){
			if(longTimePool == null){
				longTimePool = new ThreadPoolExecutor(coreSize*2, coreSize*2, 3, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(1000),
						new NamedThreadFactory("long_modbus_pool"),
						new AbortPolicyWithReport("long_modbus_pool"));
			}
		}
		
	}

	public static void initAsynMessagePool(){
        synchronized(ModbusPool.class){
            if(asynMessagePool == null){
                asynMessagePool = new ThreadPoolExecutor(coreSize, coreSize*2, 3, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(8096),
                        new NamedThreadFactory("asyn_message_pool"),
                        new AbortPolicyWithReport("asyn_message_pool"));
            }
        }
    }
	
	public static ScheduledThreadPoolExecutor getSchedulerPool() {
		return schedulerPool;
	}
}