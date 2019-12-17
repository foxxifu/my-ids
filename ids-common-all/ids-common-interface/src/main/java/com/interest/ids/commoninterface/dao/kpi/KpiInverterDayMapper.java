package com.interest.ids.commoninterface.dao.kpi;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.interest.ids.common.project.bean.kpi.KpiInverterDayM;

public interface KpiInverterDayMapper extends KpiBaseMapper {

    public List<KpiInverterDayM> selectPprTopNumInverter(@Param(value = "startTime") long startTime,
            @Param(value = "endTime") long endTime);

    public List<KpiInverterDayM> selectPprTopNumInvDayKpi(@Param(value = "stationCode") String stationCode,
            @Param(value = "devIds") List<Long> devIds, @Param(value = "startTime") long startTime,
            @Param(value = "endTime") long endTime);
}