package com.interest.ids.common.project.bean.kpi;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "ids_emi_data_t")
public class Environment {
    @Id
    @Column(name = "collect_time")
    private Long collectTime;

    @Id
    @Column(name = "dev_id")
    private Long deviceId;

    @Id
    @Column(name = "station_code")
    private String stationCode;

    /**
     * 判断当前指标是否有效：0代表有效，1代表无效  格式010000000000 : 代表第2位也就是bc_u无效
     */
    private String valid;

    @Column(name = "dev_name")
    private String deviceName;

    /**
     * 温度
     */
    private BigDecimal temperature;

    /**
     * pv温度
     */
    @Column(name = "pv_temperature")
    private BigDecimal pvTemperature;

    /**
     * 风速
     */
    @Column(name = "wind_speed")
    private BigDecimal windSpeed;

    /**
     * 风向
     */
    @Column(name = "wind_direction")
    private BigDecimal windDirection;

    /**
     * 总辐射量
     */
    @Column(name = "total_irradiation")
    private BigDecimal totalIrradiation;

    /**
     * 辐照强度
     */
    @Column(name = "irradiation_intensity")
    private BigDecimal irradiationIntensity;

    /**
     * 水平辐照强度
     */
    @Column(name = "horiz_irradiation_intensity")
    private BigDecimal horizIrradiationIntensity;

    /**
     * 水平辐照量
     */
    @Column(name = "horiz_total_irradiation")
    private BigDecimal horizTotalIrradiation;

    /**
     * @return collect_time
     */
    public Long getCollectTime() {
        return collectTime;
    }

    /**
     * @param collectTime
     */
    public void setCollectTime(Long collectTime) {
        this.collectTime = collectTime;
    }

    /**
     * @return device_id
     */
    public Long getDeviceId() {
        return deviceId;
    }

    /**
     * @param deviceId
     */
    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * @return station_code
     */
    public String getStationCode() {
        return stationCode;
    }

    /**
     * @param stationCode
     */
    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    /**
     * 获取温度
     *
     * @return temperature - 温度
     */
    public BigDecimal getTemperature() {
        return temperature;
    }

    /**
     * 设置温度
     *
     * @param temperature 温度
     */
    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    /**
     * 获取pv温度
     *
     * @return pv_temperature - pv温度
     */
    public BigDecimal getPvTemperature() {
        return pvTemperature;
    }

    /**
     * 设置pv温度
     *
     * @param pvTemperature pv温度
     */
    public void setPvTemperature(BigDecimal pvTemperature) {
        this.pvTemperature = pvTemperature;
    }

    /**
     * 获取风速
     *
     * @return wind_speed - 风速
     */
    public BigDecimal getWindSpeed() {
        return windSpeed;
    }

    /**
     * 设置风速
     *
     * @param windSpeed 风速
     */
    public void setWindSpeed(BigDecimal windSpeed) {
        this.windSpeed = windSpeed;
    }

    /**
     * 获取风向
     *
     * @return wind_direction - 风向
     */
    public BigDecimal getWindDirection() {
        return windDirection;
    }

    /**
     * 设置风向
     *
     * @param windDirection 风向
     */
    public void setWindDirection(BigDecimal windDirection) {
        this.windDirection = windDirection;
    }

    /**
     * 获取总辐射量
     *
     * @return radiant_total - 总辐射量
     */
    public BigDecimal getTotalIrradiation() {
        return totalIrradiation;
    }

    /**
     * 设置总辐射量
     *
     * @param totalIrradiation 总辐射量
     */
    public void setTotalIrradiation(BigDecimal totalIrradiation) {
        this.totalIrradiation = totalIrradiation;
    }

    /**
     * 获取辐照强度
     *
     * @return radiant_line - 辐照强度
     */
    public BigDecimal getIrradiationIntensity() {
        return irradiationIntensity;
    }

    /**
     * 设置辐照强度
     *
     * @param irradiationIntensity 辐照强度
     */
    public void setIrradiationIntensity(BigDecimal irradiationIntensity) {
        this.irradiationIntensity = irradiationIntensity;
    }

    /**
     * 获取水平辐照强度
     *
     * @return horiz_radiant_line - 水平辐照强度
     */
    public BigDecimal getHorizIrradiationIntensity() {
        return horizIrradiationIntensity;
    }

    /**
     * 设置水平辐照强度
     *
     * @param horizIrradiationIntensity 水平辐照强度
     */
    public void setHorizIrradiationIntensity(BigDecimal horizIrradiationIntensity) {
        this.horizIrradiationIntensity = horizIrradiationIntensity;
    }

    /**
     * 获取水平辐照量
     *
     * @return horiz_radiant_total - 水平辐照量
     */
    public BigDecimal getHorizTotalIrradiation() {
        return horizTotalIrradiation;
    }

    /**
     * 设置水平辐照量
     *
     * @param horizTotalIrradiation 水平辐照量
     */
    public void setHorizTotalIrradiation(BigDecimal horizTotalIrradiation) {
        this.horizTotalIrradiation = horizTotalIrradiation;
    }
}