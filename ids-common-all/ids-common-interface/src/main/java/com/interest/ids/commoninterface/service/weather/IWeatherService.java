package com.interest.ids.commoninterface.service.weather;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;

import com.alibaba.fastjson.JSONObject;

public interface IWeatherService {
	
	public static final String TIANQI_DAILY_WEATHER_URL = "https://api.seniverse.com/v3/weather/daily.json";
	public static final String TIANQI_NOW_WEATHER_URL = "https://api.seniverse.com/v3/weather/now.json";
	public static final String TIANQI_API_SECRET_KEY = "ohidp9wpbqhd8chk"; // 我的API密钥
	public static final String TIANQI_API_USER_ID = "UEC6BAE6D8"; // 我的用户ID
	public static final String TIANQI_API_SEARCH_CITY = "https://api.seniverse.com/v3/location/search.json";

	/**
	 * Generate HmacSHA1 signature with given data string and key
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws SignatureException
	 */
	public String generateSignature(String data, String key) throws SignatureException;

	/**
	 * 拼接访问天气url
	 * 
	 * @param location
	 *            参数值范围：
	 * 
	 *            城市ID 例如：location=WX4FBXXFKE4F 城市中文名 例如：location=北京 省市名称组合
	 *            例如：location=辽宁朝阳、location=北京朝阳 城市拼音/英文名
	 *            例如：location=beijing（如拼音相同城市，可在之前加省份和空格，例：shanxi yulin） 经纬度
	 *            例如：location=39.93:116.40（格式是 纬度:经度，英文冒号分隔） IP地址
	 *            例如：location=220.181.111.86（某些IP地址可能无法定位到城市） “ip”两个字母
	 *            自动识别请求IP地址，例如：location=ip
	 * @param language
	 *            参数值范围：
	 * 
	 *            zh-Hans 简体中文 zh-Hant 繁体中文 en 英文 ja 日语 de 德语 fr 法语 es 西班牙语 pt
	 *            葡萄牙语 hi 印地语（印度官方语言之一） id 印度尼西亚语 ru 俄语 th 泰语 ar 阿拉伯语
	 *            默认值：zh-Hans
	 * @param unit
	 *            参数值范围：
	 * 
	 *            c 当参数为c时，温度c、风速km/h、能见度km、气压mb f
	 *            当参数为f时，温度f、风速mph、能见度mile、气压inch 默认值：c
	 * @param start
	 *            参数值范围：
	 * 
	 *            日期 例如：start=2015/10/1 整数 例如：start=-2 代表前天、start=-1
	 *            代表昨天、start=0 代表今天、start=1 代表明天 默认值：0
	 * @param days
	 *            参数值范围：
	 * 
	 *            整数 例如：days=3 代表3天、days=7 代表7天
	 * @return url
	 * @throws SignatureException
	 * @throws UnsupportedEncodingException
	 */
	public String generateGetDiaryWeatherURL(String location, String language,
			String unit, String start, String days) throws SignatureException,UnsupportedEncodingException;

	/**
	 * 拼接访问天气url
	 * 
	 * @param location
	 *            参数值范围：
	 * 
	 *            城市ID 例如：location=WX4FBXXFKE4F 城市中文名 例如：location=北京 省市名称组合
	 *            例如：location=辽宁朝阳、location=北京朝阳 城市拼音/英文名
	 *            例如：location=beijing（如拼音相同城市，可在之前加省份和空格，例：shanxi yulin） 经纬度
	 *            例如：location=39.93:116.40（格式是 纬度:经度，英文冒号分隔） IP地址
	 *            例如：location=220.181.111.86（某些IP地址可能无法定位到城市） “ip”两个字母
	 *            自动识别请求IP地址，例如：location=ip
	 * @param language
	 *            参数值范围：
	 * 
	 *            zh-Hans 简体中文 zh-Hant 繁体中文 en 英文 ja 日语 de 德语 fr 法语 es 西班牙语 pt
	 *            葡萄牙语 hi 印地语（印度官方语言之一） id 印度尼西亚语 ru 俄语 th 泰语 ar 阿拉伯语
	 *            默认值：zh-Hans
	 * @param unit
	 *            参数值范围：
	 * 
	 *            c 当参数为c时，温度c、风速km/h、能见度km、气压mb f
	 *            当参数为f时，温度f、风速mph、能见度mile、气压inch 默认值：c
	 * @param start
	 *            参数值范围：
	 * 
	 *            日期 例如：start=2015/10/1 整数 例如：start=-2 代表前天、start=-1
	 *            代表昨天、start=0 代表今天、start=1 代表明天 默认值：0
	 * @param days
	 *            参数值范围：
	 * 
	 *            整数 例如：days=3 代表3天、days=7 代表7天
	 * @return url
	 * @throws SignatureException
	 * @throws UnsupportedEncodingException
	 */
	public String generateGetNowWeatherURL(String location, String language,
			String unit) throws SignatureException,UnsupportedEncodingException ;
	
	/**
	 * 拼接访问查询城市url
	 * 
	 * @param location
	 *            参数值范围：
	 *            例如：location=39.93:116.40（格式是 纬度:经度，英文冒号分隔） IP地址
	 * @return url
	 * @throws SignatureException
	 * @throws UnsupportedEncodingException
	 */
	public String generateSearchCityURL(String location) throws SignatureException,UnsupportedEncodingException ;

	/**
	 * 获取天气方法
	 * 
	 * @param urlPath
	 * 				请求路径
	 * @return 天气json
	 */
	public JSONObject getWeather(String urlPath);

	/**
	 * 根据经纬度获取所在的城市
	 * 
	 * @param location
	 * 				维度:经度
	 * @return 城市名称
	 */
	public String[] getCityByLocation(String location);
}
