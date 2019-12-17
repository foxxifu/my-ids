package com.interest.ids.commoninterface.dao.kpi;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.interest.ids.common.project.bean.kpi.KpiRealTimeM;

public interface KpiRealTimeMapper {

    void batchInsertRealTimeKpiData(@Param("realTimeKpis")List<KpiRealTimeM> list);

    /**
     * 删除小于当前时间的实时数据
     * 
     * @param realTime
     * 				实时数据
     * 	
     */
	void deleteSmallerRealTimeData(KpiRealTimeM realtime);

	/**
	 * 删除数据库中最大时间3天前的数据，只设计实时数据历史表
	 */
	void deleteDatabefore3Days();
}
