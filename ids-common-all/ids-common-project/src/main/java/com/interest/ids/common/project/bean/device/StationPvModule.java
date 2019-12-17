package com.interest.ids.common.project.bean.device;

import java.io.Serializable;
import java.util.List;
/**
 * 组串制造商
 * @author xm
 *
 */
public class StationPvModule implements Serializable 
{
    private static final long serialVersionUID = -1221824292951818069L;
    
    private Long id;
    private String manufacturer;//制造商
    private String moduleVersion;//组件型号
    private Double standardPower;//组件标称功率(Wp)
    private String abbreviation;//简称
    private String moduleType;//组件类型1:多晶 2:单晶 3:N型单晶 4:PERC单晶(单晶PERC) 5:单晶双玻 6:多晶双玻 7:单晶四栅60片 8:单晶四栅72片 9:多晶四栅60片 10:多晶四栅72片
    private Double moduleRatio; // 效率(%)
    private Double componentsNominalVoltage;//组件标称开路电压,Voc(V) [0~80]
    private Double nominalCurrentComponent;//组件标称短路电流,Isc(A)[0~20]
    private Double maxPowerPointVoltage;//组件最大功率点电压,Vm(V)[-1000~1000]
    private Double maxPowerPointCurrent;//组件最大功率点电流,Im(A)[-1000~1000]
    private Double fillFactor; //填充因子 FF(%)[65~85]
    private Double maxPowerTempCoef;//峰值功率温度系数(%)[-1~0]
    private Double voltageTempCoef;//组件电压温度系数 (%/oC)[-1~0]
    private Double currentTempCoef;//组件电流温度系数(%/oC)[0~0.2]
    private Double firstDegradationDrate;//组件首年衰减率(%)[0~100]
    private Double secondDegradationDrate;//组件逐年衰减率(%)[0~100]
    private Integer cellsNumPerModule; // 组件电池片数(片)[0~200]
    private Double minWorkTemp;//工作温度(oC) (最小值)
    private Double maxWorkTemp; //工作温度(oC) (最小值)
    private Long createTime; //创建时间
    private Long updateTime;//修改时间
    /**
     * 商家下面的型号
     * @return
     */
    private List<StationPvModule> moduleVersions;
    
    public List<StationPvModule> getModuleVersions() {
        return moduleVersions;
    }
    public void setModuleVersions(List<StationPvModule> moduleVersions) {
        this.moduleVersions = moduleVersions;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getManufacturer() {
        return manufacturer;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    public String getModuleVersion() {
        return moduleVersion;
    }
    public void setModuleVersion(String moduleVersion) {
        this.moduleVersion = moduleVersion;
    }
    public Double getStandardPower() {
        return standardPower;
    }
    public void setStandardPower(Double standardPower) {
        this.standardPower = standardPower;
    }
    public String getAbbreviation() {
        return abbreviation;
    }
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
    public String getModuleType() {
        return moduleType;
    }
    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }
    public Double getModuleRatio() {
        return moduleRatio;
    }
    public void setModuleRatio(Double moduleRatio) {
        this.moduleRatio = moduleRatio;
    }
    public Double getComponentsNominalVoltage() {
        return componentsNominalVoltage;
    }
    public void setComponentsNominalVoltage(Double componentsNominalVoltage) {
        this.componentsNominalVoltage = componentsNominalVoltage;
    }
    public Double getNominalCurrentComponent() {
        return nominalCurrentComponent;
    }
    public void setNominalCurrentComponent(Double nominalCurrentComponent) {
        this.nominalCurrentComponent = nominalCurrentComponent;
    }
    public Double getMaxPowerPointVoltage() {
        return maxPowerPointVoltage;
    }
    public void setMaxPowerPointVoltage(Double maxPowerPointVoltage) {
        this.maxPowerPointVoltage = maxPowerPointVoltage;
    }
    public Double getMaxPowerPointCurrent() {
        return maxPowerPointCurrent;
    }
    public void setMaxPowerPointCurrent(Double maxPowerPointCurrent) {
        this.maxPowerPointCurrent = maxPowerPointCurrent;
    }
    public Double getFillFactor() {
        return fillFactor;
    }
    public void setFillFactor(Double fillFactor) {
        this.fillFactor = fillFactor;
    }
    public Double getMaxPowerTempCoef() {
        return maxPowerTempCoef;
    }
    public void setMaxPowerTempCoef(Double maxPowerTempCoef) {
        this.maxPowerTempCoef = maxPowerTempCoef;
    }
    public Double getVoltageTempCoef() {
        return voltageTempCoef;
    }
    public void setVoltageTempCoef(Double voltageTempCoef) {
        this.voltageTempCoef = voltageTempCoef;
    }
    public Double getCurrentTempCoef() {
        return currentTempCoef;
    }
    public void setCurrentTempCoef(Double currentTempCoef) {
        this.currentTempCoef = currentTempCoef;
    }
    public Double getFirstDegradationDrate() {
        return firstDegradationDrate;
    }
    public void setFirstDegradationDrate(Double firstDegradationDrate) {
        this.firstDegradationDrate = firstDegradationDrate;
    }
    public Double getSecondDegradationDrate() {
        return secondDegradationDrate;
    }
    public void setSecondDegradationDrate(Double secondDegradationDrate) {
        this.secondDegradationDrate = secondDegradationDrate;
    }
    public Integer getCellsNumPerModule() {
        return cellsNumPerModule;
    }
    public void setCellsNumPerModule(Integer cellsNumPerModule) {
        this.cellsNumPerModule = cellsNumPerModule;
    }
    public Double getMinWorkTemp() {
        return minWorkTemp;
    }
    public void setMinWorkTemp(Double minWorkTemp) {
        this.minWorkTemp = minWorkTemp;
    }
    public Double getMaxWorkTemp() {
        return maxWorkTemp;
    }
    public void setMaxWorkTemp(Double maxWorkTemp) {
        this.maxWorkTemp = maxWorkTemp;
    }
    public Long getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
    public Long getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
   
}
