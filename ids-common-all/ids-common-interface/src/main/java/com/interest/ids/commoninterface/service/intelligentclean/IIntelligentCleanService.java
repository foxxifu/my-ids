package com.interest.ids.commoninterface.service.intelligentclean;

import java.util.List;
import java.util.Map;

import com.interest.ids.common.project.bean.intelligentclean.IntelligentCleanParamT;

/**
 * 智能清洗service
 * 
 * @author claude
 *
 */
public interface IIntelligentCleanService {

	/**
	 * 查询智能清洗计算参数配置列表
	 * 
	 * @return List<IntelligentCleanParamT>
	 */
	List<IntelligentCleanParamT> getIntelligentCleanParams(String stationCode);

	/**
	 * 根据电站编号查询该电站下所有的组串式逆变器
	 * 
	 * @param stationCode
	 * 					电站编号
	 * @return 组串式逆变器id列表
	 */
	List<Map<String, Long>> getStationInverters(String stationCode);

}
