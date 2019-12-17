package com.interest.ids.commoninterface.service.alarm;

import com.interest.ids.common.project.bean.alarm.WorkFlowTaskUserM;

public interface IWorkFlowTaskUserService {
    // 添加任务处理人
    int insertWorkFlowTaskUserM(WorkFlowTaskUserM taskUser);

    // 更新任务处理人
    int updateWorkFlowTaskUserM(WorkFlowTaskUserM taskUser);

    // 删除任务处理人
    int deleteWorkFlowTaskUserM(String task_key);

    // 根据任务key获取当前任务处理人的详细信息
    Long getTaskUserDetailByTaskKey(String task_key);

    //根据taskid查询任务关联用户数据
	WorkFlowTaskUserM selectWorkFlowTaskUserByPrvTaskId(String prevTaskId);
}
