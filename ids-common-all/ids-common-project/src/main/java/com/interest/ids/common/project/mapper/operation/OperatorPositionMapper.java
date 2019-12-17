package com.interest.ids.common.project.mapper.operation;

import java.util.List;

import com.interest.ids.common.project.bean.operation.OperatorPositionM;
import com.interest.ids.common.project.utils.CommonMapper;

public interface OperatorPositionMapper extends CommonMapper<OperatorPositionM> {

    /**
     * 查询企业下所有的运维人员
     * @param enterpriseId
     * @return
     */
    List<OperatorPositionM> selectEnterpriseOperators(Long enterpriseId);
}
