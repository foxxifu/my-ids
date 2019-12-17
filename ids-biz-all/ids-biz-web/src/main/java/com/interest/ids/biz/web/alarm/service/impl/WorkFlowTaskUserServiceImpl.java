package com.interest.ids.biz.web.alarm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.interest.ids.common.project.bean.alarm.WorkFlowTaskUserM;
import com.interest.ids.commoninterface.dao.alarm.WorkFlowTaskUserMapper;
import com.interest.ids.commoninterface.service.alarm.IWorkFlowTaskUserService;

@Service("workFlowTaskUserService")
public class WorkFlowTaskUserServiceImpl implements IWorkFlowTaskUserService
{

    @Resource
    private WorkFlowTaskUserMapper workFlowTaskUserMapper;
    
    @Override
    public int insertWorkFlowTaskUserM(WorkFlowTaskUserM taskUser) {
        return workFlowTaskUserMapper.insertWorkFlowTaskUserM(taskUser);
    }

    @Override
    public int updateWorkFlowTaskUserM(WorkFlowTaskUserM taskUser) {
        return workFlowTaskUserMapper.updateWorkFlowTaskUserM(taskUser);
    }

    @Override
    public int deleteWorkFlowTaskUserM(String task_key) {
        return workFlowTaskUserMapper.deleteWorkFlowTaskUserM(task_key);
    }

    @Override
    public Long getTaskUserDetailByTaskKey(String task_key) {
        return workFlowTaskUserMapper.getTaskUserDetailByTaskKey(task_key);
    }

	@Override
	public WorkFlowTaskUserM selectWorkFlowTaskUserByPrvTaskId(String prevTaskId) {
		return workFlowTaskUserMapper.selectWorkFlowTaskUserByPrvTaskId(prevTaskId);
	}

}
