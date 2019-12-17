package com.interest.ids.commoninterface.dao.intelligentclean;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.interest.ids.common.project.bean.intelligentclean.StationWeatherM;

/**
 * 电站天气Mapper
 * 
 * @author claude
 *
 */
public interface StationWeatherStatisticsMapper {

	/**
	 * 根据电站编号和时间查询天气信息
	 * 
	 * @param params
	 * @return
	 */
	List<StationWeatherM> getStationWeather(Map<String, Object> params);

	/**
	 * 保存天气信息
	 * 
	 * @param stationWeather
	 */
	void insertStationWeather(StationWeatherM stationWeather);

	/**
	 * 删除一年前的数据
	 * @param nowOfLastYear
	 */
	void deleteOneYearData(Long nowOfLastYear);

	List<Map<String, Object>> queryLastYearRadiation(@Param("stationCode")String stationCode,
			@Param("thirtyDays")String month1, @Param("sixtyDays")String month2);

}
