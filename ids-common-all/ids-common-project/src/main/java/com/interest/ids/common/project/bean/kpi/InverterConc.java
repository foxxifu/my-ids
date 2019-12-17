package com.interest.ids.common.project.bean.kpi;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "ids_inverter_conc_data_t")
public class InverterConc {
    @Id
    @Column(name = "collect_time")
    private Long collectTime;

    /**
     * 设备唯一标识
     */
    @Id
    @Column(name = "dev_id")
    private Long deviceId;

    @Id
    @Column(name = "station_code")
    private String stationCode;

    @Column(name = "dev_name")
    private String deviceName;

    /**
     * 0x0000:待机：初始化;idle:initializing 0x0001:待机：绝缘阻抗检测;idle:iso detecting 0x0002:待机：光照检测;
     * idle:irradiation detecting 0x0100:启动;starting 0x0200:并网;on-grid 0x0201:并网:限功率;
     * on-grid:limited 0x0300:关机：异常关机; shutdown:abnormal 0x0301:关机：指令关机shutdown:forced
     * 0x0401：电网调度：cosψ-p 曲线;grid dispatch:cosψ-p curve 0x0402：电网调度：q-u 曲线;
     * grid dispatch:q-u curve 0xa000:待机：无光照；idle:no irradiation
     */
    @Column(name = "inverter_state")
    private Integer inverterState;

    /**
     * 判断当前指标是否有效：0代表有效，1代表无效  格式010000000000 : 代表第2位也就是bc_u无效
     */
    private String valid;

    /**
     * 当日发电量
     */
    @Column(name = "day_capacity")
    private BigDecimal dayCapacity;

    /**
     * 累计发电量
     */
    @Column(name = "total_capacity")
    private BigDecimal totalCapacity;

    /**
     * 机内温度
     */
    private BigDecimal temperature;

    /**
     * 直流电压
     */
    @Column(name = "center_u")
    private BigDecimal centerU;

    /**
     * 直流电流
     */
    @Column(name = "center_i")
    private BigDecimal centerI;

    /**
     * 第1路电流值
     */
    @Column(name = "center_i_1")
    private BigDecimal centerI1;

    @Column(name = "center_i_2")
    private BigDecimal centerI2;

    @Column(name = "center_i_3")
    private BigDecimal centerI3;

    @Column(name = "center_i_4")
    private BigDecimal centerI4;

    @Column(name = "center_i_5")
    private BigDecimal centerI5;

    @Column(name = "center_i_6")
    private BigDecimal centerI6;

    @Column(name = "center_i_7")
    private BigDecimal centerI7;

    @Column(name = "center_i_8")
    private BigDecimal centerI8;

    @Column(name = "center_i_9")
    private BigDecimal centerI9;

    @Column(name = "center_i_10")
    private BigDecimal centerI10;

    /**
     * 直流输入功率
     */
    @Column(name = "mppt_power")
    private BigDecimal mpptPower;

    /**
     * a箱电压
     */
    @Column(name = "a_u")
    private BigDecimal aU;

    @Column(name = "b_u")
    private BigDecimal bU;

    @Column(name = "c_u")
    private BigDecimal cU;

    /**
     * 电网a箱电流
     */
    @Column(name = "a_i")
    private BigDecimal aI;

    @Column(name = "b_i")
    private BigDecimal bI;

    @Column(name = "c_i")
    private BigDecimal cI;

    /**
     * 功率因数
     */
    @Column(name = "power_factor")
    private BigDecimal powerFactor;

    /**
     * 并网频率
     */
    @Column(name = "grid_frequency")
    private BigDecimal gridFrequency;

    /**
     * 交流输出功率
     */
    @Column(name = "active_power")
    private BigDecimal activePower;

    /**
     * 输出无功功率
     */
    @Column(name = "reactive_power")
    private BigDecimal reactivePower;

    /**
     * 逆变器开机时间
     */
    @Column(name = "on_time")
    private Long onTime;

    /**
     * 逆变器关机时间
     */
    @Column(name = "off_time")
    private Long offTime;

    /**
     * 生产可靠性
     */
    private String aop;

    /**
     * 预留字段1
     */
    private BigDecimal ext1;

    /**
     * 预留字段2
     */
    private BigDecimal ext2;

    /**
     * 预留字段3
     */
    @Column(name = "ext_str_1")
    private String extStr1;

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
     * 获取设备唯一标识
     *
     * @return device_id - 设备唯一标识
     */
    public Long getDeviceId() {
        return deviceId;
    }

    /**
     * 设置设备唯一标识
     *
     * @param deviceId 设备唯一标识
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

    /**
     * @return busi_code
     */
    public String getDeviceName() {
        return deviceName;
    }

    /**
     * @param deviceName
     */
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Integer getInverterState() {
        return inverterState;
    }

    public void setInverterState(Integer inverterState) {
        this.inverterState = inverterState;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    /**
     * 获取当日发电量
     *
     * @return day_cap - 当日发电量
     */
    public BigDecimal getDayCapacity() {
        return dayCapacity;
    }

    /**
     * 设置当日发电量
     *
     * @param dayCapacity 当日发电量
     */
    public void setDayCapacity(BigDecimal dayCapacity) {
        this.dayCapacity = dayCapacity;
    }

    /**
     * 获取累计发电量
     *
     * @return total_cap - 累计发电量
     */
    public BigDecimal getTotalCapacity() {
        return totalCapacity;
    }

    /**
     * 设置累计发电量
     *
     * @param totalCapacity 累计发电量
     */
    public void setTotalCapacity(BigDecimal totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    /**
     * 获取机内温度
     *
     * @return temperature - 机内温度
     */
    public BigDecimal getTemperature() {
        return temperature;
    }

    /**
     * 设置机内温度
     *
     * @param temperature 机内温度
     */
    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    /**
     * 获取直流电压
     *
     * @return center_u - 直流电压
     */
    public BigDecimal getCenterU() {
        return centerU;
    }

    /**
     * 设置直流电压
     *
     * @param centerU 直流电压
     */
    public void setCenterU(BigDecimal centerU) {
        this.centerU = centerU;
    }

    /**
     * 获取直流电流
     *
     * @return center_i - 直流电流
     */
    public BigDecimal getCenterI() {
        return centerI;
    }

    /**
     * 设置直流电流
     *
     * @param centerI 直流电流
     */
    public void setCenterI(BigDecimal centerI) {
        this.centerI = centerI;
    }

    /**
     * 获取第1路电流值
     *
     * @return center_i_1 - 第1路电流值
     */
    public BigDecimal getCenterI1() {
        return centerI1;
    }

    /**
     * 设置第1路电流值
     *
     * @param centerI1 第1路电流值
     */
    public void setCenterI1(BigDecimal centerI1) {
        this.centerI1 = centerI1;
    }

    /**
     * @return center_i_2
     */
    public BigDecimal getCenterI2() {
        return centerI2;
    }

    /**
     * @param centerI2
     */
    public void setCenterI2(BigDecimal centerI2) {
        this.centerI2 = centerI2;
    }

    /**
     * @return center_i_3
     */
    public BigDecimal getCenterI3() {
        return centerI3;
    }

    /**
     * @param centerI3
     */
    public void setCenterI3(BigDecimal centerI3) {
        this.centerI3 = centerI3;
    }

    /**
     * @return center_i_4
     */
    public BigDecimal getCenterI4() {
        return centerI4;
    }

    /**
     * @param centerI4
     */
    public void setCenterI4(BigDecimal centerI4) {
        this.centerI4 = centerI4;
    }

    /**
     * @return center_i_5
     */
    public BigDecimal getCenterI5() {
        return centerI5;
    }

    /**
     * @param centerI5
     */
    public void setCenterI5(BigDecimal centerI5) {
        this.centerI5 = centerI5;
    }

    /**
     * @return center_i_6
     */
    public BigDecimal getCenterI6() {
        return centerI6;
    }

    /**
     * @param centerI6
     */
    public void setCenterI6(BigDecimal centerI6) {
        this.centerI6 = centerI6;
    }

    /**
     * @return center_i_7
     */
    public BigDecimal getCenterI7() {
        return centerI7;
    }

    /**
     * @param centerI7
     */
    public void setCenterI7(BigDecimal centerI7) {
        this.centerI7 = centerI7;
    }

    /**
     * @return center_i_8
     */
    public BigDecimal getCenterI8() {
        return centerI8;
    }

    /**
     * @param centerI8
     */
    public void setCenterI8(BigDecimal centerI8) {
        this.centerI8 = centerI8;
    }

    /**
     * @return center_i_9
     */
    public BigDecimal getCenterI9() {
        return centerI9;
    }

    /**
     * @param centerI9
     */
    public void setCenterI9(BigDecimal centerI9) {
        this.centerI9 = centerI9;
    }

    /**
     * @return center_i_10
     */
    public BigDecimal getCenterI10() {
        return centerI10;
    }

    /**
     * @param centerI10
     */
    public void setCenterI10(BigDecimal centerI10) {
        this.centerI10 = centerI10;
    }

    /**
     * 获取直流输入功率
     *
     * @return mppt_power - 直流输入功率
     */
    public BigDecimal getMpptPower() {
        return mpptPower;
    }

    /**
     * 设置直流输入功率
     *
     * @param mpptPower 直流输入功率
     */
    public void setMpptPower(BigDecimal mpptPower) {
        this.mpptPower = mpptPower;
    }

    /**
     * 获取a箱电压
     *
     * @return a_u - a箱电压
     */
    public BigDecimal getaU() {
        return aU;
    }

    /**
     * 设置a箱电压
     *
     * @param aU a箱电压
     */
    public void setaU(BigDecimal aU) {
        this.aU = aU;
    }

    /**
     * @return b_u
     */
    public BigDecimal getbU() {
        return bU;
    }

    /**
     * @param bU
     */
    public void setbU(BigDecimal bU) {
        this.bU = bU;
    }

    /**
     * @return c_u
     */
    public BigDecimal getcU() {
        return cU;
    }

    /**
     * @param cU
     */
    public void setcU(BigDecimal cU) {
        this.cU = cU;
    }

    /**
     * 获取电网a箱电流
     *
     * @return a_i - 电网a箱电流
     */
    public BigDecimal getaI() {
        return aI;
    }

    /**
     * 设置电网a箱电流
     *
     * @param aI 电网a箱电流
     */
    public void setaI(BigDecimal aI) {
        this.aI = aI;
    }

    /**
     * @return b_i
     */
    public BigDecimal getbI() {
        return bI;
    }

    /**
     * @param bI
     */
    public void setbI(BigDecimal bI) {
        this.bI = bI;
    }

    /**
     * @return c_i
     */
    public BigDecimal getcI() {
        return cI;
    }

    /**
     * @param cI
     */
    public void setcI(BigDecimal cI) {
        this.cI = cI;
    }

    /**
     * 获取功率因数
     *
     * @return power_factor - 功率因数
     */
    public BigDecimal getPowerFactor() {
        return powerFactor;
    }

    /**
     * 设置功率因数
     *
     * @param powerFactor 功率因数
     */
    public void setPowerFactor(BigDecimal powerFactor) {
        this.powerFactor = powerFactor;
    }

    /**
     * 获取并网频率
     *
     * @return elec_freq - 并网频率
     */
    public BigDecimal getGridFrequency() {
        return gridFrequency;
    }

    /**
     * 设置并网频率
     *
     * @param gridFrequency 并网频率
     */
    public void setGridFrequency(BigDecimal gridFrequency) {
        this.gridFrequency = gridFrequency;
    }

    /**
     * 获取交流输出功率
     *
     * @return active_power - 交流输出功率
     */
    public BigDecimal getActivePower() {
        return activePower;
    }

    /**
     * 设置交流输出功率
     *
     * @param activePower 交流输出功率
     */
    public void setActivePower(BigDecimal activePower) {
        this.activePower = activePower;
    }

    /**
     * 获取输出无功功率
     *
     * @return reactive_power - 输出无功功率
     */
    public BigDecimal getReactivePower() {
        return reactivePower;
    }

    /**
     * 设置输出无功功率
     *
     * @param reactivePower 输出无功功率
     */
    public void setReactivePower(BigDecimal reactivePower) {
        this.reactivePower = reactivePower;
    }

    /**
     * 获取逆变器开机时间
     *
     * @return open_time - 逆变器开机时间
     */
    public Long getOnTime() {
        return onTime;
    }

    /**
     * 设置逆变器开机时间
     *
     * @param onTime 逆变器开机时间
     */
    public void setOnTime(Long onTime) {
        this.onTime = onTime;
    }

    /**
     * 获取逆变器关机时间
     *
     * @return close_time - 逆变器关机时间
     */
    public Long getOffTime() {
        return offTime;
    }

    /**
     * 设置逆变器关机时间
     *
     * @param offTime 逆变器关机时间
     */
    public void setOffTime(Long offTime) {
        this.offTime = offTime;
    }

    /**
     * 获取生产可靠性
     *
     * @return aop - 生产可靠性
     */
    public String getAop() {
        return aop;
    }

    /**
     * 设置生产可靠性
     *
     * @param aop 生产可靠性
     */
    public void setAop(String aop) {
        this.aop = aop;
    }

    /**
     * 获取预留字段1
     *
     * @return ext1 - 预留字段1
     */
    public BigDecimal getExt1() {
        return ext1;
    }

    /**
     * 设置预留字段1
     *
     * @param ext1 预留字段1
     */
    public void setExt1(BigDecimal ext1) {
        this.ext1 = ext1;
    }

    /**
     * 获取预留字段2
     *
     * @return ext2 - 预留字段2
     */
    public BigDecimal getExt2() {
        return ext2;
    }

    /**
     * 设置预留字段2
     *
     * @param ext2 预留字段2
     */
    public void setExt2(BigDecimal ext2) {
        this.ext2 = ext2;
    }

    /**
     * 获取预留字段3
     *
     * @return ext_str_1 - 预留字段3
     */
    public String getExtStr1() {
        return extStr1;
    }

    /**
     * 设置预留字段3
     *
     * @param extStr1 预留字段3
     */
    public void setExtStr1(String extStr1) {
        this.extStr1 = extStr1;
    }
}