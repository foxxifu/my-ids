package com.interest.ids.biz.web.alarm.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.interest.ids.common.project.bean.alarm.QueryWorkFlowDefect;
import com.interest.ids.common.project.bean.alarm.WorkFlowDefectDto;
import com.interest.ids.common.project.bean.alarm.WorkFlowTaskM;
import com.interest.ids.commoninterface.dao.alarm.WorkFlowTaskMapper;
import com.interest.ids.commoninterface.service.alarm.IWorkFlowTaskService;

@Service("workFlowTaskService")
public class WorkFlowTaskServiceImpl implements IWorkFlowTaskService
{
    @Resource
    private WorkFlowTaskMapper workFlowTaskMapper;

    @Override
    public int insertWorkFlowTaskM(WorkFlowTaskM task) {
        return workFlowTaskMapper.insertWorkFlowTaskM(task);
    }

    @Override
    public WorkFlowTaskM selectWorkFlowTaskMById(Long id) {
        return workFlowTaskMapper.selectWorkFlowTaskMById(id);
    }

    @Override
    public WorkFlowTaskM selectWorkFlowTaskMByTaskId(String taskId) {
        return workFlowTaskMapper.selectWorkFlowTaskMByTaskId(taskId);
    }

    @Override
    public WorkFlowTaskM selectPrevWorkFlowTaskMById(String pre_task_id) {
        return workFlowTaskMapper.selectPrevWorkFlowTaskMById(pre_task_id);
    }

    @Override
    public List<WorkFlowTaskM> selectWorkFlowTaskMByStationCode(
            String stationCode) {
        return workFlowTaskMapper.selectWorkFlowTaskMByStationCode(stationCode);
    }

    @Override
    public int updateWorkFlowTaskM(WorkFlowTaskM task) {
        return workFlowTaskMapper.updateWorkFlowTaskM(task);
    }

    @Override
    public List<Map<String,Object>> selectWorkFlowTaskDealingId(Object[] procIds) {
        return workFlowTaskMapper.selectWorkFlowTaskDealingId(procIds);
    }

    @Override
    public List<WorkFlowDefectDto> selectTaskByCondition(
            QueryWorkFlowDefect condition) {
        return workFlowTaskMapper.selectTaskByCondition(condition);
    }
}
