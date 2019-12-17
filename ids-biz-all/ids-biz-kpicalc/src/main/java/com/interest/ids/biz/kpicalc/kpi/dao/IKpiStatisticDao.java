package com.interest.ids.biz.kpicalc.kpi.dao;

import java.util.List;

import com.interest.ids.common.project.bean.kpi.KpiDcCombinerDayM;

public interface IKpiStatisticDao {

   <T> void saveOrUpdate(List<T> list);

   List<KpiDcCombinerDayM> getKpiDcCombinerDayByStationCode(String stationCode, long startTime,
			long endTime);

   List<KpiDcCombinerDayM> getTopKpiDcCombinerDayByStationCode(String stationCode,
		long startTime, long endTime);

}
