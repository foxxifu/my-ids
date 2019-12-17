package com.interest.ids.biz.kpicalc.kpi.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * 线程池管理类
 */
public class KpiThreadPoolManager {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private static KpiThreadPoolManager instance;

    // 线程池维护线程的最少数量
    private final static int CORE_POOL_SIZE = 5;

    // 线程池维护线程的最大数量
    private final static int MAX_POOL_SIZE = 50;

    // 线程池维护线程所允许的空闲时间
    private final static int KEEP_ALIVE_TIME = 0;

    // 线程池所使用的缓冲队列大小
    private final static int WORK_QUEUE_SIZE = 1500;

    // 任务调度周期
    private final static int TASK_QOS_PERIOD = 10;

    // 任务缓冲队列
    private Queue<Runnable> taskQueue = new LinkedList<>();

    /*
     * 线程池超出界线时将任务加入缓冲队列
     */
    final RejectedExecutionHandler handler = new RejectedExecutionHandler() {
        public void rejectedExecution(Runnable task, ThreadPoolExecutor executor) {
            if(task != null){
                taskQueue.offer(task);
            }
            else{
                log.error("KpiThreadPoolManager.handler.rejectedExecution(): task is null!");
            }
        }
    };

    /*
     * 将缓冲队列中的任务重新加载到线程池
     */
    final Runnable accessBufferThread = new Runnable() {
        public void run() {
            if(hasMoreAcquire()){
                try{
                    threadPool.execute(taskQueue.poll());
                }
                catch(Exception e){
                    log.error(e.getMessage() + ":task execute error!");
                }
            }
        }
    };

    //创建一个调度线程池
    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    //通过调度线程周期性的执行缓冲队列中任务
    final ScheduledFuture<?> taskHandler = scheduler.scheduleAtFixedRate(accessBufferThread, 0, TASK_QOS_PERIOD,
            TimeUnit.MILLISECONDS);
    //线程池
    final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(WORK_QUEUE_SIZE), this.handler);

    private KpiThreadPoolManager() {

    }

    /*
     * 线程池单例创建方法
     */
    public static synchronized KpiThreadPoolManager getInstance() {
        if(instance == null){
            instance = new KpiThreadPoolManager();
        }
        return instance;
    }

    /*
     * 消息队列检查方法
     */
    private boolean hasMoreAcquire() {
        return !taskQueue.isEmpty();
    }

    /*
     * 向线程池中添加任务方法, 有返回值
     */
    public <T> Future<T> addExecuteTask(Callable<T> task) {
        if(task != null){
            try{
                return threadPool.submit(task);
            }
            catch(Exception e){
                log.error(e.getMessage() + ":task execute error!");
            }
        }
        return null;
    }

    /*
     * 向线程池中添加任务方法
     */
    public void addExecuteTask(Runnable task) {
        if(task != null){
            try{
                threadPool.execute(task);
            }
            catch(Exception e){
                log.error(e.getMessage() + ":task execute error!");
            }
        }
    }
}
