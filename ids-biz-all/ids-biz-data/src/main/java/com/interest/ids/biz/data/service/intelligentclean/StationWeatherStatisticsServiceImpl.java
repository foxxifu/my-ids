package com.interest.ids.biz.data.service.intelligentclean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interest.ids.common.project.bean.intelligentclean.StationWeatherM;
import com.interest.ids.commoninterface.dao.intelligentclean.StationWeatherStatisticsMapper;
import com.interest.ids.commoninterface.service.intelligentclean.IStationWeatherStatisticsService;

@Service("stationWeatherStatisticsService")
public class StationWeatherStatisticsServiceImpl implements IStationWeatherStatisticsService {

	@Autowired
	public StationWeatherStatisticsMapper stationWeatherStatisticsMapper;

	@Override
	public void insertStationWeather(StationWeatherM stationWeather) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("stationCode", stationWeather.getStationCode());
		params.put("collectTime", stationWeather.getCollectTime());
		List<StationWeatherM> weather = this.stationWeatherStatisticsMapper.getStationWeather(params);
		if (weather == null || weather.size() == 0) {
			this.stationWeatherStatisticsMapper.insertStationWeather(stationWeather);
		}
	}

	@Override
	public void deleteOneYearData(Long nowOfLastYear) {
		this.stationWeatherStatisticsMapper.deleteOneYearData(nowOfLastYear);
	}

	@Override
	public List<Map<String, Object>> queryLastYearRadiation(String stationCode,String month1,String month2) {
		return this.stationWeatherStatisticsMapper.queryLastYearRadiation(stationCode, month1, month2);
	}

}
