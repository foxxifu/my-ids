package com.interest.ids.commoninterface.dao.kpi;

import com.interest.ids.common.project.bean.kpi.KpiStationMonthM;

import java.util.List;
import java.util.Map;

public interface KpiStationMonthMapper extends KpiBaseMapper{

    List<KpiStationMonthM> selectStationMonthKpi(Map<String, Object> params);
}