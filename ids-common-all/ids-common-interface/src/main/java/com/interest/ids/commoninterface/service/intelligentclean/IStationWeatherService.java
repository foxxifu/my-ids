package com.interest.ids.commoninterface.service.intelligentclean;

import com.interest.ids.common.project.bean.intelligentclean.StationWeatherM;

/**
 * 维护电站历史天气表service
 * 
 * @author claude
 *
 */
public interface IStationWeatherService {

	/**
	 * 保存昨日电站天气信息
	 * 
	 * @param stationWeather
	 *            电站天气信息
	 */
	void insertStationWeather(StationWeatherM stationWeather);

	/**
	 * 删除一年前的数据
	 * 
	 * @param nowOfLastYear
	 */
	void deleteOneYearData(Long nowOfLastYear);

}
