package com.interest.ids.commoninterface.dao.intelligentclean;

import java.util.List;
import java.util.Map;

import com.interest.ids.common.project.bean.intelligentclean.IntelligentCleanParamT;
import com.interest.ids.common.project.bean.kpi.KpiInverterDayM;

/**
 * 智能清洗Mapper
 * 
 * @author claude
 *
 */
public interface IntelligentCleanMapper {

	/**
	 * 查询电站智能清洗计算参数配置列表
	 * 
	 * @param stationCode
	 * 				stationCode
	 * @return List<IntelligentCleanParamT>
	 */
	List<IntelligentCleanParamT> getStationIntelligentCleanParams(String stationCode);
	
	/**
	 * 查询智能清洗计算参数配置中的值
	 * 
	 * @param paramKey
	 * 				key值
	 * @return value
	 */
	public String getParamValue(String paramKey);

	/**
	 * 查询系统默认智能清洗计算参数配置列表
	 * 
	 * @return List<IntelligentCleanParamT>
	 */
	List<IntelligentCleanParamT> getSystemIntelligentCleanParams();

	/**
	 * 根据电站编号查询该电站下所有的组串式逆变器
	 * 
	 * @param stationCode
	 * 				电站编号
	 * @return 组串式逆变器设备id列表
	 */
	List<Map<String, Long>> getStationInverters(String stationCode);

	/**
	 * 根据逆变器id查询该逆变器最后清洗日期
	 * 
	 * @param deviceId
	 * 				设备id
	 * @return 最后清洗日期
	 */
	Long getInverterCleanDay(Long deviceId);

	/**
	 * 查询逆变器开始运行时间
	 * 
	 * @param deviceId
	 * 				设备id
	 * @return 逆变器开始运行时间
	 */
	Long getInverterMinRundate(Long deviceId);

	/**
	 * 获取最后清洗时间开始的N天（cleaningCycle）数据，如没有最后清洗时间则取最初运行时间数据
	 * 筛选规则：
	 * 		1、逆变器日发电效率在leastPR与peakPR之间；
	 * 		2、当日辐照量小于irradiationThreshold（MJ/㎡）时，去除数据；
	 * 		3、存在故障和限电的逆变器日数据去除；
	 * 
	 * @param startTime
	 * @param endTime
	 * @param irradiationThreshold
	 * @param leastPR
	 * @param peakPR
	 * @return 满足条件的逆变器日数据
	 */
	List<KpiInverterDayM> getCleaningCycleData(Map<String, Object> params);

	/**
	 * 获取对应15天，30天，60天辐照量
	 * 
	 * @param nowOfLastYear
	 * 				去年今天
	 * @param fifteenDays
	 * 				去年今天后15天
	 * @param thirtyDays
	 * 				去年今天后30天
	 * @param sixtyDays
	 * 				去年今天后60天
	 * @param stationCode
	 * 				stationCode
	 * @return 
	 */
	List<Map<String, Object>> queryLastYearRadiation(Map<String, Object> params);

}
