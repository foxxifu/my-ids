package com.interest.ids.biz.web.weather.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.commoninterface.service.weather.IWeatherService;

/**
 * 查询天气信息
 * 
 * @author claude
 *
 */
@Controller
@RequestMapping("/weather")
public class WeatherController {

	private static Logger log = LoggerFactory.getLogger(WeatherController.class);
	
	@Autowired
	public IWeatherService weatherService;

	/**
	 * 根据城市获取天气信息,
	 * 
	 * @param location
	 *            维度：经度
	 * @param language
	 *            语言：默认为简体中文
	 * @param start
	 *            开始数：默认为0
	 * @param days
	 *            查询数据天数：默认为3
	 * @return Response<JSONObject>
	 */
	@ResponseBody
	@RequestMapping(value = "/getWeatherDaily", method = RequestMethod.POST)
	public Response<JSONObject> getWeatherDaily(@RequestBody JSONObject params) {

		Response<JSONObject> response = new Response<JSONObject>();

		try {
			String location = params.getString("location");
			String[] locations = this.weatherService.getCityByLocation(location);
			String language = params.getString("language");
			String start = params.getString("start");
			String days = params.getString("days");
			Assert.notNull(location, "citys is null!");
			JSONObject j = null;
			for(String loc : locations){
				String urlPath = this.weatherService.generateGetDiaryWeatherURL(loc, language, "c", start, days);
				log.info("urlPath=" + urlPath);
				j = this.weatherService.getWeather(urlPath);
				if(j != null){
					response.setResults(j);
					response.setCode(ResponseConstants.CODE_SUCCESS);
					response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
					break;
				}
			}
			if(j == null){
				log.error("get weather error: no weather data get...");
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			}
		} catch (Exception e) {
			log.error("getWeatherByCityName faild! error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		return response;
	}

	/**
	 * 根据城市获取天气信息,
	 * 
	 * @param location
	 *            维度：经度
	 * @param language
	 *            语言：默认为简体中文
	 * @param start
	 *            开始数：默认为0
	 * @param days
	 *            查询数据天数：默认为3
	 * @return Response<JSONObject>
	 */
	@ResponseBody
	@RequestMapping(value = "/getWeatherNow", method = RequestMethod.POST)
	public Response<JSONObject> getWeatherNow(@RequestBody JSONObject params) {

		Response<JSONObject> response = new Response<JSONObject>();

		try {
			String location = params.getString("location");
			String[] locations = this.weatherService.getCityByLocation(location);
			String language = params.getString("language");
			Assert.notNull(location, "citys is null!");
			JSONObject j = null;
			for(String loc : locations){
				String urlPath = this.weatherService.generateGetNowWeatherURL(loc, language, "c");
				j = this.weatherService.getWeather(urlPath);
				if(j != null){
					response.setResults(j);
					response.setCode(ResponseConstants.CODE_SUCCESS);
					response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
					break;
				}
			}
			if(j == null){
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			}
		} catch (Exception e) {
			log.error("getWeatherByCityName faild! error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		return response;
	}
	
}
