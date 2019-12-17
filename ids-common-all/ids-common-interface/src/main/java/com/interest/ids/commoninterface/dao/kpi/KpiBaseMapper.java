package com.interest.ids.commoninterface.dao.kpi;

import com.interest.ids.common.project.bean.KpiBaseModel;

import java.util.List;

public interface KpiBaseMapper {

    void insertList(List<? extends KpiBaseModel> list);

    void deleteByPrimaryKeyList(List<? extends KpiBaseModel> list);

}
