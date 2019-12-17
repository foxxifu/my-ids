package com.interest.ids.biz.data.service.intelligentclean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interest.ids.common.project.bean.intelligentclean.StationWeatherM;
import com.interest.ids.commoninterface.dao.intelligentclean.StationWeatherMapper;
import com.interest.ids.commoninterface.service.intelligentclean.IStationWeatherService;

@Service("stationWeatherService")
public class StationWeatherServiceImpl implements IStationWeatherService {

	@Autowired
	public StationWeatherMapper stationWeatherMapper;

	@Override
	public void insertStationWeather(StationWeatherM stationWeather) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("stationCode", stationWeather.getStationCode());
		params.put("collectTime", stationWeather.getCollectTime());
		List<StationWeatherM> weather = this.stationWeatherMapper.getStationWeather(params);
		if (weather == null || weather.size() == 0) {
			this.stationWeatherMapper.insertStationWeather(stationWeather);
		}
	}

	@Override
	public void deleteOneYearData(Long nowOfLastYear) {
		this.stationWeatherMapper.deleteOneYearData(nowOfLastYear);
	}

}
