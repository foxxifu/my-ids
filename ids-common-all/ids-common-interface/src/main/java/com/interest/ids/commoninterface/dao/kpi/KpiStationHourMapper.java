package com.interest.ids.commoninterface.dao.kpi;

import com.interest.ids.common.project.bean.kpi.KpiStationHourM;

import java.util.List;
import java.util.Map;

public interface KpiStationHourMapper extends KpiBaseMapper{

    List<KpiStationHourM> selectStationHourKpi(Map<String, Object> params);

}