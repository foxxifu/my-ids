package com.interest.ids.common.project.bean.kpi;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "ids_meter_data_t")
public class Meter {
    @Id
    @Column(name = "collect_time")
    private Long collectTime;

    @Id
    @Column(name = "dev_id")
    private Long deviceId;

    @Id
    @Column(name = "station_code")
    private String stationCode;


    @Column(name = "dev_name")
    private String deviceName;

    /**
     * 判断当前指标是否有效：0代表有效，1代表无效  格式010000000000 : 代表第2位也就是bc_u无效
     */
    private String valid;

    /**
     * 设备dev_type_id, 用于区分不同的电表, 17: 关口电表    47: 户用电表
     */
    @Column(name = "meter_type")
    private Integer meterType;

    /**
     * 电表状态
     */
    @Column(name = "status")
    private String status;

    /**
     * 电网电压
     */
    @Column(name = "grid_u")
    private BigDecimal gridU;

    /**
     * 电网电流
     */
    @Column(name = "grid_i")
    private BigDecimal gridI;

    /**
     * 电网ab线电压
     */
    @Column(name = "ab_u")
    private BigDecimal abU;

    /**
     * 电网bc线电压
     */
    @Column(name = "bc_u")
    private BigDecimal bcU;

    /**
     * 电网ca线电压
     */
    @Column(name = "ca_u")
    private BigDecimal caU;

    /**
     * a相电压（交流输出）
     */
    @Column(name = "a_u")
    private BigDecimal aU;

    /**
     * b相电压（交流输出）
     */
    @Column(name = "b_u")
    private BigDecimal bU;

    /**
     * c相电压（交流输出）
     */
    @Column(name = "c_u")
    private BigDecimal cU;

    /**
     * 电网a相电流(ia)
     */
    @Column(name = "a_i")
    private BigDecimal aI;

    /**
     * 电网b相电流(ib)
     */
    @Column(name = "b_i")
    private BigDecimal bI;

    /**
     * 电网c相电流(ic)
     */
    @Column(name = "c_i")
    private BigDecimal cI;

    /**
     * 有功功率
     */
    @Column(name = "active_power")
    private BigDecimal activePower;

    /**
     * 功率因数
     */
    @Column(name = "power_factor")
    private BigDecimal powerFactor;

    /**
     * 有功电量(正向有功电度)
     */
    @Column(name = "active_capacity")
    private BigDecimal activeCapacity;

    /**
     * 无功功率
     */
    @Column(name = "reactive_power")
    private BigDecimal reactivePower;

    /**
     * 反向有功电度
     */
    @Column(name = "reverse_active_cap")
    private BigDecimal reverseActiveCap;

    /**
     * 正向无功电度
     */
    @Column(name = "forward_reactive_cap")
    private BigDecimal forwardReactiveCap;

    /**
     * 反向无功电度
     */
    @Column(name = "reverse_reactive_cap")
    private BigDecimal reverseReactiveCap;

    /**
     * 有功功率pa(kw)
     */
    @Column(name = "active_power_a")
    private BigDecimal activePowerA;

    /**
     * 有功功率pb(kw)
     */
    @Column(name = "active_power_b")
    private BigDecimal activePowerB;

    /**
     * 有功功率pc(kw)
     */
    @Column(name = "active_power_c")
    private BigDecimal activePowerC;

    /**
     * 无功功率qa(kvar)
     */
    @Column(name = "reactive_power_a")
    private BigDecimal reactivePowerA;

    /**
     * 无功功率qb(kvar)
     */
    @Column(name = "reactive_power_b")
    private BigDecimal reactivePowerB;

    /**
     * 无功功率qc(kvar)
     */
    @Column(name = "reactive_power_c")
    private BigDecimal reactivePowerC;

    /**
     * 总视在功率(kva)
     */
    @Column(name = "total_apparent_power")
    private BigDecimal totalApparentPower;

    /**
     * 电网频率(hz)
     */
    @Column(name = "grid_frequency")
    private BigDecimal gridFrequency;

    /**
     * 反向有功电度（峰）(kwh)
     */
    @Column(name = "reverse_active_peak")
    private BigDecimal reverseActivePeak;

    /**
     * 反向有功电度（平）(kwh)
     */
    @Column(name = "reverse_active_power")
    private BigDecimal reverseActivePower;

    /**
     * 反向有功电度（谷）(kwh)
     */
    @Column(name = "reverse_active_valley")
    private BigDecimal reverseActiveValley;

    /**
     * 反向有功电度（尖峰）(kwh)
     */
    @Column(name = "reverse_active_top")
    private BigDecimal reverseActiveTop;

    /**
     * 正向有功电度（峰）(kwh)
     */
    @Column(name = "positive_active_peak")
    private BigDecimal positiveActivePeak;

    /**
     * 正向有功电度（平）(kwh)
     */
    @Column(name = "positive_active_power")
    private BigDecimal positiveActivePower;

    /**
     * 正向有功电度（谷）(kwh)
     */
    @Column(name = "positive_active_valley")
    private BigDecimal positiveActiveValley;

    /**
     * 正向有功电度（尖峰）(kwh)
     */
    @Column(name = "positive_active_top")
    private BigDecimal positiveActiveTop;

    /**
     * 反向无功电度（峰）(kvarh)
     */
    @Column(name = "reverse_reactive_peak")
    private BigDecimal reverseReactivePeak;

    /**
     * 反向无功电度（平）(kvarh)
     */
    @Column(name = "reverse_reactive_power")
    private BigDecimal reverseReactivePower;

    /**
     * 反向无功电度（谷）(kvarh)
     */
    @Column(name = "reverse_reactive_valley")
    private BigDecimal reverseReactiveValley;

    /**
     * 反向无功电度（尖峰）(kvarh)
     */
    @Column(name = "reverse_reactive_top")
    private BigDecimal reverseReactiveTop;

    /**
     * 正向无功电度（峰）(kvarh)
     */
    @Column(name = "positive_reactive_peak")
    private BigDecimal positiveReactivePeak;

    /**
     * 正向无功电度（平）(kvarh)
     */
    @Column(name = "positive_reactive_power")
    private BigDecimal positiveReactivePower;

    /**
     * 正向无功电度（谷）(kvarh)
     */
    @Column(name = "positive_reactive_valley")
    private BigDecimal positiveReactiveValley;

    /**
     * 正向无功电度（尖峰）(kvarh)
     */
    @Column(name = "positive_reactive_top")
    private BigDecimal positiveReactiveTop;

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

    /**
     * 获取设备dev_type_id, 用于区分不同的电表, 17: 关口电表    47: 户用电表
     *
     * @return meter_type - 设备dev_type_id, 用于区分不同的电表, 17: 关口电表    47: 户用电表
     */
    public Integer getMeterType() {
        return meterType;
    }

    /**
     * 设置设备dev_type_id, 用于区分不同的电表, 17: 关口电表    47: 户用电表
     *
     * @param meterType 设备dev_type_id, 用于区分不同的电表, 17: 关口电表    47: 户用电表
     */
    public void setMeterType(Integer meterType) {
        this.meterType = meterType;
    }

    /**
     * 获取电表状态
     *
     * @return meter_status - 电表状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置电表状态
     *
     * @param status 电表状态
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取电网ab线电压
     *
     * @return ab_u - 电网ab线电压
     */
    public BigDecimal getAbU() {
        return abU;
    }

    /**
     * 设置电网ab线电压
     *
     * @param abU 电网ab线电压
     */
    public void setAbU(BigDecimal abU) {
        this.abU = abU;
    }

    /**
     * 获取电网bc线电压
     *
     * @return bc_u - 电网bc线电压
     */
    public BigDecimal getBcU() {
        return bcU;
    }

    /**
     * 设置电网bc线电压
     *
     * @param bcU 电网bc线电压
     */
    public void setBcU(BigDecimal bcU) {
        this.bcU = bcU;
    }

    /**
     * 获取电网ca线电压
     *
     * @return ca_u - 电网ca线电压
     */
    public BigDecimal getCaU() {
        return caU;
    }

    /**
     * 设置电网ca线电压
     *
     * @param caU 电网ca线电压
     */
    public void setCaU(BigDecimal caU) {
        this.caU = caU;
    }

    /**
     * 获取a相电压（交流输出）
     *
     * @return a_u - a相电压（交流输出）
     */
    public BigDecimal getaU() {
        return aU;
    }

    /**
     * 设置a相电压（交流输出）
     *
     * @param aU a相电压（交流输出）
     */
    public void setaU(BigDecimal aU) {
        this.aU = aU;
    }

    /**
     * 获取b相电压（交流输出）
     *
     * @return b_u - b相电压（交流输出）
     */
    public BigDecimal getbU() {
        return bU;
    }

    /**
     * 设置b相电压（交流输出）
     *
     * @param bU b相电压（交流输出）
     */
    public void setbU(BigDecimal bU) {
        this.bU = bU;
    }

    /**
     * 获取c相电压（交流输出）
     *
     * @return c_u - c相电压（交流输出）
     */
    public BigDecimal getcU() {
        return cU;
    }

    /**
     * 设置c相电压（交流输出）
     *
     * @param cU c相电压（交流输出）
     */
    public void setcU(BigDecimal cU) {
        this.cU = cU;
    }

    /**
     * 获取电网a相电流(ia)
     *
     * @return a_i - 电网a相电流(ia)
     */
    public BigDecimal getaI() {
        return aI;
    }

    /**
     * 设置电网a相电流(ia)
     *
     * @param aI 电网a相电流(ia)
     */
    public void setaI(BigDecimal aI) {
        this.aI = aI;
    }

    /**
     * 获取电网b相电流(ib)
     *
     * @return b_i - 电网b相电流(ib)
     */
    public BigDecimal getbI() {
        return bI;
    }

    /**
     * 设置电网b相电流(ib)
     *
     * @param bI 电网b相电流(ib)
     */
    public void setbI(BigDecimal bI) {
        this.bI = bI;
    }

    /**
     * 获取电网c相电流(ic)
     *
     * @return c_i - 电网c相电流(ic)
     */
    public BigDecimal getcI() {
        return cI;
    }

    /**
     * 设置电网c相电流(ic)
     *
     * @param cI 电网c相电流(ic)
     */
    public void setcI(BigDecimal cI) {
        this.cI = cI;
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
     * 获取有功电量(正向有功电度)
     *
     * @return active_cap - 有功电量(正向有功电度)
     */
    public BigDecimal getActiveCapacity() {
        return activeCapacity;
    }

    /**
     * 设置有功电量(正向有功电度)
     *
     * @param activeCapacity 有功电量(正向有功电度)
     */
    public void setActiveCapacity(BigDecimal activeCapacity) {
        this.activeCapacity = activeCapacity;
    }

    /**
     * 获取无功功率
     *
     * @return reactive_power - 无功功率
     */
    public BigDecimal getReactivePower() {
        return reactivePower;
    }

    /**
     * 设置无功功率
     *
     * @param reactivePower 无功功率
     */
    public void setReactivePower(BigDecimal reactivePower) {
        this.reactivePower = reactivePower;
    }

    /**
     * 获取反向有功电度
     *
     * @return reverse_active_cap - 反向有功电度
     */
    public BigDecimal getReverseActiveCap() {
        return reverseActiveCap;
    }

    /**
     * 设置反向有功电度
     *
     * @param reverseActiveCap 反向有功电度
     */
    public void setReverseActiveCap(BigDecimal reverseActiveCap) {
        this.reverseActiveCap = reverseActiveCap;
    }

    /**
     * 获取正向无功电度
     *
     * @return forward_reactive_cap - 正向无功电度
     */
    public BigDecimal getForwardReactiveCap() {
        return forwardReactiveCap;
    }

    /**
     * 设置正向无功电度
     *
     * @param forwardReactiveCap 正向无功电度
     */
    public void setForwardReactiveCap(BigDecimal forwardReactiveCap) {
        this.forwardReactiveCap = forwardReactiveCap;
    }

    /**
     * 获取反向无功电度
     *
     * @return reverse_reactive_cap - 反向无功电度
     */
    public BigDecimal getReverseReactiveCap() {
        return reverseReactiveCap;
    }

    /**
     * 设置反向无功电度
     *
     * @param reverseReactiveCap 反向无功电度
     */
    public void setReverseReactiveCap(BigDecimal reverseReactiveCap) {
        this.reverseReactiveCap = reverseReactiveCap;
    }

    /**
     * 获取有功功率pa(kw)
     *
     * @return active_power_a - 有功功率pa(kw)
     */
    public BigDecimal getActivePowerA() {
        return activePowerA;
    }

    /**
     * 设置有功功率pa(kw)
     *
     * @param activePowerA 有功功率pa(kw)
     */
    public void setActivePowerA(BigDecimal activePowerA) {
        this.activePowerA = activePowerA;
    }

    /**
     * 获取有功功率pb(kw)
     *
     * @return active_power_b - 有功功率pb(kw)
     */
    public BigDecimal getActivePowerB() {
        return activePowerB;
    }

    /**
     * 设置有功功率pb(kw)
     *
     * @param activePowerB 有功功率pb(kw)
     */
    public void setActivePowerB(BigDecimal activePowerB) {
        this.activePowerB = activePowerB;
    }

    /**
     * 获取有功功率pc(kw)
     *
     * @return active_power_c - 有功功率pc(kw)
     */
    public BigDecimal getActivePowerC() {
        return activePowerC;
    }

    /**
     * 设置有功功率pc(kw)
     *
     * @param activePowerC 有功功率pc(kw)
     */
    public void setActivePowerC(BigDecimal activePowerC) {
        this.activePowerC = activePowerC;
    }

    /**
     * 获取无功功率qa(kvar)
     *
     * @return reactive_power_a - 无功功率qa(kvar)
     */
    public BigDecimal getReactivePowerA() {
        return reactivePowerA;
    }

    /**
     * 设置无功功率qa(kvar)
     *
     * @param reactivePowerA 无功功率qa(kvar)
     */
    public void setReactivePowerA(BigDecimal reactivePowerA) {
        this.reactivePowerA = reactivePowerA;
    }

    /**
     * 获取无功功率qb(kvar)
     *
     * @return reactive_power_b - 无功功率qb(kvar)
     */
    public BigDecimal getReactivePowerB() {
        return reactivePowerB;
    }

    /**
     * 设置无功功率qb(kvar)
     *
     * @param reactivePowerB 无功功率qb(kvar)
     */
    public void setReactivePowerB(BigDecimal reactivePowerB) {
        this.reactivePowerB = reactivePowerB;
    }

    /**
     * 获取无功功率qc(kvar)
     *
     * @return reactive_power_c - 无功功率qc(kvar)
     */
    public BigDecimal getReactivePowerC() {
        return reactivePowerC;
    }

    /**
     * 设置无功功率qc(kvar)
     *
     * @param reactivePowerC 无功功率qc(kvar)
     */
    public void setReactivePowerC(BigDecimal reactivePowerC) {
        this.reactivePowerC = reactivePowerC;
    }

    /**
     * 获取总视在功率(kva)
     *
     * @return total_apparent_power - 总视在功率(kva)
     */
    public BigDecimal getTotalApparentPower() {
        return totalApparentPower;
    }

    /**
     * 设置总视在功率(kva)
     *
     * @param totalApparentPower 总视在功率(kva)
     */
    public void setTotalApparentPower(BigDecimal totalApparentPower) {
        this.totalApparentPower = totalApparentPower;
    }

    /**
     * 获取电网频率(hz)
     *
     * @return grid_frequency - 电网频率(hz)
     */
    public BigDecimal getGridFrequency() {
        return gridFrequency;
    }

    /**
     * 设置电网频率(hz)
     *
     * @param gridFrequency 电网频率(hz)
     */
    public void setGridFrequency(BigDecimal gridFrequency) {
        this.gridFrequency = gridFrequency;
    }

    /**
     * 获取反向有功电度（峰）(kwh)
     *
     * @return reverse_active_peak - 反向有功电度（峰）(kwh)
     */
    public BigDecimal getReverseActivePeak() {
        return reverseActivePeak;
    }

    /**
     * 设置反向有功电度（峰）(kwh)
     *
     * @param reverseActivePeak 反向有功电度（峰）(kwh)
     */
    public void setReverseActivePeak(BigDecimal reverseActivePeak) {
        this.reverseActivePeak = reverseActivePeak;
    }

    /**
     * 获取反向有功电度（平）(kwh)
     *
     * @return reverse_active_power - 反向有功电度（平）(kwh)
     */
    public BigDecimal getReverseActivePower() {
        return reverseActivePower;
    }

    /**
     * 设置反向有功电度（平）(kwh)
     *
     * @param reverseActivePower 反向有功电度（平）(kwh)
     */
    public void setReverseActivePower(BigDecimal reverseActivePower) {
        this.reverseActivePower = reverseActivePower;
    }

    /**
     * 获取反向有功电度（谷）(kwh)
     *
     * @return reverse_active_valley - 反向有功电度（谷）(kwh)
     */
    public BigDecimal getReverseActiveValley() {
        return reverseActiveValley;
    }

    /**
     * 设置反向有功电度（谷）(kwh)
     *
     * @param reverseActiveValley 反向有功电度（谷）(kwh)
     */
    public void setReverseActiveValley(BigDecimal reverseActiveValley) {
        this.reverseActiveValley = reverseActiveValley;
    }

    /**
     * 获取反向有功电度（尖峰）(kwh)
     *
     * @return reverse_active_top - 反向有功电度（尖峰）(kwh)
     */
    public BigDecimal getReverseActiveTop() {
        return reverseActiveTop;
    }

    /**
     * 设置反向有功电度（尖峰）(kwh)
     *
     * @param reverseActiveTop 反向有功电度（尖峰）(kwh)
     */
    public void setReverseActiveTop(BigDecimal reverseActiveTop) {
        this.reverseActiveTop = reverseActiveTop;
    }

    /**
     * 获取正向有功电度（峰）(kwh)
     *
     * @return positive_active_peak - 正向有功电度（峰）(kwh)
     */
    public BigDecimal getPositiveActivePeak() {
        return positiveActivePeak;
    }

    /**
     * 设置正向有功电度（峰）(kwh)
     *
     * @param positiveActivePeak 正向有功电度（峰）(kwh)
     */
    public void setPositiveActivePeak(BigDecimal positiveActivePeak) {
        this.positiveActivePeak = positiveActivePeak;
    }

    /**
     * 获取正向有功电度（平）(kwh)
     *
     * @return positive_active_power - 正向有功电度（平）(kwh)
     */
    public BigDecimal getPositiveActivePower() {
        return positiveActivePower;
    }

    /**
     * 设置正向有功电度（平）(kwh)
     *
     * @param positiveActivePower 正向有功电度（平）(kwh)
     */
    public void setPositiveActivePower(BigDecimal positiveActivePower) {
        this.positiveActivePower = positiveActivePower;
    }

    /**
     * 获取正向有功电度（谷）(kwh)
     *
     * @return positive_active_valley - 正向有功电度（谷）(kwh)
     */
    public BigDecimal getPositiveActiveValley() {
        return positiveActiveValley;
    }

    /**
     * 设置正向有功电度（谷）(kwh)
     *
     * @param positiveActiveValley 正向有功电度（谷）(kwh)
     */
    public void setPositiveActiveValley(BigDecimal positiveActiveValley) {
        this.positiveActiveValley = positiveActiveValley;
    }

    /**
     * 获取正向有功电度（尖峰）(kwh)
     *
     * @return positive_active_top - 正向有功电度（尖峰）(kwh)
     */
    public BigDecimal getPositiveActiveTop() {
        return positiveActiveTop;
    }

    /**
     * 设置正向有功电度（尖峰）(kwh)
     *
     * @param positiveActiveTop 正向有功电度（尖峰）(kwh)
     */
    public void setPositiveActiveTop(BigDecimal positiveActiveTop) {
        this.positiveActiveTop = positiveActiveTop;
    }

    /**
     * 获取反向无功电度（峰）(kvarh)
     *
     * @return reverse_reactive_peak - 反向无功电度（峰）(kvarh)
     */
    public BigDecimal getReverseReactivePeak() {
        return reverseReactivePeak;
    }

    /**
     * 设置反向无功电度（峰）(kvarh)
     *
     * @param reverseReactivePeak 反向无功电度（峰）(kvarh)
     */
    public void setReverseReactivePeak(BigDecimal reverseReactivePeak) {
        this.reverseReactivePeak = reverseReactivePeak;
    }

    /**
     * 获取反向无功电度（平）(kvarh)
     *
     * @return reverse_reactive_power - 反向无功电度（平）(kvarh)
     */
    public BigDecimal getReverseReactivePower() {
        return reverseReactivePower;
    }

    /**
     * 设置反向无功电度（平）(kvarh)
     *
     * @param reverseReactivePower 反向无功电度（平）(kvarh)
     */
    public void setReverseReactivePower(BigDecimal reverseReactivePower) {
        this.reverseReactivePower = reverseReactivePower;
    }

    /**
     * 获取反向无功电度（谷）(kvarh)
     *
     * @return reverse_reactive_valley - 反向无功电度（谷）(kvarh)
     */
    public BigDecimal getReverseReactiveValley() {
        return reverseReactiveValley;
    }

    /**
     * 设置反向无功电度（谷）(kvarh)
     *
     * @param reverseReactiveValley 反向无功电度（谷）(kvarh)
     */
    public void setReverseReactiveValley(BigDecimal reverseReactiveValley) {
        this.reverseReactiveValley = reverseReactiveValley;
    }

    /**
     * 获取反向无功电度（尖峰）(kvarh)
     *
     * @return reverse_reactive_top - 反向无功电度（尖峰）(kvarh)
     */
    public BigDecimal getReverseReactiveTop() {
        return reverseReactiveTop;
    }

    /**
     * 设置反向无功电度（尖峰）(kvarh)
     *
     * @param reverseReactiveTop 反向无功电度（尖峰）(kvarh)
     */
    public void setReverseReactiveTop(BigDecimal reverseReactiveTop) {
        this.reverseReactiveTop = reverseReactiveTop;
    }

    /**
     * 获取正向无功电度（峰）(kvarh)
     *
     * @return positive_reactive_peak - 正向无功电度（峰）(kvarh)
     */
    public BigDecimal getPositiveReactivePeak() {
        return positiveReactivePeak;
    }

    /**
     * 设置正向无功电度（峰）(kvarh)
     *
     * @param positiveReactivePeak 正向无功电度（峰）(kvarh)
     */
    public void setPositiveReactivePeak(BigDecimal positiveReactivePeak) {
        this.positiveReactivePeak = positiveReactivePeak;
    }

    /**
     * 获取正向无功电度（平）(kvarh)
     *
     * @return positive_reactive_power - 正向无功电度（平）(kvarh)
     */
    public BigDecimal getPositiveReactivePower() {
        return positiveReactivePower;
    }

    /**
     * 设置正向无功电度（平）(kvarh)
     *
     * @param positiveReactivePower 正向无功电度（平）(kvarh)
     */
    public void setPositiveReactivePower(BigDecimal positiveReactivePower) {
        this.positiveReactivePower = positiveReactivePower;
    }

    /**
     * 获取正向无功电度（谷）(kvarh)
     *
     * @return positive_reactive_valley - 正向无功电度（谷）(kvarh)
     */
    public BigDecimal getPositiveReactiveValley() {
        return positiveReactiveValley;
    }

    /**
     * 设置正向无功电度（谷）(kvarh)
     *
     * @param positiveReactiveValley 正向无功电度（谷）(kvarh)
     */
    public void setPositiveReactiveValley(BigDecimal positiveReactiveValley) {
        this.positiveReactiveValley = positiveReactiveValley;
    }

    /**
     * 获取正向无功电度（尖峰）(kvarh)
     *
     * @return positive_reactive_top - 正向无功电度（尖峰）(kvarh)
     */
    public BigDecimal getPositiveReactiveTop() {
        return positiveReactiveTop;
    }
    
    public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public BigDecimal getGridU() {
		return gridU;
	}

	public void setGridU(BigDecimal gridU) {
		this.gridU = gridU;
	}

	public BigDecimal getGridI() {
		return gridI;
	}

	public void setGridI(BigDecimal gridI) {
		this.gridI = gridI;
	}

	/**
     * 设置正向无功电度（尖峰）(kvarh)
     *
     * @param positiveReactiveTop 正向无功电度（尖峰）(kvarh)
     */
    public void setPositiveReactiveTop(BigDecimal positiveReactiveTop) {
        this.positiveReactiveTop = positiveReactiveTop;
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