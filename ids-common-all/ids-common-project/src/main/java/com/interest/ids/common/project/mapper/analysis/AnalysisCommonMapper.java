package com.interest.ids.common.project.mapper.analysis;

import java.util.List;
import java.util.Map;

import com.interest.ids.common.project.bean.kpi.InverterConc;
import com.interest.ids.common.project.bean.kpi.InverterString;


/**
 * 
 * @author lhq
 *
 *
 */
public interface AnalysisCommonMapper {
	
	Long getMaxCollectTime2Range(Map<String,Object> map);
	//获取指定范围内的最大值
	Double getMaxValue2Range(Map<String,Object> map);
	//获取制定范围内column的和
	Double getSumValue2Range(Map<String,Object> map);
	
	Double getTransferSumVoltage2Range(Map<String,Object> map);
	
	Integer queryVloAndCurrent(Map<String,Object> map);
	
	List<InverterConc> queryConInverter(Map<String,Object> map);
	
	List<InverterString> queryInverter(Map<String,Object> map);

}
