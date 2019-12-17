package com.interest.ids.commoninterface.dao.report;

import java.util.List;
import java.util.Map;

public interface CompontAnalyzsisMapper {

	/**根据电站的编号查询电站月度分析数据*/
	public List<Map<String,Object>> selectStationMouthAnalysis(Map<String,Object> condition);

	/**查询子阵月报分析数据*/
	public List<Map<String, Object>> selectCompontAnalysisMouth(Map<String, Object> condition);
	/**
	 * 获取设备也的每一个设备的每一种状态的数量
	 * @param condition 查询条件
	 * @return
	 */
	public List<Map<String, Object>> monthPvCountNumOfState(Map<String, Object> condition);

	/**电站年报分析统计*/
	public Integer selectStationYearAnalysisCount(Map<String, Object> condition);
	
	/**电站年报分析数据*/
	public List<Map<String, Object>> selectStationYearAnalysis(Map<String, Object> condition);

	/**查询推荐日期*/
	public List<Long> selectComponetCountTime(Long matrixId);

	/**查询子阵日统计分析*/
	public List<Map<String, Object>> getMatrixAnalysisDay(Map<String, Object> condition);

	/**查询组串诊断日分析*/
	public List<Map<String, Object>> getCompontAnalysisDay(Map<String, Object> condition);

	/**查询电站月度组件总数量*/
	public Integer selectStationMouthAnalysisCount(Map<String, Object> condition);

	/**查询电站子阵级别的月度分析总记录数*/
	public Integer selectCompontAnalysisMouthCount(Map<String, Object> condition);

	/**组件日分析数据报告条数统计*/
	public Integer selectCompontAnalysisDayCount(Map<String, Object> condition);

	/**查询电站级月度chart数据*/
	public Map<String, Object> selectStationMouthChartData(
			Map<String, Object> condition);

	public List<Map<String, Object>> selectCompontAnalysisMouthByStation(
			Map<String, Object> condition);
	/**
	 * 获取电站年的每个月份的统计echart图表信息
	 * @param condition
	 * @return
	 */
	List<Map<String, Object>> selectCompontAnalysisYearChartByStation(Map<String, Object> condition);
	/**组件日分析电站数据统计*/
	public Integer selectStationDayAnalysisCount(Map<String, Object> condition);

	/**电站日统计chart数据*/
	public Map<String, Object> selectStationDayAnalysis(
			Map<String, Object> condition);
	/**电站日统计table数据*/
	public List<Map<String, Object>> selectStationDayAnalysisByPage(
			Map<String, Object> condition);
	/**年报子阵数据条数统计*/
	public Integer selectCompontAnalysisYearCount(Map<String, Object> condition);

	/**年报电站数据*/
	public List<Map<String, Object>> selectCompontAnalysisYear(
			Map<String, Object> condition);

	/**查询最大的pv数*/
	public Integer selectMaxPv(Map<String, Object> condition);
	
	/**统计逆变器的个数*/
	public Integer selectInverterDiscreteRateCount(Map<String, Object> condition);

	/**获取逆变器chartData数据*/
	public List<Map<String,Object>> selectInverterDiscreteRateChartData(
			Map<String, Object> condition);

	/**获取逆变器tableData数据*/
	public List<Map<String, Object>> selectInverterDiscreteRateTableData(
			Map<String, Object> condition);
	/**统计直流汇率箱的个数*/
	public Integer selectCombinerdcDiscreteRateCount(
			Map<String, Object> condition);
	/**获取直流汇率箱chartData数据*/
	public List<Map<String, Object>> selectCombinerdcDiscreteRateChartData(
			Map<String, Object> condition);
	/**获取直流汇率箱tableData数据*/
	public List<Map<String, Object>> selectCombinerdcDiscreteRateTableData(
			Map<String, Object> condition);
	/**查询直流汇率箱和逆变器的最大pv数*/
	public Integer getInverterMaxPv(Map<String, Object> condition);
	/**导出电站月度分析报告数据*/
	public List<Map<String, Object>> exportStationMouthData(
			Map<String, Object> condition);
	/**导出子阵月度分析报告数据*/
	public List<Map<String, Object>> exportSubarrayMouthData(
			Map<String, Object> condition);
	/**导出电站年度分析报告数据*/
	public List<Map<String, Object>> exportStationYearData(
			Map<String, Object> condition);
	/**导出子阵年度分析报告数据*/
	public List<Map<String, Object>> exportSubarrayYearData(
			Map<String, Object> condition);
	/**导出电站日度分析报告数据*/
	public List<Map<String, Object>> exportStationDayData(
			Map<String, Object> condition);
	/**导出子阵日度分析报告数据*/
	public List<Map<String, Object>> exportSubarrayDayData(
			Map<String, Object> condition);
	/**导出逆变器分析报告数据*/
	public List<Map<String, Object>> exportCcatterData(
			Map<String, Object> condition);
	/**导出直流汇率箱分析报告数据*/
	public List<Map<String, Object>> exportScatterDCData(
			Map<String, Object> condition);

}
