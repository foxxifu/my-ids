package com.interest.ids.biz.data.service.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interest.ids.common.project.bean.operation.OperatorPositionM;
import com.interest.ids.common.project.mapper.operation.OperatorPositionMapper;

@Service
public class OperatorPositionServiceImpl implements IOperatorPositionService {

    @Autowired
    private OperatorPositionMapper operatorPositionMapper;
    
    @Override
    public void saveOperatorPosition(OperatorPositionM position) {
       
        operatorPositionMapper.insert(position);
    }

    @Override
    public void updateOperatorPosition(OperatorPositionM position) {
        operatorPositionMapper.updateByPrimaryKey(position);
    }

    @Override
    public OperatorPositionM queryOperatorPosition(Long userId) {
        return operatorPositionMapper.selectByPrimaryKey(userId);
    }
}
