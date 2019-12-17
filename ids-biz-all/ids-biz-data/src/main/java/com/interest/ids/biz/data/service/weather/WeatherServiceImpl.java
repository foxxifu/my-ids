package com.interest.ids.biz.data.service.weather;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.SignatureException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.interest.ids.commoninterface.service.weather.IWeatherService;

@Service("weatherService")
public class WeatherServiceImpl implements IWeatherService {
	
	private static Logger log = LoggerFactory.getLogger(WeatherServiceImpl.class);

	@Override
	public String generateSignature(String data, String key) throws SignatureException {
		String result;
		try {
			SecretKeySpec signingKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA1");
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(signingKey);
			byte[] rawHmac = mac.doFinal(data.getBytes("UTF-8"));
			result = new sun.misc.BASE64Encoder().encode(rawHmac);
		} catch (Exception e) {
			throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
		}
		return result;
	}

	@Override
	public String generateGetDiaryWeatherURL(String location, String language,
			String unit, String start, String days) throws SignatureException, UnsupportedEncodingException {
		String timestamp = String.valueOf(new Date().getTime());
		String params = "ts=" + timestamp + "&ttl=1800&uid=" + TIANQI_API_USER_ID;
		String signature = URLEncoder.encode( generateSignature(params, TIANQI_API_SECRET_KEY), "UTF-8");
		String url = TIANQI_DAILY_WEATHER_URL + "?" + params + "&sig=" + signature + "&location="
				+ URLEncoder.encode(location, "UTF-8");
		if (!StringUtils.isEmpty(language)) {
			url += "&language=" + language;
		} else {
			url += "&language=zh-Hans";
		}
		if (!StringUtils.isEmpty(unit)) {
			url += "&unit=" + unit;
		}
		if (StringUtils.isEmpty(start)) {
			start = "0";
		}
		if (StringUtils.isEmpty(days)) {
			days = "3";
		}
		url += "&start=" + start + "&days=" + days;
		return url;
	}

	@Override
	public String generateGetNowWeatherURL(String location, String language,
			String unit) throws SignatureException, UnsupportedEncodingException {
		String timestamp = String.valueOf(new Date().getTime());
		String params = "ts=" + timestamp + "&ttl=30&uid=" + TIANQI_API_USER_ID;
		String signature = URLEncoder.encode(generateSignature(params, TIANQI_API_SECRET_KEY), "UTF-8");
		String url = TIANQI_NOW_WEATHER_URL + "?" + params + "&sig=" + signature + "&location="
				+ URLEncoder.encode(location, "UTF-8");
		if (!StringUtils.isEmpty(language)) {
			url += "&language=" + language;
		} else {
			url += "&language=zh-Hans";
		}
		if (!StringUtils.isEmpty(unit)) {
			url += "&unit=" + unit;
		}
		return url;
	}

	@Override
	public String generateSearchCityURL(String location) 
			throws SignatureException, UnsupportedEncodingException {
		String url = TIANQI_API_SEARCH_CITY + "?" + "key=" + TIANQI_API_SECRET_KEY + "&q="
				+ URLEncoder.encode(location, "UTF-8");
		return url;
	}

	@Override
	public JSONObject getWeather(String urlPath) {
		try {
				
			StringBuffer strBuf = new StringBuffer();
	
			URL url = new URL(urlPath);
			URLConnection conn = url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));// 转码。
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line + " ");
			}
			reader.close();
	
			String resultString = strBuf.toString();
			JSONObject j = null;
			if (!resultString.contains("status_code")) {
				j = JSONObject.parseObject(resultString);
			} else {
				log.error("get weather error:msg = {}", resultString);
			}
			return j;
		} catch (Exception e) {
			log.error("get weather error eception:", e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String[] getCityByLocation(String location) {
		try {
			StringBuffer strBuf = new StringBuffer();
			String urlPath = this.generateSearchCityURL(location);
			URL url = new URL(urlPath);
			URLConnection conn = url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));// 转码。
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line + " ");
			}
			reader.close();
			String resultString = strBuf.toString();
			if (!resultString.contains("status_code")) {
				JSONObject j = JSONObject.parseObject(resultString);
				List<Map<String, String>> cityInfo = (List<Map<String, String>>) j.get("results");
				if(cityInfo != null && cityInfo.size() > 0){
					return cityInfo.get(0).get("path").split(",");
				}
			}
		} catch (Exception e) {
			log.error("getCityByLocation failed! error msg : " + e.getMessage());
		}
		return new String[]{location};
	}
}
