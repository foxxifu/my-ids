package com.interest.ids.biz.web.alarm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.interest.ids.common.project.bean.alarm.WorkFlowProcessM;
import com.interest.ids.commoninterface.dao.alarm.WorkFlowProcessMapper;
import com.interest.ids.commoninterface.service.alarm.IWorkFlowProcessService;

@Service("workFlowProcessService")
public class WorkFlowProcessServiceImpl implements IWorkFlowProcessService
{
    @Resource
    private WorkFlowProcessMapper workFlowProcessMapper;
    
    @Override
    public WorkFlowProcessM selectWorkFlowProcessMById(Long id) {
        return workFlowProcessMapper.selectWorkFlowProcessMById(id);
    }

    @Override
    public WorkFlowProcessM selectWorkFlowProcessMByProcId(String proc_id) {
        return workFlowProcessMapper.selectWorkFlowProcessMByProcId(proc_id);
    }

    @Override
    public int insertWorkFlowProcess(WorkFlowProcessM process) {
        return workFlowProcessMapper.insertWorkFlowProcess(process);
    }

    @Override
    public int updateWorkFlowProcess(WorkFlowProcessM process) {
        return workFlowProcessMapper.updateWorkFlowProcess(process);
    }

    @Override
    public String selectCurrentAssignee(String proc_id) {
        return workFlowProcessMapper.selectCurrentAssignee(proc_id);
    }

}
