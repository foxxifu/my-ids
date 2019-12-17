package com.interest.ids.commoninterface.service.report;

import java.util.List;
import java.util.Map;

public interface CompontAnalysisService {

	/**查询电站月度组件总数量*/
	public Integer selectStationMouthAnalysisCount(Map<String, Object> condition);
	
	/**根据电站的编号查询电站月度分析数据*/
	public Map<String,Object> selectStationMouthAnalysis(Map<String,Object> condition);

	/**查询电站子阵级别的月度分析总记录数*/
	public Integer selectCompontAnalysisMouthCount(Map<String, Object> condition);
	
	/**查询子阵月报分析数据*/
	public Map<String, Object> selectCompontAnalysisMouth(Map<String, Object> condition);

	/**电站年报分析统计*/
	public Integer selectStationYearAnalysisCount(Map<String, Object> condition);
	
	/**电站年报分析数据*/
	public Map<String, Object> selectStationYearAnalysis(Map<String, Object> condition);

	/**电站子阵年报分析*/
	public Map<String, Object> selectCompontAnalysisYear(Map<String, Object> condition);

	/**查询推荐日期*/
	public List<Long> selectComponetCountTime(Long matrixId);

	/**组件日分析数据报告*/
	public Map<String, Object> getCompontAnalysisDay(Map<String,Object> condition);

	/**组件日分析数据报告条数统计*/
	public Integer selectCompontAnalysisDayCount(Map<String,Object> condition);

	/**组件日分析电站数据统计*/
	public Integer selectStationDayAnalysisCount(Map<String, Object> condition);

	/**组件日分析电站数据*/
	public Map<String, Object> selectStationDayAnalysis(
			Map<String, Object> condition);

	/**年报子阵数据条数统计*/
	public Integer selectCompontAnalysisYearCount(Map<String, Object> condition);

	/**查询最大的pv数*/
	public Integer selectMaxPv(Map<String, Object> condition);

	/**统计逆变器的个数*/
	public Integer selectInverterDiscreteRateCount(Map<String, Object> condition);

	/**获取逆变器离散率数据*/
	public Map<String, Object> selectInverterDiscreteRate(
			Map<String, Object> condition);

	/**统计直流汇率箱的个数*/
	public Integer selectCombinerdcDiscreteRateCount(
			Map<String, Object> condition);
	/**获取直流汇率箱离散率数据*/
	public Map<String, Object> selectCombinerdcDiscreteRate(
			Map<String, Object> condition);

	/**查询直流汇率箱和逆变器的最大pv数*/
	public Integer getInverterMaxPv(Map<String, Object> condition);

	/**导出电站月度分析报告数据*/
	public List<Object[]> exportStationMouthData(Map<String, Object> condition);

	/**导出子阵月度分析报告数据*/
	public List<Object[]> exportSubarrayMouthData(Map<String, Object> condition);
	/**导出电站年度分析报告数据*/
	public List<Object[]> exportStationYearData(Map<String, Object> condition);
	/**导出子阵年度分析报告数据*/
	public List<Object[]> exportSubarrayYearData(Map<String, Object> condition);
	/**导出电站日度分析报告数据*/
	public List<Object[]> exportStationDayData(Map<String, Object> condition);
	/**导出子阵日度分析报告数据*/
	public List<Object[]> exportSubarrayDayData(Map<String, Object> condition);
	/**导出逆变器分析报告数据*/
	public List<Object[]> exportCcatterData(Map<String, Object> condition);
	/**导出直流汇率箱分析报告数据*/
	public List<Object[]> exportScatterDCData(Map<String, Object> condition);
}
