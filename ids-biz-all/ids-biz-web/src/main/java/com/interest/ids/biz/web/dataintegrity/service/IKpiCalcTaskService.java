package com.interest.ids.biz.web.dataintegrity.service;

import java.util.List;

import com.interest.ids.biz.web.dataintegrity.vo.KpiCalcTaskVo;
import com.interest.ids.common.project.bean.sm.KpiCalcTaskM;
import com.interest.ids.common.project.bean.sm.UserInfo;

public interface IKpiCalcTaskService {

    /**
     * 创建KPI 手动计算任务
     * 
     * @param kpiRevise
     */
    void addKpiReviseTask(KpiCalcTaskM kpiRevise);

    /**
     * 删除指定的手动计算任务
     * 
     * @param kpiRevise
     */
    void removeKpiReviseTask(KpiCalcTaskM kpiRevise);

    void removeKpiReviseTask(Long taskId);

    /**
     * 批量删除未执行的手动计算任务
     * 
     * @param kpiReviseList
     */
    void removeKpiReviseTaskList(List<KpiCalcTaskM> kpiReviseList);

    void modifyKpiReviseTask(KpiCalcTaskM kpiRevise);

    /**
     * 根据查询条件，查询对应的手动计算任务，组装为VO对象返回
     * 
     * @param user
     * @param stationName
     * @param taskStatus
     * @return
     */
    List<KpiCalcTaskVo> queryKpiRevises(UserInfo user, String stationName, String taskName, byte taskStatus);

    /**
     * 根据任务主键，查询对应的计算任务
     * 
     * @param taskId
     * @return
     */
    KpiCalcTaskM queryKpiReviseTask(Long taskId);

    /**
     * 手动启动计算任务，启动后任务开始执行
     * 
     * @param taskId
     * @return
     */
    void startExcuteKpiReviseTask(Long taskId, UserInfo user);
    
}
