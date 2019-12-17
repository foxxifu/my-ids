package com.interest.ids.biz.kpicalc.kpi.service.scheduler;

import com.interest.ids.biz.kpicalc.kpi.constant.KpiSchedulerStatus;
import com.interest.ids.common.project.bean.kpi.StatisticsScheduleTaskM;

import java.util.List;
import java.util.Map;

public interface IKpiSchedulerService {

    /**
     * 根据某天某个小时获取定时任务
     *
     * @param sIds
     *            电站集合
     * @param statTime
     *            开始时间
     * @return
     */
    List<StatisticsScheduleTaskM> queryTodoSchedulers(List<String> sIds, Long statTime, Integer statType);

    /**
     * 根据某天某个小时范围获取定时任务
     * @param sIds
     * @param sTime
     * @param eTime
     * @param statType
     * @return
     */
    List<StatisticsScheduleTaskM> queryTodoSchedulers(List<String> sIds, Long sTime, Long eTime,
                                                      Integer statType);

    /**
     * 根据某天某个小时范围获取定时任务
     */
    List<StatisticsScheduleTaskM> queryTodoErrSchedulers(List<String> sIds, Long sTime, Long eTime,
                                                         Integer statType);

    /**
     * 将列表中所有的任务都置为某种状态
     *
     * @param doneList
     *            任务列表
     * @param status
     *            状态
     */
    void setAllSchedularToStatus(List<StatisticsScheduleTaskM> doneList, KpiSchedulerStatus status);

    /**
     * 获取任务列表中的sId集合
     *
     * @param schedulers
     *            任务列表
     * @return sId集合
     */
    public List<String> listSchedulerStateStationId(List<StatisticsScheduleTaskM> schedulers);

    /**
     * 将任务列表拆分成以统计类型作为key， 以任务列表作为value的Map结构
     *
     * @param schedulers
     * @return
     */
    public Map<String, List<StatisticsScheduleTaskM>> spliteScheduleToBusiTypeMap(
            List<StatisticsScheduleTaskM> schedulers);

    /**
     * 将任务列表拆分成以时间作为key， 以任务列表作为value的Map结构
     *
     * @param schedulers
     * @return
     */
    public Map<Long, List<StatisticsScheduleTaskM>> spliteScheduleToTimeMap(
            List<StatisticsScheduleTaskM> schedulers, Integer statDateType);

    /**
     * 删除所有完成任务
     */
    public void deleteSchedulers(List<StatisticsScheduleTaskM> list);

    /**
     * 取出宕机任务
     */
    public List<StatisticsScheduleTaskM> queryErrSchedulers(List<String> sIds, Long sTime, Long eTime);
}
