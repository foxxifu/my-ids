package com.interest.ids.common.project.bean.kpi;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "ids_inverter_string_data_t")
public class InverterString {
    @Id
    @Column(name = "collect_time")
    private Long collectTime;

    /**
     * 设备唯一标识
     */
    @Id
    @Column(name = "dev_id")
    private Long devId;

    @Id
    @Column(name = "station_code")
    private String stationCode;

    @Column(name = "dev_name")
    private String devName;

    @Column(name = "inverter_state")
    private Integer inverterState;

    /**
     * 判断当前指标是否有效：0代表有效，1代表无效  格式010000000000 : 代表第2位也就是bc_u无效
     */
    private String valid;


    /**
     * 电网ab电压
     */
    @Column(name = "ab_u")
    private BigDecimal abU;

    @Column(name = "bc_u")
    private BigDecimal bcU;

    @Column(name = "ca_u")
    private BigDecimal caU;

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
     * 逆变器转换效率(厂家)
     */
    @Column(name = "conversion_efficiency")
    private BigDecimal conversionEfficiency;

    /**
     * 机内温度
     */
    private BigDecimal temperature;

    /**
     * 功率因数
     */
    @Column(name = "power_factor")
    private BigDecimal powerFactor;

    /**
     * 电网频率
     */
    @Column(name = "grid_frequency")
    private BigDecimal gridFrequency;

    /**
     * 有功功率
     */
    @Column(name = "active_power")
    private BigDecimal activePower;

    /**
     * 输出无功功率
     */
    @Column(name = "reactive_power")
    private BigDecimal reactivePower;

    /**
     * 当日发电量
     */
    @Column(name = "day_capacity")
    private BigDecimal dayCapacity;

    /**
     * mppt输入总功率
     */
    @Column(name = "mppt_power")
    private BigDecimal mpptPower;

    /**
     * pv1输入电压
     */
    @Column(name = "pv1_u")
    private BigDecimal pv1U;

    @Column(name = "pv2_u")
    private BigDecimal pv2U;

    @Column(name = "pv3_u")
    private BigDecimal pv3U;

    @Column(name = "pv4_u")
    private BigDecimal pv4U;

    @Column(name = "pv5_u")
    private BigDecimal pv5U;

    @Column(name = "pv6_u")
    private BigDecimal pv6U;

    @Column(name = "pv7_u")
    private BigDecimal pv7U;

    @Column(name = "pv8_u")
    private BigDecimal pv8U;

    @Column(name = "pv9_u")
    private BigDecimal pv9U;

    @Column(name = "pv10_u")
    private BigDecimal pv10U;

    @Column(name = "pv11_u")
    private BigDecimal pv11U;

    @Column(name = "pv12_u")
    private BigDecimal pv12U;

    @Column(name = "pv13_u")
    private BigDecimal pv13U;

    @Column(name = "pv14_u")
    private BigDecimal pv14U;

    /**
     * pv1输入电流
     */
    @Column(name = "pv1_i")
    private BigDecimal pv1I;

    @Column(name = "pv2_i")
    private BigDecimal pv2I;

    @Column(name = "pv3_i")
    private BigDecimal pv3I;

    @Column(name = "pv4_i")
    private BigDecimal pv4I;

    @Column(name = "pv5_i")
    private BigDecimal pv5I;

    @Column(name = "pv6_i")
    private BigDecimal pv6I;

    @Column(name = "pv7_i")
    private BigDecimal pv7I;

    @Column(name = "pv8_i")
    private BigDecimal pv8I;

    @Column(name = "pv9_i")
    private BigDecimal pv9I;

    @Column(name = "pv10_i")
    private BigDecimal pv10I;

    @Column(name = "pv11_i")
    private BigDecimal pv11I;

    @Column(name = "pv12_i")
    private BigDecimal pv12I;

    @Column(name = "pv13_i")
    private BigDecimal pv13I;

    @Column(name = "pv14_i")
    private BigDecimal pv14I;

    /**
     * 累计发电量
     */
    @Column(name = "total_capacity")
    private BigDecimal totalCapacity;

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
     * 直流输入总电量
     */
    @Column(name = "mppt_total_cap")
    private BigDecimal mpptTotalCap;

    /**
     * mppt1直流累计发电量
     */
    @Column(name = "mppt_1_cap")
    private BigDecimal mppt1Cap;

    @Column(name = "mppt_2_cap")
    private BigDecimal mppt2Cap;

    @Column(name = "mppt_3_cap")
    private BigDecimal mppt3Cap;

    @Column(name = "mppt_4_cap")
    private BigDecimal mppt4Cap;

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
    private BigDecimal ext3;

    /**
     * 预留字段4
     */
    private BigDecimal ext4;

    /**
     * 预留字段5
     */
    @Column(name = "ext5")
    private String ext5;

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
     * 获取0x0000:待机：初始化;idle:initializing 0x0001:待机：绝缘阻抗检测;idle:iso detecting 0x0002:待机：光照检测;idle:irradiation detecting 0x0100:启动;starting 0x0200:并网;on-grid 0x0201:并网:限功率;on-grid:limited 0x0300:关机：异常关机; shutdown:abnormal 0x0301:关机：指令关机shutdown:forced 0x0401：电网调度：cosψ-p 曲线;grid dispatch:cosψ-p curve 0x0402：电网调度：q-u 曲线;grid dispatch:q-u curve 0xa000:待机：无光照；idle:no irradiation
     *
     * @return inverter_state - 0x0000:待机：初始化;idle:initializing 0x0001:待机：绝缘阻抗检测;idle:iso detecting 0x0002:待机：光照检测;idle:irradiation detecting 0x0100:启动;starting 0x0200:并网;on-grid 0x0201:并网:限功率;on-grid:limited 0x0300:关机：异常关机; shutdown:abnormal 0x0301:关机：指令关机shutdown:forced 0x0401：电网调度：cosψ-p 曲线;grid dispatch:cosψ-p curve 0x0402：电网调度：q-u 曲线;grid dispatch:q-u curve 0xa000:待机：无光照；idle:no irradiation
     */
    public Integer getInverterState() {
        return inverterState;
    }

    /**
     * 设置0x0000:待机：初始化;idle:initializing 0x0001:待机：绝缘阻抗检测;idle:iso detecting 0x0002:待机：光照检测;idle:irradiation detecting 0x0100:启动;starting 0x0200:并网;on-grid 0x0201:并网:限功率;on-grid:limited 0x0300:关机：异常关机; shutdown:abnormal 0x0301:关机：指令关机shutdown:forced 0x0401：电网调度：cosψ-p 曲线;grid dispatch:cosψ-p curve 0x0402：电网调度：q-u 曲线;grid dispatch:q-u curve 0xa000:待机：无光照；idle:no irradiation
     *
     * @param inverterState 0x0000:待机：初始化;idle:initializing 0x0001:待机：绝缘阻抗检测;idle:iso detecting 0x0002:待机：光照检测;idle:irradiation detecting 0x0100:启动;starting 0x0200:并网;on-grid 0x0201:并网:限功率;on-grid:limited 0x0300:关机：异常关机; shutdown:abnormal 0x0301:关机：指令关机shutdown:forced 0x0401：电网调度：cosψ-p 曲线;grid dispatch:cosψ-p curve 0x0402：电网调度：q-u 曲线;grid dispatch:q-u curve 0xa000:待机：无光照；idle:no irradiation
     */
    public void setInverterState(Integer inverterState) {
        this.inverterState = inverterState;
    }

    /**
     * 获取判断当前指标是否有效：0代表有效，1代表无效  格式010000000000 : 代表第2位也就是bc_u无效
     *
     * @return valid - 判断当前指标是否有效：0代表有效，1代表无效  格式010000000000 : 代表第2位也就是bc_u无效
     */
    public String getValid() {
        return valid;
    }

    /**
     * 设置判断当前指标是否有效：0代表有效，1代表无效  格式010000000000 : 代表第2位也就是bc_u无效
     *
     * @param valid 判断当前指标是否有效：0代表有效，1代表无效  格式010000000000 : 代表第2位也就是bc_u无效
     */
    public void setValid(String valid) {
        this.valid = valid;
    }

   
    public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}

	/**
     * 获取电网ab电压
     *
     * @return ab_u - 电网ab电压
     */
    public BigDecimal getAbU() {
        return abU;
    }

    /**
     * 设置电网ab电压
     *
     * @param abU 电网ab电压
     */
    public void setAbU(BigDecimal abU) {
        this.abU = abU;
    }

    /**
     * @return bc_u
     */
    public BigDecimal getBcU() {
        return bcU;
    }

    /**
     * @param bcU
     */
    public void setBcU(BigDecimal bcU) {
        this.bcU = bcU;
    }

    /**
     * @return ca_u
     */
    public BigDecimal getCaU() {
        return caU;
    }

    /**
     * @param caU
     */
    public void setCaU(BigDecimal caU) {
        this.caU = caU;
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
     * 获取逆变器转换效率(厂家)
     *
     * @return conversionEfficiency - 逆变器转换效率(厂家)
     */
    public BigDecimal getConversionEfficiency() {
        return conversionEfficiency;
    }

    /**
     * 设置逆变器转换效率(厂家)
     *
     * @param conversionEfficiency 逆变器转换效率(厂家)
     */
    public void setConversionEfficiency(BigDecimal conversionEfficiency) {
        this.conversionEfficiency = conversionEfficiency;
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
     * 获取电网频率
     *
     * @return elec_freq - 电网频率
     */
    public BigDecimal getGridFrequency() {
        return gridFrequency;
    }

    /**
     * 设置电网频率
     *
     * @param gridFrequency 电网频率
     */
    public void setGridFrequency(BigDecimal gridFrequency) {
        this.gridFrequency = gridFrequency;
    }

    /**
     * 获取有功功率
     *
     * @return active_power - 有功功率
     */
    public BigDecimal getActivePower() {
        return activePower;
    }

    /**
     * 设置有功功率
     *
     * @param activePower 有功功率
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
     * 获取mppt输入总功率
     *
     * @return mppt_power - mppt输入总功率
     */
    public BigDecimal getMpptPower() {
        return mpptPower;
    }

    /**
     * 设置mppt输入总功率
     *
     * @param mpptPower mppt输入总功率
     */
    public void setMpptPower(BigDecimal mpptPower) {
        this.mpptPower = mpptPower;
    }

    /**
     * 获取pv1输入电压
     *
     * @return pv1_u - pv1输入电压
     */
    public BigDecimal getPv1U() {
        return pv1U;
    }

    /**
     * 设置pv1输入电压
     *
     * @param pv1U pv1输入电压
     */
    public void setPv1U(BigDecimal pv1U) {
        this.pv1U = pv1U;
    }

    /**
     * @return pv2_u
     */
    public BigDecimal getPv2U() {
        return pv2U;
    }

    /**
     * @param pv2U
     */
    public void setPv2U(BigDecimal pv2U) {
        this.pv2U = pv2U;
    }

    /**
     * @return pv3_u
     */
    public BigDecimal getPv3U() {
        return pv3U;
    }

    /**
     * @param pv3U
     */
    public void setPv3U(BigDecimal pv3U) {
        this.pv3U = pv3U;
    }

    /**
     * @return pv4_u
     */
    public BigDecimal getPv4U() {
        return pv4U;
    }

    /**
     * @param pv4U
     */
    public void setPv4U(BigDecimal pv4U) {
        this.pv4U = pv4U;
    }

    /**
     * @return pv5_u
     */
    public BigDecimal getPv5U() {
        return pv5U;
    }

    /**
     * @param pv5U
     */
    public void setPv5U(BigDecimal pv5U) {
        this.pv5U = pv5U;
    }

    /**
     * @return pv6_u
     */
    public BigDecimal getPv6U() {
        return pv6U;
    }

    /**
     * @param pv6U
     */
    public void setPv6U(BigDecimal pv6U) {
        this.pv6U = pv6U;
    }

    /**
     * @return pv7_u
     */
    public BigDecimal getPv7U() {
        return pv7U;
    }

    /**
     * @param pv7U
     */
    public void setPv7U(BigDecimal pv7U) {
        this.pv7U = pv7U;
    }

    /**
     * @return pv8_u
     */
    public BigDecimal getPv8U() {
        return pv8U;
    }

    /**
     * @param pv8U
     */
    public void setPv8U(BigDecimal pv8U) {
        this.pv8U = pv8U;
    }

    /**
     * @return pv9_u
     */
    public BigDecimal getPv9U() {
        return pv9U;
    }

    /**
     * @param pv9U
     */
    public void setPv9U(BigDecimal pv9U) {
        this.pv9U = pv9U;
    }

    /**
     * @return pv10_u
     */
    public BigDecimal getPv10U() {
        return pv10U;
    }

    /**
     * @param pv10U
     */
    public void setPv10U(BigDecimal pv10U) {
        this.pv10U = pv10U;
    }

    /**
     * @return pv11_u
     */
    public BigDecimal getPv11U() {
        return pv11U;
    }

    /**
     * @param pv11U
     */
    public void setPv11U(BigDecimal pv11U) {
        this.pv11U = pv11U;
    }

    /**
     * @return pv12_u
     */
    public BigDecimal getPv12U() {
        return pv12U;
    }

    /**
     * @param pv12U
     */
    public void setPv12U(BigDecimal pv12U) {
        this.pv12U = pv12U;
    }

    /**
     * @return pv13_u
     */
    public BigDecimal getPv13U() {
        return pv13U;
    }

    /**
     * @param pv13U
     */
    public void setPv13U(BigDecimal pv13U) {
        this.pv13U = pv13U;
    }

    /**
     * @return pv14_u
     */
    public BigDecimal getPv14U() {
        return pv14U;
    }

    /**
     * @param pv14U
     */
    public void setPv14U(BigDecimal pv14U) {
        this.pv14U = pv14U;
    }

    /**
     * 获取pv1输入电流
     *
     * @return pv1_i - pv1输入电流
     */
    public BigDecimal getPv1I() {
        return pv1I;
    }

    /**
     * 设置pv1输入电流
     *
     * @param pv1I pv1输入电流
     */
    public void setPv1I(BigDecimal pv1I) {
        this.pv1I = pv1I;
    }

    /**
     * @return pv2_i
     */
    public BigDecimal getPv2I() {
        return pv2I;
    }

    /**
     * @param pv2I
     */
    public void setPv2I(BigDecimal pv2I) {
        this.pv2I = pv2I;
    }

    /**
     * @return pv3_i
     */
    public BigDecimal getPv3I() {
        return pv3I;
    }

    /**
     * @param pv3I
     */
    public void setPv3I(BigDecimal pv3I) {
        this.pv3I = pv3I;
    }

    /**
     * @return pv4_i
     */
    public BigDecimal getPv4I() {
        return pv4I;
    }

    /**
     * @param pv4I
     */
    public void setPv4I(BigDecimal pv4I) {
        this.pv4I = pv4I;
    }

    /**
     * @return pv5_i
     */
    public BigDecimal getPv5I() {
        return pv5I;
    }

    /**
     * @param pv5I
     */
    public void setPv5I(BigDecimal pv5I) {
        this.pv5I = pv5I;
    }

    /**
     * @return pv6_i
     */
    public BigDecimal getPv6I() {
        return pv6I;
    }

    /**
     * @param pv6I
     */
    public void setPv6I(BigDecimal pv6I) {
        this.pv6I = pv6I;
    }

    /**
     * @return pv7_i
     */
    public BigDecimal getPv7I() {
        return pv7I;
    }

    /**
     * @param pv7I
     */
    public void setPv7I(BigDecimal pv7I) {
        this.pv7I = pv7I;
    }

    /**
     * @return pv8_i
     */
    public BigDecimal getPv8I() {
        return pv8I;
    }

    /**
     * @param pv8I
     */
    public void setPv8I(BigDecimal pv8I) {
        this.pv8I = pv8I;
    }

    /**
     * @return pv9_i
     */
    public BigDecimal getPv9I() {
        return pv9I;
    }

    /**
     * @param pv9I
     */
    public void setPv9I(BigDecimal pv9I) {
        this.pv9I = pv9I;
    }

    /**
     * @return pv10_i
     */
    public BigDecimal getPv10I() {
        return pv10I;
    }

    /**
     * @param pv10I
     */
    public void setPv10I(BigDecimal pv10I) {
        this.pv10I = pv10I;
    }

    /**
     * @return pv11_i
     */
    public BigDecimal getPv11I() {
        return pv11I;
    }

    /**
     * @param pv11I
     */
    public void setPv11I(BigDecimal pv11I) {
        this.pv11I = pv11I;
    }

    /**
     * @return pv12_i
     */
    public BigDecimal getPv12I() {
        return pv12I;
    }

    /**
     * @param pv12I
     */
    public void setPv12I(BigDecimal pv12I) {
        this.pv12I = pv12I;
    }

    /**
     * @return pv13_i
     */
    public BigDecimal getPv13I() {
        return pv13I;
    }

    /**
     * @param pv13I
     */
    public void setPv13I(BigDecimal pv13I) {
        this.pv13I = pv13I;
    }

    /**
     * @return pv14_i
     */
    public BigDecimal getPv14I() {
        return pv14I;
    }

    /**
     * @param pv14I
     */
    public void setPv14I(BigDecimal pv14I) {
        this.pv14I = pv14I;
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
     * 获取直流输入总电量
     *
     * @return mppt_total_cap - 直流输入总电量
     */
    public BigDecimal getMpptTotalCap() {
        return mpptTotalCap;
    }

    /**
     * 设置直流输入总电量
     *
     * @param mpptTotalCap 直流输入总电量
     */
    public void setMpptTotalCap(BigDecimal mpptTotalCap) {
        this.mpptTotalCap = mpptTotalCap;
    }

    /**
     * 获取mppt1直流累计发电量
     *
     * @return mppt_1_cap - mppt1直流累计发电量
     */
    public BigDecimal getMppt1Cap() {
        return mppt1Cap;
    }

    /**
     * 设置mppt1直流累计发电量
     *
     * @param mppt1Cap mppt1直流累计发电量
     */
    public void setMppt1Cap(BigDecimal mppt1Cap) {
        this.mppt1Cap = mppt1Cap;
    }

    /**
     * @return mppt_2_cap
     */
    public BigDecimal getMppt2Cap() {
        return mppt2Cap;
    }

    /**
     * @param mppt2Cap
     */
    public void setMppt2Cap(BigDecimal mppt2Cap) {
        this.mppt2Cap = mppt2Cap;
    }

    /**
     * @return mppt_3_cap
     */
    public BigDecimal getMppt3Cap() {
        return mppt3Cap;
    }

    /**
     * @param mppt3Cap
     */
    public void setMppt3Cap(BigDecimal mppt3Cap) {
        this.mppt3Cap = mppt3Cap;
    }

    /**
     * @return mppt_4_cap
     */
    public BigDecimal getMppt4Cap() {
        return mppt4Cap;
    }

    /**
     * @param mppt4Cap
     */
    public void setMppt4Cap(BigDecimal mppt4Cap) {
        this.mppt4Cap = mppt4Cap;
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
     * @return ext3 - 预留字段3
     */
    public BigDecimal getExt3() {
        return ext3;
    }

    /**
     * 设置预留字段3
     *
     * @param ext3 预留字段3
     */
    public void setExt3(BigDecimal ext3) {
        this.ext3 = ext3;
    }

    /**
     * 获取预留字段4
     *
     * @return ext4 - 预留字段4
     */
    public BigDecimal getExt4() {
        return ext4;
    }

    /**
     * 设置预留字段4
     *
     * @param ext4 预留字段4
     */
    public void setExt4(BigDecimal ext4) {
        this.ext4 = ext4;
    }

	public Long getDevId() {
		return devId;
	}

	public void setDevId(Long devId) {
		this.devId = devId;
	}

	public Long getOnTime() {
		return onTime;
	}

	public void setOnTime(Long onTime) {
		this.onTime = onTime;
	}

	public Long getOffTime() {
		return offTime;
	}

	public void setOffTime(Long offTime) {
		this.offTime = offTime;
	}

	public String getExt5() {
		return ext5;
	}

	public void setExt5(String ext5) {
		this.ext5 = ext5;
	}
}