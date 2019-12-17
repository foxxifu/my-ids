package com.interest.ids.common.project.bean.intelligentclean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "ids_station_weather_day_t")
public class StationWeatherM implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "station_code")
	private String stationCode;

	@Column(name = "collect_time")
	private Long collectTime;

	@Column(name = "weather_text_day")
	private String weatherTextDay;

	@Column(name = "weather_text_night")
	private String weatherTextNight;

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public Long getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(Long collectTime) {
		this.collectTime = collectTime;
	}

	public String getWeatherTextDay() {
		return weatherTextDay;
	}

	public void setWeatherTextDay(String weatherTextDay) {
		this.weatherTextDay = weatherTextDay;
	}

	public String getWeatherTextNight() {
		return weatherTextNight;
	}

	public void setWeatherTextNight(String weatherTextNight) {
		this.weatherTextNight = weatherTextNight;
	}

}
