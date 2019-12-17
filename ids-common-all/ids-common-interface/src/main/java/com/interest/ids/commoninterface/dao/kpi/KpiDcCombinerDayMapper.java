package com.interest.ids.commoninterface.dao.kpi;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.interest.ids.common.project.bean.kpi.KpiDcCombinerDayM;

public interface KpiDcCombinerDayMapper extends KpiBaseMapper{

	List<KpiDcCombinerDayM> getKpiDcCombinerDayByStationCode(@Param("stationCode")String stationCode,@Param("startTime") long startTime,
			@Param("endTime") long endTime);

	List<KpiDcCombinerDayM> getTopKpiDcCombinerDayByStationCode(
			@Param("stationCode")String stationCode,@Param("startTime") long startTime,
			@Param("endTime") long endTime);

}