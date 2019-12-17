package com.interest.ids.biz.kpicalc.kpi.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.interest.ids.common.project.bean.intelligentclean.IntelligentCleanParamT;
import com.interest.ids.common.project.bean.intelligentclean.StationWeatherM;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.spring.context.SystemContext;
import com.interest.ids.common.project.utils.DateUtil;
import com.interest.ids.common.project.utils.ThreadPoolUtil;
import com.interest.ids.commoninterface.service.intelligentclean.IIntelligentCleanService;
import com.interest.ids.commoninterface.service.intelligentclean.IStationWeatherService;
import com.interest.ids.commoninterface.service.weather.IWeatherService;
import com.interest.ids.redis.caches.StationCache;

@Component
public class IntelligentCleanJob implements Job {

	private static IIntelligentCleanService intelligentCleanService;
	private static IWeatherService weatherService;
	private static IStationWeatherService stationWeatherService;
	
	static {
		if(intelligentCleanService == null){
			intelligentCleanService = (IIntelligentCleanService) SystemContext.getBean("intelligentCleanService");
		}
		if(weatherService == null){
			weatherService = (IWeatherService) SystemContext.getBean("weatherService");
		}
		if(weatherService == null){
			stationWeatherService = (IStationWeatherService) SystemContext.getBean("stationWeatherService");
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public void execute(JobExecutionContext jobContext) throws JobExecutionException {
		// 1、查询所有分布式和地面式电站，只有分布式和地面式电站才需要进行智能清洗
		List<StationInfoM> cleanStations = new ArrayList<StationInfoM>();
		List<StationInfoM> allStations = StationCache.getAllstations();
		for(StationInfoM station : allStations){
			// CombinedType(1:地面式 2:分布式);StationBuildStatus(1:并网)
			if((station.getOnlineType() == 1 || station.getOnlineType() == 2) && station.getStationBuildStatus() == 1){
				cleanStations.add(station);
			}
		}
		
		// 2、循环电站进行组串式逆变器设备分析
		List<IntelligentCleanParamT> stationParam = null;// 电站智能清洗参数
		List<Map<String, Long>> stationInverters = null;
		InverterIntelligentCleanRunnable cleanCallable = null;
		for(StationInfoM station : cleanStations){
			stationParam = this.intelligentCleanService.getIntelligentCleanParams(station.getStationCode());
			stationInverters = this.intelligentCleanService.getStationInverters(station.getStationCode());
			for(Map<String, Long> map : stationInverters){
				cleanCallable = new InverterIntelligentCleanRunnable(map.get("id"), stationParam);
				// 分单个逆变器执行
				ThreadPoolUtil.getInstance().addRunnableTask(cleanCallable);
			}
			StationWeatherM stationWeather = this.getTodayWeather(station);
			if(stationWeather != null){
				this.stationWeatherService.insertStationWeather(stationWeather);
			}
		}
		// 删除一年前的天气数据
		this.stationWeatherService.deleteOneYearData(DateUtil.getNowOfLastYear(TimeZone.getDefault()));
	}
	
	/**
	 * 查询电站的昨日天气
	 * 
	 * @param station
	 * 				电站信息
	 * @return 天气bean
	 */
	@SuppressWarnings({ "static-access", "unchecked" })
	public StationWeatherM getTodayWeather(StationInfoM station) {
		StationWeatherM stationWeather = new StationWeatherM();
		stationWeather.setStationCode(station.getStationCode());
		stationWeather.setCollectTime(DateUtil.getBeginOfDayTimeByMill(System.currentTimeMillis()));
		try {
			String latitudeAndLongitude = station.getLatitude() + ":" + station.getLongitude();
			String[] locations = this.weatherService.getCityByLocation(latitudeAndLongitude);
			JSONObject j = null;
			for(String loc : locations){
				String urlPath = this.weatherService.generateGetDiaryWeatherURL(loc, "zh-Hans", "c", "0", "1");
				j = this.weatherService.getWeather(urlPath);
				if(j != null){
					List<Map<String, String>> yesterdayWeather = (List<Map<String, String>>) j.get("results");
					if(yesterdayWeather != null && yesterdayWeather.size() > 1){
						stationWeather.setWeatherTextDay(yesterdayWeather.get(1).get("text_day"));
						stationWeather.setWeatherTextDay(yesterdayWeather.get(1).get("text_night"));
					}
				}
			}
		} catch (Exception e) {
			return null;
		}
		return stationWeather;
	}

}
