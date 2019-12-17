package com.interest.ids.commoninterface.dao.alarm;

import com.interest.ids.common.project.bean.alarm.WorkFlowProcessM;

public interface WorkFlowProcessMapper 
{
    //根据id查询进度
    WorkFlowProcessM selectWorkFlowProcessMById(Long id);
    
    //根据进度id查询进度
    WorkFlowProcessM selectWorkFlowProcessMByProcId(String proc_id);
    
    //添加一个进度
    int insertWorkFlowProcess(WorkFlowProcessM process);
    
    //修改一个进度
    int updateWorkFlowProcess(WorkFlowProcessM process);
    
    //查询当前处理人
    String selectCurrentAssignee(String proc_id);
}
