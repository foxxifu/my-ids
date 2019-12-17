package com.interest.ids.biz.data.service.operation;

import com.interest.ids.common.project.bean.operation.OperatorPositionM;

public interface IOperatorPositionService {

    /**
     * 保存运维人员实时位置
     * @param position
     */
    void saveOperatorPosition(OperatorPositionM position); 
    
    /**
     * 更新运维人员实时位置信息
     * @param position
     */
    void updateOperatorPosition(OperatorPositionM position);
    
    /**
     * 查询运维人员位置信息
     * @param userId
     * @return
     */
    OperatorPositionM queryOperatorPosition(Long userId);
    
}
