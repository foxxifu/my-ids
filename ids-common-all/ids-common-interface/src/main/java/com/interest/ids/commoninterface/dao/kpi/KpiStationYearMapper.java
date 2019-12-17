package com.interest.ids.commoninterface.dao.kpi;

import com.interest.ids.common.project.bean.kpi.KpiStationYearM;

import java.util.List;
import java.util.Map;

public interface KpiStationYearMapper extends KpiBaseMapper{

    List<KpiStationYearM> selectStationYearKpi(Map<String, Object> params);
}