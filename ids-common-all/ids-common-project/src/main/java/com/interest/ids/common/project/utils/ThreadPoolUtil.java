package com.interest.ids.common.project.utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadPoolUtil {

    private static final Logger log = LoggerFactory.getLogger(ThreadPoolUtil.class);

    private static ThreadPoolUtil instance;

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
    private Queue<Runnable> taskQueue = new LinkedList<Runnable>();

    /**
     * 线程池超出界线时将任务加入缓冲队列
     */
    final RejectedExecutionHandler executionHandler = new RejectedExecutionHandler() {
        
        @Override
        public void rejectedExecution(Runnable task, ThreadPoolExecutor executor) {

            if (task != null) {
                taskQueue.offer(task);
            } else {
                log.error("ThreadPoolUtil.handler.rejectedExecution(): task is null!");
            }

        }

    };

    /**
     * 创建一个调度线程池
     */
    final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(6);

    /**
     * 将缓冲队列中的任务重新加载到线程池
     */
    final Runnable executeWaitTask = new Runnable() {

        public void run() {
            if (hasMoreTask()) {
                try {
                    threadPool.execute(taskQueue.poll());
                } catch (Exception e) {
                    log.error("ThreadPoolUtile execute Task error! error msg : " + e.getMessage());
                }
            }
        }

    };
    
    /**
     * 通过调度线程周期性的执行缓冲队列中任务
     */
    final ScheduledFuture<?> taskHandler = executorService.scheduleAtFixedRate(
            executeWaitTask, 0, TASK_QOS_PERIOD, TimeUnit.MILLISECONDS);

    /**
     * 线程池
     */
    final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(WORK_QUEUE_SIZE), this.executionHandler);


    /**
     * 单例方法
     * 
     * @return ThreadPoolUtil
     */
    public static synchronized ThreadPoolUtil getInstance() {
        if (instance == null) {
            instance = new ThreadPoolUtil();
        }
        return instance;
    }

    /**
     * 判断任务队列中是否存在需要执行的任务
     * 
     * @return boolean
     */
    private boolean hasMoreTask() {
        return !taskQueue.isEmpty();
    }

    /**
     * 向线程池中增加Callable<T>任务
     * 
     * @param task
     *            Callable<T> task
     * @return T
     */
    public <T> Future<T> addCallableTask(Callable<T> task) {
        if (task != null) {
            try {
                return threadPool.submit(task);
            } catch (Exception e) {
                log.error("ThreadPoolUtile execute Callable Task error! error msg : " + e.getMessage());
            }
        }
        return null;
    }

    /**
     * 向线程池中增加Runnable线程任务
     * 
     * @param task
     *            Runnable Task
     */
    public void addRunnableTask(Runnable task) {
        if (task != null) {
            try {
                threadPool.execute(task);
            } catch (Exception e) {
                log.error("ThreadPoolUtile execute Runnable task error! error msg : " + e.getMessage());
            }
        }
    }
}
