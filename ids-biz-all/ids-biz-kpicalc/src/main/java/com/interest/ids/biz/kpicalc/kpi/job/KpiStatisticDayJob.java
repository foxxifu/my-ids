package com.interest.ids.biz.kpicalc.kpi.job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.interest.ids.biz.kpicalc.kpi.constant.KpiSchedulerStatus;
import com.interest.ids.biz.kpicalc.kpi.constant.KpiStatiticDeviceType;
import com.interest.ids.biz.kpicalc.kpi.calc.AbstractKpiStatistic;
import com.interest.ids.biz.kpicalc.kpi.calc.calculator.KpiCombinerDcDayCalculator;
import com.interest.ids.biz.kpicalc.kpi.calc.calculator.KpiInverterDayCalculator;
import com.interest.ids.biz.kpicalc.kpi.calc.calculator.KpiInverterMonthCalculator;
import com.interest.ids.biz.kpicalc.kpi.calc.calculator.KpiInverterYearCalculator;
import com.interest.ids.biz.kpicalc.kpi.calc.calculator.KpiMeterDayCalculator;
import com.interest.ids.biz.kpicalc.kpi.calc.calculator.KpiMeterMonthCalculator;
import com.interest.ids.biz.kpicalc.kpi.calc.calculator.KpiMeterYearCalculator;
import com.interest.ids.biz.kpicalc.kpi.calc.calculator.KpiStationDayCalculator;
import com.interest.ids.biz.kpicalc.kpi.calc.calculator.KpiStationMonthCalculator;
import com.interest.ids.biz.kpicalc.kpi.calc.calculator.KpiStationYearCalculator;
import com.interest.ids.biz.kpicalc.kpi.service.scheduler.IKpiSchedulerService;
import com.interest.ids.biz.kpicalc.kpi.util.KpiCalculatorFactory;
import com.interest.ids.biz.kpicalc.kpi.util.KpiStatisticFlowVar;
import com.interest.ids.biz.kpicalc.kpi.util.KpiThreadPoolManager;
import com.interest.ids.common.project.bean.kpi.StatisticsScheduleTaskM;
import com.interest.ids.common.project.constant.KpiConstants;
import com.interest.ids.common.project.constant.SchedulerBusiType;
import com.interest.ids.common.project.spring.context.SystemContext;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.common.project.utils.DateUtil;
import com.interest.ids.redis.caches.StationCache;

@Component("kpiStatisticDayJob")
public class KpiStatisticDayJob implements Job {

    private final static Logger logger = LoggerFactory.getLogger(KpiStatisticDayJob.class);

    private static IKpiSchedulerService schedulerService;
    private static KpiStatisticHourJob kpiStatisticHourJob;

    static {
        if (schedulerService == null) {
            schedulerService = (IKpiSchedulerService) SystemContext.getBean("kpiSchedulerService");
        }

        if (kpiStatisticHourJob == null) {
            kpiStatisticHourJob = (KpiStatisticHourJob) SystemContext.getBean("kpiStatisticHourJob");
        }
    }

    /**
     * 每天凌晨1点触发该任务的执行
     */
    @Override
    public void execute(JobExecutionContext jobContext) throws JobExecutionException {
        TimeZone timeZone = null;
        if (jobContext.getMergedJobDataMap().containsKey("timeZone")) {
            timeZone = (TimeZone) jobContext.getMergedJobDataMap().get("timeZone");
        }

        if (timeZone == null) {
            timeZone = TimeZone.getDefault();
        }

        executeJob(timeZone);
    }

    public void executeJob(TimeZone timeZone) {

        // 获取前一天起点的毫秒数
        Long day = DateUtil.getNowDayByTimeZone(timeZone) - 24 * 3600 * 1000;

        String taskStarTime = DateUtil.formatDateStrByLang(System.currentTimeMillis(), "");
        logger.info("Day job start to execute, start time: " + taskStarTime);

        // 1. 取得当前服务器管理电站
        // 非户用
        final List<String> sIds = StationCache.getStationsCodesByTimeZone(CommonUtil.getTimeZoneId(timeZone));
        if (null == sIds || sIds.size() == 0) {
            logger.error("can not get station in cache! (" + timeZone.getID() + ")");
            return;
        }

        // 2. 执行天统计任务前，先检查当天是否存在未执行完毕的小时任务
        finishCurrentDayHourTasks(sIds, day, timeZone);

        // 3. 获取待执行任务列表执行统计归档
        List<StatisticsScheduleTaskM> schedulerList = schedulerService.queryTodoSchedulers(sIds, day,
                KpiConstants.KPI_STATE_TYPE_DAY);
        executeDayStatistic(schedulerList, day, timeZone, true);

        String taskEndTime = DateUtil.formatDateStrByLang(System.currentTimeMillis(), "");
        logger.info("Day job execute over, end time: " + taskEndTime);

        // 4. 最后将当天之前未处理或者处理失败的任务进行统计归档
        finishEarlierTasks(sIds, day);
    }

    /**
     * 检查是否存在未完成以及执行有问题的小时任务，完成小时任务的统计
     * 
     * @param sIds
     * @param day
     */
    private void finishCurrentDayHourTasks(List<String> sIds, Long day, TimeZone timeZone) {

        Long lastHour = DateUtil.getDayLastSecond(day);
        List<StatisticsScheduleTaskM> schedulerList = schedulerService.queryTodoErrSchedulers(sIds, day, lastHour,
                KpiConstants.KPI_STATE_TYPE_HOUR);
        // 按时间分组为不同的任务组
        Map<Long, List<StatisticsScheduleTaskM>> schedulerMap = schedulerService.spliteScheduleToTimeMap(
                schedulerList, KpiConstants.KPI_STATE_TYPE_HOUR);

        //按时间进行排序
        List<Long> collectTimeList = new ArrayList<>(schedulerMap.keySet());
        Collections.sort(collectTimeList);
        
        for(Long key : collectTimeList){
            long nowHour = key;
            long nextHour = nowHour + 3600 * 1000;
            List<String> stationCodes = schedulerService.listSchedulerStateStationId(schedulerMap.get(key));
            kpiStatisticHourJob.executeHourStatBySchedule(nowHour, schedulerMap.get(key), timeZone, true);
            if (!"23".equals(DateUtil.formatDateStrByLang(nowHour, "HH")) && !schedulerMap.keySet().contains(nextHour)) {
                // 执行下一个小时的统计
                kpiStatisticHourJob.executeHourStatBySId(nextHour, stationCodes, timeZone);
            }
        }
    }

    /**
     * 分别归档设备日、月、年数据-->电站的日、月、年数据
     * 
     * @param schedulerList
     * @param day
     * @param timeZone
     * @param realTask
     */
    public void executeDayStatistic(List<StatisticsScheduleTaskM> schedulerList, final Long day,
                                    final TimeZone timeZone, boolean realTask) {
        if (schedulerList != null && schedulerList.size() > 0) {
            // 3.1. 根据统计的设备类型进行任务分组
            Map<String, List<StatisticsScheduleTaskM>> devTypeSchedulerMap = schedulerService
                    .spliteScheduleToBusiTypeMap(schedulerList);

            // 3.2. 锁定任务
            if (realTask) {
                schedulerService.setAllSchedularToStatus(schedulerList, KpiSchedulerStatus.DOING);
            }

            LinkedBlockingDeque<Future<KpiStatisticFlowVar>> taskQueue = new LinkedBlockingDeque<>();
            // 3.3. 先进行设备日、月、年数据归档
            for (Map.Entry<String, List<StatisticsScheduleTaskM>> entry : devTypeSchedulerMap.entrySet()) {
                String busiType = entry.getKey();
                final List<String> stationCodes = schedulerService.listSchedulerStateStationId(entry.getValue());
                // 逆变器天、月、年统计归档
                if (SchedulerBusiType.INVERTER.toString().equalsIgnoreCase(busiType)) {
                    taskQueue.add(KpiThreadPoolManager.getInstance().addExecuteTask(
                            new Callable<KpiStatisticFlowVar>() {
                                @Override
                                public KpiStatisticFlowVar call() throws Exception {
                                    AbstractKpiStatistic invDayTask = KpiCalculatorFactory.getService(
                                            KpiInverterDayCalculator.class, timeZone);
                                    AbstractKpiStatistic invMonthTask = KpiCalculatorFactory.getService(
                                            KpiInverterMonthCalculator.class, timeZone);
                                    AbstractKpiStatistic invYearTask = KpiCalculatorFactory.getService(
                                            KpiInverterYearCalculator.class, timeZone);

                                    // 任务间连续性设置
                                    invDayTask.setNextCaculator(invMonthTask);
                                    invMonthTask.setNextCaculator(invYearTask);
                                    return invDayTask.kpiCalculate(stationCodes, day);
                                }
                            }));
                }
                // 直流汇流箱天统计归档
                else if (SchedulerBusiType.COMBINERDC.toString().equalsIgnoreCase(busiType)) {
                    taskQueue.add(KpiThreadPoolManager.getInstance().addExecuteTask(
                            new Callable<KpiStatisticFlowVar>() {
                                @Override
                                public KpiStatisticFlowVar call() throws Exception {
                                    AbstractKpiStatistic combinerDcDayTask = KpiCalculatorFactory.getService(
                                            KpiCombinerDcDayCalculator.class, timeZone);
                                    return combinerDcDayTask.kpiCalculate(stationCodes, day);
                                }
                            }));
                }
                // 电表数据归档
                else if (KpiStatiticDeviceType.METER.toString().equalsIgnoreCase(busiType)) {
                    taskQueue.add(KpiThreadPoolManager.getInstance().addExecuteTask(
                            new Callable<KpiStatisticFlowVar>() {
                                @Override
                                public KpiStatisticFlowVar call() throws Exception {
                                    AbstractKpiStatistic meterDayTask = KpiCalculatorFactory.getService(
                                            KpiMeterDayCalculator.class, timeZone);
                                    AbstractKpiStatistic meterMonthTask = KpiCalculatorFactory.getService(
                                            KpiMeterMonthCalculator.class, timeZone);
                                    AbstractKpiStatistic meterYearTask = KpiCalculatorFactory.getService(
                                            KpiMeterYearCalculator.class, timeZone);

                                    meterDayTask.setNextCaculator(meterMonthTask);
                                    meterMonthTask.setNextCaculator(meterYearTask);
                                    return meterDayTask.kpiCalculate(stationCodes, day);
                                }
                            }));
                } else if (KpiStatiticDeviceType.ENERGY_STORE.toString().equalsIgnoreCase(busiType)) {
                    // TODO 储能以后再处理
                }
            }

            // 3.4. 电站日、月、年数据归档
            List<String> stationIds = schedulerService.listSchedulerStateStationId(schedulerList);
            KpiStatisticFlowVar stationStatResult = finishStationDayCalculation(stationIds, day, timeZone);

            // 3.5. 待所有任务执行完毕后，处理任务列表
            try {
                boolean flag = stationStatResult.getStatStatus();
                for (Future<KpiStatisticFlowVar> future : taskQueue) {
                    flag = future.get().getStatStatus() && flag;
                }

                if (realTask) {
                    if (flag) {
                        // 任务成功执行，删除任务列表
                        schedulerService.deleteSchedulers(schedulerList);
                    } else {
                        // 存在任务执行失败时，将任务状态设置为-1
                        schedulerService.setAllSchedularToStatus(schedulerList, KpiSchedulerStatus.ERROR);
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 电站日、月、年统计
     * @param stationCodes
     * @param day
     * @return
     */
    public KpiStatisticFlowVar finishStationDayCalculation(List<String> stationCodes, Long day, TimeZone timeZone){
     
        AbstractKpiStatistic stationDayTask = KpiCalculatorFactory.getService(KpiStationDayCalculator.class,
                timeZone);
        AbstractKpiStatistic stationMonthTask = KpiCalculatorFactory.getService(KpiStationMonthCalculator.class,
                timeZone);
        AbstractKpiStatistic stationYearTask = KpiCalculatorFactory.getService(KpiStationYearCalculator.class,
                timeZone);

        stationDayTask.setNextCaculator(stationMonthTask);
        stationMonthTask.setNextCaculator(stationYearTask);
        
        return stationDayTask.kpiCalculate(stationCodes, day);
    }
    
    /**
     * 待当天数据统计归档后，如果历史统计中存在未完成的，或者执行有问题的，再进行统计归档一次
     * 
     * @param stationCodes
     * @param currentDay
     */
    private void finishEarlierTasks(List<String> stationCodes, Long currentDay) {
        // TODO 暂时不实现，属于异常情况，理论上不允许这种异常情况出现
    }

}
