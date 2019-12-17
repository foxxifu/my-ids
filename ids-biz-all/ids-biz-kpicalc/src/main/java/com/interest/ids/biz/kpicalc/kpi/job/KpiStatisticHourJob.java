package com.interest.ids.biz.kpicalc.kpi.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;

import com.interest.ids.biz.kpicalc.kpi.util.KpiStatisticUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.interest.ids.biz.kpicalc.kpi.constant.KpiSchedulerStatus;
import com.interest.ids.biz.kpicalc.kpi.calc.AbstractKpiStatistic;
import com.interest.ids.biz.kpicalc.kpi.calc.calculator.KpiEmiHourCalculator;
import com.interest.ids.biz.kpicalc.kpi.calc.calculator.KpiInverterHourCalculator;
import com.interest.ids.biz.kpicalc.kpi.calc.calculator.KpiMeterHourCalculator;
import com.interest.ids.biz.kpicalc.kpi.calc.calculator.KpiStationHourCalculator;
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

@Component("kpiStatisticHourJob")
public class KpiStatisticHourJob implements Job {

    private final static Logger logger = LoggerFactory.getLogger(KpiStatisticHourJob.class);

    private static IKpiSchedulerService schedulerService;

    static {
        if (schedulerService == null) {
            schedulerService = (IKpiSchedulerService) SystemContext.getBean("kpiSchedulerService");
        }
    }

    /**
     * 小时定时任务， 每小时触发执行
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

    /**
     * 执行小时级定时任务
     * 
     * @param timeZone
     */
    public void executeJob(TimeZone timeZone) {

        String taskStarTime = DateUtil.formatDateStrByLang(System.currentTimeMillis(), "");
        logger.info("Kpi Hour job started at: " + taskStarTime);

        // 获取上一小时起点毫秒数
        Long hour = DateUtil.getBeginOfHourTime(timeZone) - 3600 * 1000;

        // 1. 取得当前服务器管理电站（只取当前时区的电站）
        List<String> stationCodes = StationCache.getStationsCodesByTimeZone(CommonUtil.getTimeZoneId(timeZone));
        if (null == stationCodes || stationCodes.size() == 0) {
            logger.warn("can not get station in cache or this timeZone = " + timeZone.getID()
                    + " has no station!");
            return;
        }

        // 2. 根据当前小时获取当前小时任务列表
        List<StatisticsScheduleTaskM> schedulerList = schedulerService.queryTodoSchedulers(stationCodes, hour,
                KpiConstants.KPI_STATE_TYPE_HOUR);

        // 3. 执行统计
        executeHourStatBySchedule(hour, schedulerList, timeZone, true);

        String taskEndTime = DateUtil.formatDateStrByLang(System.currentTimeMillis(), "");
        logger.info("Kpi Hour job ended at: " + taskEndTime);
    }

    public void executeHourStatBySchedule(final Long hour, List<StatisticsScheduleTaskM> schedulerList,
            final TimeZone timeZone, boolean realTask) {
        if (schedulerList != null && schedulerList.size() > 0) {
            // 4. 根据设备类型拆分成不同独自的任务<devType,taskList>
            Map<String, List<StatisticsScheduleTaskM>> devScheduleTodoMap = schedulerService
                    .spliteScheduleToBusiTypeMap(schedulerList);
            // 5. 更改任务状态为正在处理，锁定待处理任务
            if (realTask) {
            	// 重新启动任务时需要扫描该表中正在执行的任务，进行立即执行
                schedulerService.setAllSchedularToStatus(schedulerList, KpiSchedulerStatus.DOING);
            }
            // 6. 统计不同设备性能数据以小时维度进行归档
            LinkedBlockingDeque<Future<KpiStatisticFlowVar>> taskQueue = new LinkedBlockingDeque<>();
            for (final Map.Entry<String, List<StatisticsScheduleTaskM>> entry : devScheduleTodoMap.entrySet()) {
                // 获取对应设备类型下的电站编号集合
                final List<String> stationCodes = schedulerService.listSchedulerStateStationId(entry.getValue());
                
                if (CommonUtil.isNotEmpty(stationCodes)) {
                    taskQueue.add(KpiThreadPoolManager.getInstance().addExecuteTask(
                            new Callable<KpiStatisticFlowVar>() {
                                @Override
                                public KpiStatisticFlowVar call() throws Exception {
                                    AbstractKpiStatistic statService = getDeviceHourStatSer(entry.getKey(), timeZone);
                                    if (statService != null) {
                                        KpiStatisticUtil.getCurrentTheadKpiStatFlowVar(statService).setStatDevType(
                                                entry.getKey().toUpperCase());

                                        return statService.kpiCalculate(stationCodes, hour);
                                    }

                                    return new KpiStatisticFlowVar(false);
                                }
                            }));
                }
            }

            // 7. 待所有设备小时任务执行完毕，以小时维度归档电站数据
            AbstractKpiStatistic stationHourService = KpiCalculatorFactory.getService(KpiStationHourCalculator.class,
                    timeZone);

            for (Future<KpiStatisticFlowVar> future : taskQueue) {
                try {
                    KpiStatisticFlowVar devStatFlowVar = future.get();
                    // 设备小时数据统计完成后，将统计数据传递到电站统计中避免从数据库查询
                    if (devStatFlowVar != null && devStatFlowVar.getStatStatus()) {
                        stationHourService.transferDataToNext(devStatFlowVar.getStatDevType(), devStatFlowVar
                                .getDataListPool().get("data"));
                    }
                    logger.info("device hour job execution over[ statistic time:" + hour + ", info:" + devStatFlowVar
                            + "]");
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }

            // 获取电站统计列表
            List<String> sCodes = schedulerService.listSchedulerStateStationId(schedulerList);
            KpiStatisticFlowVar stationStatFlowVar = stationHourService.kpiCalculate(sCodes, hour);

            // 8. 小时定时任务完成后，删除任务列表的数据
            if (realTask) {
                dealScheduleList(schedulerList, hour, stationStatFlowVar.getStatDevType(),
                        stationStatFlowVar.getStatStatus());
            }

            logger.info("station hour job execution over[ statistic time:" + hour + ", info:" + stationStatFlowVar
                    + "]");
        }
    }

    /**
     * 模拟一些任务进行执行
     * 
     * @param hour
     * @param sIds
     * @param timeZone
     */
    public void executeHourStatBySId(final Long hour, List<String> sIds, final TimeZone timeZone) {
        if (CommonUtil.isNotEmpty(sIds)) {
            List<StatisticsScheduleTaskM> notRealSchule = new ArrayList<>();
            for (String sId : sIds) {
                // 逆变器任务
                notRealSchule.add(new StatisticsScheduleTaskM(sId, SchedulerBusiType.INVERTER.toString(), hour));

                // 电表任务
                notRealSchule.add(new StatisticsScheduleTaskM(sId, SchedulerBusiType.METER.toString(), hour));

                // 环境监测仪任务
                notRealSchule.add(new StatisticsScheduleTaskM(sId, SchedulerBusiType.ENVIRONMENT.toString(), hour));

            }
            executeHourStatBySchedule(hour, notRealSchule, timeZone, false);
        }
    }

    /**
     * 根据设备类型，获得具体的数据统计处理对象
     * 
     * @param devFlag
     * @param timeZone
     * @return
     */
    private AbstractKpiStatistic getDeviceHourStatSer(String devFlag, TimeZone timeZone) {
        AbstractKpiStatistic statSer = null;

        // 逆变器(3种： 组串式， 集中式， 户用)
        if (SchedulerBusiType.INVERTER.toString().equalsIgnoreCase(devFlag)) {
            statSer = KpiCalculatorFactory.getService(KpiInverterHourCalculator.class, timeZone);
        }

        // 电表 (2种： 户用电表、关口电表)
        else if (SchedulerBusiType.METER.toString().equalsIgnoreCase(devFlag)) {
            statSer = KpiCalculatorFactory.getService(KpiMeterHourCalculator.class, timeZone);
        }

        // 环境监测仪
        else if (SchedulerBusiType.ENVIRONMENT.toString().equalsIgnoreCase(devFlag)) {
            statSer = KpiCalculatorFactory.getService(KpiEmiHourCalculator.class, timeZone);
        }

        return statSer;
    }

    /**
     * 根据任务执行情况进行任务列表的处理
     * 
     * @param list
     * @param hour
     * @param scheType
     * @param dealStatus
     */
    private void dealScheduleList(List<StatisticsScheduleTaskM> list, Long hour, String scheType, boolean dealStatus) {
        // 删除当前任务
        if (dealStatus) {
            logger.info("stat hour " + hour + "(" + scheType + ") ! scheduler list =" + list);
            schedulerService.deleteSchedulers(list);
        }
        // 将定时任务置为-1
        else {
            logger.warn("stat hour " + hour + "(" + scheType + ") occer some errors! scheduler list =" + list);
            schedulerService.setAllSchedularToStatus(list, KpiSchedulerStatus.ERROR);
        }
    }
}
