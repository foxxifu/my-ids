package com.interest.ids.common.project.bean.intelligentclean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "ids_station_weather_statistics_t")
public class StationWeatherStatisticsM implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "station_code")
	private String stationCode;

	@Column(name = "year_time")
	private String yearTime;

	@Column(name = "month_time")
	private String monthTime;

	@Column(name = "radiation_intensity")
	private Double radiationIntensity;

	@Column(name = "heavy_rain_days")
	private Double heavyRainEays;

	@Column(name = "middle_rain_days")
	private int middleRainDays;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public String getYearTime() {
		return yearTime;
	}

	public void setYearTime(String yearTime) {
		this.yearTime = yearTime;
	}

	public String getMonthTime() {
		return monthTime;
	}

	public void setMonthTime(String monthTime) {
		this.monthTime = monthTime;
	}

	public Double getRadiationIntensity() {
		return radiationIntensity;
	}

	public void setRadiationIntensity(Double radiationIntensity) {
		this.radiationIntensity = radiationIntensity;
	}

	public Double getHeavyRainEays() {
		return heavyRainEays;
	}

	public void setHeavyRainEays(Double heavyRainEays) {
		this.heavyRainEays = heavyRainEays;
	}

	public int getMiddleRainDays() {
		return middleRainDays;
	}

	public void setMiddleRainDays(int middleRainDays) {
		this.middleRainDays = middleRainDays;
	}

}
