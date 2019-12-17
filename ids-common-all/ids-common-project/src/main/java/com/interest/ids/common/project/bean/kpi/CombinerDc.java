package com.interest.ids.common.project.bean.kpi;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "ids_combiner_dc_data_t")
public class CombinerDc {
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
     * 第1路电流值
     */
    @Column(name = "dc_i1")
    private BigDecimal dcI1;

    @Column(name = "dc_i2")
    private BigDecimal dcI2;

    @Column(name = "dc_i3")
    private BigDecimal dcI3;

    @Column(name = "dc_i4")
    private BigDecimal dcI4;

    @Column(name = "dc_i5")
    private BigDecimal dcI5;

    @Column(name = "dc_i6")
    private BigDecimal dcI6;

    @Column(name = "dc_i7")
    private BigDecimal dcI7;

    @Column(name = "dc_i8")
    private BigDecimal dcI8;

    @Column(name = "dc_i9")
    private BigDecimal dcI9;

    @Column(name = "dc_i10")
    private BigDecimal dcI10;

    @Column(name = "dc_i11")
    private BigDecimal dcI11;

    @Column(name = "dc_i12")
    private BigDecimal dcI12;

    @Column(name = "dc_i13")
    private BigDecimal dcI13;

    @Column(name = "dc_i14")
    private BigDecimal dcI14;

    @Column(name = "dc_i15")
    private BigDecimal dcI15;

    @Column(name = "dc_i16")
    private BigDecimal dcI16;

    @Column(name = "dc_i17")
    private BigDecimal dcI17;

    @Column(name = "dc_i18")
    private BigDecimal dcI18;

    @Column(name = "dc_i19")
    private BigDecimal dcI19;

    @Column(name = "dc_i20")
    private BigDecimal dcI20;

    /**
     * 光伏电流
     */
    @Column(name = "photc_i")
    private BigDecimal photcI;

    /**
     * 光伏电压
     */
    @Column(name = "photc_u")
    private BigDecimal photcU;

    /**
     * 温度
     */
    private BigDecimal temprature;

    /**
     * 雷击次数
     */
    @Column(name = "struck_count")
    private Integer struckCount;

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
     * 获取第1路电流值
     *
     * @return dc_i1 - 第1路电流值
     */
    public BigDecimal getDcI1() {
        return dcI1;
    }

    /**
     * 设置第1路电流值
     *
     * @param dcI1 第1路电流值
     */
    public void setDcI1(BigDecimal dcI1) {
        this.dcI1 = dcI1;
    }

    /**
     * @return dc_i2
     */
    public BigDecimal getDcI2() {
        return dcI2;
    }

    /**
     * @param dcI2
     */
    public void setDcI2(BigDecimal dcI2) {
        this.dcI2 = dcI2;
    }

    /**
     * @return dc_i3
     */
    public BigDecimal getDcI3() {
        return dcI3;
    }

    /**
     * @param dcI3
     */
    public void setDcI3(BigDecimal dcI3) {
        this.dcI3 = dcI3;
    }

    /**
     * @return dc_i4
     */
    public BigDecimal getDcI4() {
        return dcI4;
    }

    /**
     * @param dcI4
     */
    public void setDcI4(BigDecimal dcI4) {
        this.dcI4 = dcI4;
    }

    /**
     * @return dc_i5
     */
    public BigDecimal getDcI5() {
        return dcI5;
    }

    /**
     * @param dcI5
     */
    public void setDcI5(BigDecimal dcI5) {
        this.dcI5 = dcI5;
    }

    /**
     * @return dc_i6
     */
    public BigDecimal getDcI6() {
        return dcI6;
    }

    /**
     * @param dcI6
     */
    public void setDcI6(BigDecimal dcI6) {
        this.dcI6 = dcI6;
    }

    /**
     * @return dc_i7
     */
    public BigDecimal getDcI7() {
        return dcI7;
    }

    /**
     * @param dcI7
     */
    public void setDcI7(BigDecimal dcI7) {
        this.dcI7 = dcI7;
    }

    /**
     * @return dc_i8
     */
    public BigDecimal getDcI8() {
        return dcI8;
    }

    /**
     * @param dcI8
     */
    public void setDcI8(BigDecimal dcI8) {
        this.dcI8 = dcI8;
    }

    /**
     * @return dc_i9
     */
    public BigDecimal getDcI9() {
        return dcI9;
    }

    /**
     * @param dcI9
     */
    public void setDcI9(BigDecimal dcI9) {
        this.dcI9 = dcI9;
    }

    /**
     * @return dc_i10
     */
    public BigDecimal getDcI10() {
        return dcI10;
    }

    /**
     * @param dcI10
     */
    public void setDcI10(BigDecimal dcI10) {
        this.dcI10 = dcI10;
    }

    /**
     * @return dc_i11
     */
    public BigDecimal getDcI11() {
        return dcI11;
    }

    /**
     * @param dcI11
     */
    public void setDcI11(BigDecimal dcI11) {
        this.dcI11 = dcI11;
    }

    /**
     * @return dc_i12
     */
    public BigDecimal getDcI12() {
        return dcI12;
    }

    /**
     * @param dcI12
     */
    public void setDcI12(BigDecimal dcI12) {
        this.dcI12 = dcI12;
    }

    /**
     * @return dc_i13
     */
    public BigDecimal getDcI13() {
        return dcI13;
    }

    /**
     * @param dcI13
     */
    public void setDcI13(BigDecimal dcI13) {
        this.dcI13 = dcI13;
    }

    /**
     * @return dc_i14
     */
    public BigDecimal getDcI14() {
        return dcI14;
    }

    /**
     * @param dcI14
     */
    public void setDcI14(BigDecimal dcI14) {
        this.dcI14 = dcI14;
    }

    /**
     * @return dc_i15
     */
    public BigDecimal getDcI15() {
        return dcI15;
    }

    /**
     * @param dcI15
     */
    public void setDcI15(BigDecimal dcI15) {
        this.dcI15 = dcI15;
    }

    /**
     * @return dc_i16
     */
    public BigDecimal getDcI16() {
        return dcI16;
    }

    /**
     * @param dcI16
     */
    public void setDcI16(BigDecimal dcI16) {
        this.dcI16 = dcI16;
    }

    /**
     * @return dc_i17
     */
    public BigDecimal getDcI17() {
        return dcI17;
    }

    /**
     * @param dcI17
     */
    public void setDcI17(BigDecimal dcI17) {
        this.dcI17 = dcI17;
    }

    /**
     * @return dc_i18
     */
    public BigDecimal getDcI18() {
        return dcI18;
    }

    /**
     * @param dcI18
     */
    public void setDcI18(BigDecimal dcI18) {
        this.dcI18 = dcI18;
    }

    /**
     * @return dc_i19
     */
    public BigDecimal getDcI19() {
        return dcI19;
    }

    /**
     * @param dcI19
     */
    public void setDcI19(BigDecimal dcI19) {
        this.dcI19 = dcI19;
    }

    /**
     * @return dc_i20
     */
    public BigDecimal getDcI20() {
        return dcI20;
    }

    /**
     * @param dcI20
     */
    public void setDcI20(BigDecimal dcI20) {
        this.dcI20 = dcI20;
    }

    /**
     * 获取光伏电流
     *
     * @return photc_i - 光伏电流
     */
    public BigDecimal getPhotcI() {
        return photcI;
    }

    /**
     * 设置光伏电流
     *
     * @param photcI 光伏电流
     */
    public void setPhotcI(BigDecimal photcI) {
        this.photcI = photcI;
    }

    /**
     * 获取光伏电压
     *
     * @return photc_u - 光伏电压
     */
    public BigDecimal getPhotcU() {
        return photcU;
    }

    /**
     * 设置光伏电压
     *
     * @param photcU 光伏电压
     */
    public void setPhotcU(BigDecimal photcU) {
        this.photcU = photcU;
    }

    /**
     * 获取温度
     *
     * @return temprature - 温度
     */
    public BigDecimal getTemprature() {
        return temprature;
    }

    /**
     * 设置温度
     *
     * @param temprature 温度
     */
    public void setTemprature(BigDecimal temprature) {
        this.temprature = temprature;
    }

    /**
     * 获取雷击次数
     *
     * @return thunder_count - 雷击次数
     */
    public Integer getStruckCount() {
        return struckCount;
    }

    /**
     * 设置雷击次数
     *
     * @param struckCount 雷击次数
     */
    public void setStruckCount(Integer struckCount) {
        this.struckCount = struckCount;
    }
}