package com.interest.ids.biz.web.dataintegrity.service;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.interest.ids.biz.web.dataintegrity.thread.KpiCalcTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.interest.ids.biz.web.dataintegrity.constant.KpiCalcTaskConstant;
import com.interest.ids.biz.web.dataintegrity.dao.KpiCalcTaskMapper;
import com.interest.ids.biz.web.dataintegrity.vo.KpiCalcTaskVo;
import com.interest.ids.common.project.bean.sm.KpiCalcTaskM;
import com.interest.ids.common.project.bean.sm.UserInfo;

@Component("kpiCalcTaskService")
public class KpiCalcTaskServiceImpl implements IKpiCalcTaskService {

    @Autowired
    private KpiCalcTaskMapper kpiReviseMapper;
    
    private static final Logger logger = LoggerFactory.getLogger(KpiCalcTaskServiceImpl.class);

    private static ThreadPoolExecutor threadPoolExecutor;

    private static BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(100);

    static {
        threadPoolExecutor = new ThreadPoolExecutor(5, 10, 600, TimeUnit.SECONDS, blockingQueue);
    }

    @Override
    @Transactional
    public void addKpiReviseTask(KpiCalcTaskM kpiRevise) {
        kpiReviseMapper.insertUseGeneratedKeys(kpiRevise);

        logger.info("create one KpiCalcTask: " + kpiRevise);
    }

    @Override
    @Transactional
    public void removeKpiReviseTask(KpiCalcTaskM kpiRevise) {

        if (kpiRevise == null) {
            return;
        }

        if (kpiRevise.getTaskStatus() == KpiCalcTaskConstant.UNDO) {
            kpiReviseMapper.delete(kpiRevise);

            logger.info("remove one KpiCalcTask: " + kpiRevise);
        } else {
            throw new RuntimeException("task status is not correct.");
        }
    }

    @Override
    @Transactional
    public void removeKpiReviseTask(Long taskId) {
        KpiCalcTaskM kpiRevise = kpiReviseMapper.selectByPrimaryKey(taskId);
        if (kpiRevise != null) {

            if (kpiRevise.getTaskStatus() == KpiCalcTaskConstant.UNDO) {
                kpiReviseMapper.delete(kpiRevise);

                logger.info("remove one KpiCalcTask: " + kpiRevise);
            } else {
                throw new RuntimeException("task status is not correct.");
            }
        } else {
            logger.info("The task: " + taskId + " which want to be removed doesn't exist.");
        }
    }

    @Override
    @Transactional
    public void removeKpiReviseTaskList(List<KpiCalcTaskM> kpiReviseList) {

        if (kpiReviseList != null) {
            for (KpiCalcTaskM kpiReviseM : kpiReviseList) {
                if (kpiReviseM == null) {
                    continue;
                }

                if (kpiReviseM.getTaskStatus() == KpiCalcTaskConstant.UNDO) {
                    kpiReviseMapper.delete(kpiReviseM);

                    logger.info("remove one KpiCalcTask: " + kpiReviseM);
                } else {
                    throw new RuntimeException("task status is not correct.");
                }
            }
        }
    }

    @Override
    @Transactional
    public void modifyKpiReviseTask(KpiCalcTaskM kpiRevise) {

        if (kpiRevise == null) {
            return;
        }

        kpiReviseMapper.updateByPrimaryKey(kpiRevise);
    }

    @Override
    public List<KpiCalcTaskVo> queryKpiRevises(UserInfo user, String stationName, String taskName, byte taskStatus) {

        if (user == null) {
            throw new RuntimeException("illegal argument.");
        }

        return kpiReviseMapper.selectTaskByAllCondition(user, stationName, taskName, taskStatus);
    }

    @Override
    public KpiCalcTaskM queryKpiReviseTask(Long taskId) {

        return kpiReviseMapper.selectByPrimaryKey(taskId);
    }

    @Override
    public void startExcuteKpiReviseTask(Long taskId, UserInfo user) {

        // 1. 更新任务状态为正在执行
        KpiCalcTaskM kpiRevise = queryKpiReviseTask(taskId);
        if (kpiRevise != null && (kpiRevise.getTaskStatus() == KpiCalcTaskConstant.UNDO|| 
                kpiRevise.getTaskStatus() == KpiCalcTaskConstant.ERROR)) {
            kpiRevise.setTaskStatus(KpiCalcTaskConstant.DOING);
            modifyKpiReviseTask(kpiRevise);
            
            // 2. 开始执行计算任务
            if (threadPoolExecutor.isShutdown()) {
                threadPoolExecutor = new ThreadPoolExecutor(5, 10, 600, TimeUnit.SECONDS, blockingQueue);
            }
            
            KpiCalcTask task = new KpiCalcTask(kpiRevise, user);
            threadPoolExecutor.execute(task);
        } else {
            logger.info("the KpiCalcTask which want to be started is not propable: " + kpiRevise);
        }
    }
}
