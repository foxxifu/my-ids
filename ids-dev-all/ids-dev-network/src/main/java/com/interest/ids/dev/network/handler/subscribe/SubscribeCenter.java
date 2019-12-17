package com.interest.ids.dev.network.handler.subscribe;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.dev.network.handler.subscribe.DevSubscriptionTask.SubscriptionTask;
import com.interest.ids.dev.network.modbus.command.CommunicateCommand;
import com.interest.ids.dev.network.modbus.command.SubscribeCmd;
import com.interest.ids.dev.network.modbus.command.SupplementCmd;
import com.interest.ids.dev.network.modbus.command.WholeCallCmd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author lhq
 * 只做订阅处理
 */
public class SubscribeCenter {

    private static final Logger log = LoggerFactory.getLogger(SubscribeCenter.class);

    private BlockingQueue<DevSubscriptionTask> tasks = new LinkedBlockingQueue<DevSubscriptionTask>();

    public static final SubscribeCenter center = new SubscribeCenter();

    public static SubscribeCenter getInstance() {
        return center;
    }

    private volatile boolean continueWork = true;

    private SubscribeCenter() {
        start();
    }

    public void pushTask(DevSubscriptionTask task) {
        if (task != null && task.getDev() != null) {
            tasks.add(task);
        }
    }

    public void start() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (continueWork) {
                    try {
                        final DevSubscriptionTask task = tasks.take();
                        log.info("execute one task: {}", task);
                        SubscriptionTask taskType = task.getTask();
                        try {
                            switch (taskType) {
                                case SUBSCRIBE:
                                    subscribe(task.getDev());
                                    break;
                                case WHOLECALL:
                                    wholeCall(task.getDev());
                                    break;
                                case Communicate:
                                    communicateConfig(task.getDev());
                                    break;
                                case CommunicateExecute:
                                    communicateExexute(task.getDev());
                                    break;
                                case Supplement:
                                    supplement(task.getDev());
                                    break;
                                default:
                                    break;
                            }
                        } catch (Exception e) {
                            log.error("subscribe the task error:", e);
                        }
                    } catch (Exception e) {
                        log.error("execute the task error", e);
                    }
                }
            }
        }, "subscribeCenter").start();
    }

    public void subscribe(DeviceInfo dev) {
        SubscribeCmd.subscribe(dev);
    }

    /**
     *总召
     */
    public void wholeCall(DeviceInfo dev) {
        WholeCallCmd.wholeCall(dev);
    }

    /**
     * 通信任务配置
     * @param dev
     */
    public void communicateConfig(DeviceInfo dev) {
        CommunicateCommand.configure(dev);
    }

    /**
     * 通信任务执行
     * @param dev
     */
    public void communicateExexute(DeviceInfo dev) {
//        CommunicateCommand.execute(dev);
    }

    /**
     * 补采
     * @param dev
     */
    public void supplement(DeviceInfo dev) {
//        SupplementCmd.supplement(dev);
    }

}
