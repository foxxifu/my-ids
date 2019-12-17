package com.interest.ids.biz.web.dataintegrity.thread;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.interest.ids.biz.kpicalc.kpi.job.KpiStatisticDayJob;
import com.interest.ids.biz.kpicalc.kpi.job.KpiStatisticHourJob;
import com.interest.ids.biz.web.dataintegrity.constant.KpiCalcTaskConstant;
import com.interest.ids.biz.web.dataintegrity.service.IKpiCalcTaskService;
import com.interest.ids.biz.web.dataintegrity.websocket.WebSocketKpiCalcTaskHandler;
import com.interest.ids.biz.web.dataintegrity.websocket.WebSocketResultBean;
import com.interest.ids.common.project.bean.kpi.StatisticsScheduleTaskM;
import com.interest.ids.common.project.bean.sm.KpiCalcTaskM;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.constant.SchedulerBusiType;
import com.interest.ids.common.project.spring.context.SystemContext;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.common.project.utils.DateUtil;
import com.interest.ids.commoninterface.service.kpi.IKpiCommonService;
import com.interest.ids.redis.caches.StationCache;

/**
 * 任务执行线程
 * 
 * @author zl
 * @date 2018-3-9
 */
public class KpiCalcTask implements Runnable {

    private Logger logger = LoggerFactory.getLogger(KpiCalcTask.class);

    private IKpiCommonService kpiCommonService;

    private KpiStatisticHourJob kpiStatisticHourJob;

    private KpiStatisticDayJob kpiStatisticDayJob;

    private KpiCalcTaskM kpiRevise;
    
    private IKpiCalcTaskService kpiCalcTaskService;

    private WebSocketKpiCalcTaskHandler handler;
    
    private UserInfo user;

    @Override
    public void run() {
        boolean flag = false;
        
        if (kpiRevise != null) {
            flag = executeJobByUser(kpiRevise.getStationCode(), kpiRevise.getStartTime(), kpiRevise.getEndTime());
            
        }
        
        JSONObject jsonStr = new JSONObject();
        jsonStr.put("taskId", kpiRevise.getId());
        if (flag) {
            jsonStr.put("taskStatus", KpiCalcTaskConstant.COMPLTE);
            // 更新任务状态
            kpiRevise.setTaskStatus(KpiCalcTaskConstant.COMPLTE);
        } else {
            jsonStr.put("taskStatus", KpiCalcTaskConstant.ERROR);
            kpiRevise.setTaskStatus(KpiCalcTaskConstant.ERROR);
        }

        // 根据执行结果更新数据库任务状态
        kpiCalcTaskService.modifyKpiReviseTask(kpiRevise);
        // 将执行结果返回给客户端
        WebSocketResultBean resultBean = new WebSocketResultBean();
        resultBean.setData(jsonStr);
        handler.sendMessageToClient(user, resultBean);
    }

    public KpiCalcTask(KpiCalcTaskM kpiRevise, UserInfo user) {
        
        this.kpiRevise = kpiRevise;
        this.user = user;
    }

    public KpiCalcTaskM getKpiRevise() {
        return kpiRevise;
    }

    public void setKpiRevise(KpiCalcTaskM kpiRevise) {
        this.kpiRevise = kpiRevise;
    }
    
    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    /**
     * 手动计算
     * 
     * @param stationCode
     * @param startTime
     * @param endTime
     */
    public boolean executeJobByUser(String stationCode, Long startTime, Long endTime) {

        initDepencyObjc();

        boolean executeSucc = true;

        if (startTime > endTime) {
            logger.warn("");
            throw new RuntimeException("illegal arguments: startTime > endTime[startTime:" + startTime + ", endTime:"
                    + endTime);
        }

        logger.info("Start to execute mannual calculation task.");

        // 1. 获取电站下可参与计算的设备类型
        List<Integer> stationAllDevTypes = kpiCommonService.selectStationDeviceTypes(stationCode);
        if (CommonUtil.isNotEmpty(stationAllDevTypes)) {
            Set<String> busiTypes = new HashSet<>();
            for (Integer deviceTypeId : stationAllDevTypes) {
                if (DevTypeConstant.INVERTER_DEV_TYPE.equals(deviceTypeId)
                        || DevTypeConstant.CENTER_INVERT_DEV_TYPE.equals(deviceTypeId)
                        || DevTypeConstant.HOUSEHOLD_INVERTER_DEV_TYPE.equals(deviceTypeId)) {

                    busiTypes.add(SchedulerBusiType.INVERTER.name());
                } else if (DevTypeConstant.EMI_DEV_TYPE_ID.equals(deviceTypeId)) {

                    busiTypes.add(SchedulerBusiType.ENVIRONMENT.name());
                } else if (DevTypeConstant.BOX_DEV_TYPE.equals(deviceTypeId)) {

                    busiTypes.add(SchedulerBusiType.TRANSFORMER.name());
                } else if (DevTypeConstant.ACJS_DEV_TYPE.equals(deviceTypeId)) {

                    busiTypes.add(SchedulerBusiType.COMBINER.name());
                } else if (DevTypeConstant.DCJS_DEV_TYPE.equals(deviceTypeId)) {

                    busiTypes.add(SchedulerBusiType.COMBINERDC.name());
                } else if (DevTypeConstant.GATEWAYMETER_DEV_TYPE_ID.equals(deviceTypeId)) {

                    busiTypes.add(SchedulerBusiType.METER.name());
                }
            }

            // 计算有多少个小时计算点
            List<Long> calcHours = new ArrayList<>();
            for (Long hour = startTime; hour <= endTime;) {

                calcHours.add(hour);
                hour = hour + 3600 * 1000;
            }

            // 2. 构造计算任务列表并执行小时任务
            StationInfoM station = StationCache.getStation(stationCode);
            TimeZone timeZone = TimeZone.getDefault();
            if (station != null) {
                timeZone = CommonUtil.getTimeZoneById(station.getTimeZone());
            }

            List<Long> calcDays = new ArrayList<>();

            List<StatisticsScheduleTaskM> schedulerList = new LinkedList<>();
            for (Long hour : calcHours) {

                Long day = DateUtil.getBeginOfDayTimeByMill(hour, timeZone);
                if (!calcDays.contains(day)) {
                    calcDays.add(day);
                }

                for (String busiType : busiTypes) {
                    StatisticsScheduleTaskM schedulerParam = new StatisticsScheduleTaskM();
                    schedulerParam.setBusiType(busiType);
                    schedulerParam.setStationCode(stationCode);
                    schedulerParam.setStatDate(hour);
                    
                    schedulerList.add(schedulerParam);
                }

                try {
                    // 执行小时任务
                    kpiStatisticHourJob.executeHourStatBySchedule(hour, schedulerList, timeZone, false);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);

                    executeSucc = false;
                    break;
                } finally {
                    schedulerList.clear();
                }
            }

            // 3. 构造并执行天任务：确保小时任务都执行成功，否则不执行天任务，返回执行状态
            if (executeSucc) {
                schedulerList.clear();
                for (Long day : calcDays) {
                    for (String busiType : busiTypes) {
                        StatisticsScheduleTaskM schedulerParam = new StatisticsScheduleTaskM();
                        schedulerParam.setBusiType(busiType);
                        schedulerParam.setStationCode(stationCode);
                        schedulerParam.setStatDate(day);
                        
                        schedulerList.add(schedulerParam);
                    }

                    try {
                        kpiStatisticDayJob.executeDayStatistic(schedulerList, day, timeZone, false);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);

                        executeSucc = false;
                        break;
                    } finally {
                        schedulerList.clear();
                    }
                }
            }
        }

        return executeSucc;
    }

    /**
     * 初始化依赖的对象
     */
    private void initDepencyObjc() {

        kpiCommonService = (IKpiCommonService) SystemContext.getBean("kpiCommonService");

        kpiStatisticHourJob = (KpiStatisticHourJob) SystemContext.getBean("kpiStatisticHourJob");

        kpiStatisticDayJob = (KpiStatisticDayJob) SystemContext.getBean("kpiStatisticDayJob");
        
        kpiCalcTaskService = (IKpiCalcTaskService)SystemContext.getBean("kpiCalcTaskService");
        
        handler = (WebSocketKpiCalcTaskHandler)SystemContext.getBean("webSocketKpiReviseHandler");
    }
}
