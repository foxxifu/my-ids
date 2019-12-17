package com.interest.ids.commoninterface.dao.alarm;

import java.util.List;
import java.util.Map;

import com.interest.ids.common.project.bean.alarm.QueryWorkFlowDefect;
import com.interest.ids.common.project.bean.alarm.WorkFlowDefectDto;
import com.interest.ids.common.project.bean.alarm.WorkFlowTaskM;

public interface WorkFlowTaskMapper
{
    //插入一个任务
    int insertWorkFlowTaskM(WorkFlowTaskM task);
    
    //根据id查询当前任务
    WorkFlowTaskM selectWorkFlowTaskMById(Long id);
    
    //根据任务id查询当前任务
    WorkFlowTaskM selectWorkFlowTaskMByTaskId(String taskId);
    
    //根据上一部任务id查询上一部任务
    WorkFlowTaskM selectPrevWorkFlowTaskMById(String pre_task_id);
    
    //查询历史人物
    List<WorkFlowTaskM> selectWorkFlowTaskMByStationCode(String stationCode);
    
    //更新任务
    int updateWorkFlowTaskM(WorkFlowTaskM task);
    
    //查询代办任务 - 暂未提供
    List<WorkFlowTaskM> selectWaitingWorkFlowTaskM();

    /**
     * @author xm
     * 根据进度id查询所有的任务按照task_start_time升序
     * @param procId 进度id
     * @return
     */
    List<WorkFlowTaskM> selectPrevTaskMByProcId(String procId);

    /**
     * @author xm
     * 根据当前的任务id查询当前的任务的处理人的id
     * @param procIds
     * @return
     */
    List<Map<String,Object>> selectWorkFlowTaskDealingId(Object[] procIds);

    /**
     * 根据条件查询任务
     * @param condition
     * @return
     */
    List<WorkFlowDefectDto> selectTaskByCondition(QueryWorkFlowDefect condition);
}
