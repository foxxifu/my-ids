package com.interest.ids.biz.web.report.vo;

/**
 * @Author: sunbjx
 * @Description: 电站报表VO
 * @Date Created in 20:45 2018/1/21
 * @Modified By:
 */
public class StationReportVO {
    // 综合厂用电量
    private Double allUserPower;
    // 综合厂用电率
    private Double allUserPowerRatio;
    // 网馈电量
    private Double buyPower;
    // 单电站返回时间的 list, h/day/moth
    private Long collectTime;
    // 等效利用小时
    private Double equivalentHour;
    // 收益
    private Double inCome;
    // 供电量
    private Double ongridPower;
    // 发电效率
    private Double pr;
    // 发电量
    private Double productPower;
    // 辐照量
    private Double radiationIntensity;
    // 自用电量
    private Double selfUsePower;
    // 自发自用率
    private Double selfUseRatio;
    // 电站编号
    private String stationCode;
    // 电站名称
    private String stationName;
    // 厂用电量
    private Double usePower;
    
    private Double theoryPower;

    public Double getAllUserPower() {
        return allUserPower;
    }

    public void setAllUserPower(Double allUserPower) {
        this.allUserPower = allUserPower;
    }

    public Double getAllUserPowerRatio() {
        return allUserPowerRatio;
    }

    public void setAllUserPowerRatio(Double allUserPowerRatio) {
        this.allUserPowerRatio = allUserPowerRatio;
    }

    public Double getBuyPower() {
        return buyPower;
    }

    public void setBuyPower(Double buyPower) {
        this.buyPower = buyPower;
    }

    public Long getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Long collectTime) {
        this.collectTime = collectTime;
    }

    public Double getEquivalentHour() {
        return equivalentHour;
    }

    public void setEquivalentHour(Double equivalentHour) {
        this.equivalentHour = equivalentHour;
    }

    public Double getInCome() {
        return inCome;
    }

    public void setInCome(Double inCome) {
        this.inCome = inCome;
    }

    public Double getOngridPower() {
        return ongridPower;
    }

    public void setOngridPower(Double ongridPower) {
        this.ongridPower = ongridPower;
    }

    public Double getPr() {
        return pr;
    }

    public void setPr(Double pr) {
        this.pr = pr;
    }

    public Double getProductPower() {
        return productPower;
    }

    public void setProductPower(Double productPower) {
        this.productPower = productPower;
    }

    public Double getRadiationIntensity() {
        return radiationIntensity;
    }

    public void setRadiationIntensity(Double radiationIntensity) {
        this.radiationIntensity = radiationIntensity;
    }

    public Double getSelfUsePower() {
        return selfUsePower;
    }

    public void setSelfUsePower(Double selfUsePower) {
        this.selfUsePower = selfUsePower;
    }

    public Double getSelfUseRatio() {
        return selfUseRatio;
    }

    public void setSelfUseRatio(Double selfUseRatio) {
        this.selfUseRatio = selfUseRatio;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Double getUsePower() {
        return usePower;
    }

    public void setUsePower(Double usePower) {
        this.usePower = usePower;
    }

	public Double getTheoryPower() {
		return theoryPower;
	}

	public void setTheoryPower(Double theoryPower) {
		this.theoryPower = theoryPower;
	}
    
    
}
