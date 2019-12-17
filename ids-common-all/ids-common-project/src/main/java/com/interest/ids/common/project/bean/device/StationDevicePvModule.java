package com.interest.ids.common.project.bean.device;

import java.io.Serializable;

/**
 * 设备组串
 * @author sunbangxiong
 *
 */
public class StationDevicePvModule implements Serializable 
{
    private static final long serialVersionUID = 7823673554551523865L;
    
    private Long devId;
    private String stationCode;
    private Double fixedPower;//组串容量(Wp)
    private String snCode; // 设备esn号
    private Integer pvIndex;//pv下标，如1,2,..20
    private Long pvModuleId; //组件信息编号，关联组件信息
    private Integer modulesNumPerString;//组串组件串联数量(块)[0~50]
    private Long moduleProductionDate;//组件投产日期
    private Long createTime;//创建时间
    private Long updateTime;//修改时间
    private String isDefault; //是否是预置
    public Long getDevId() {
        return devId;
    }
    public void setDevId(Long devId) {
        this.devId = devId;
    }
    public String getStationCode() {
        return stationCode;
    }
    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }
    public Double getFixedPower() {
        return fixedPower;
    }
    public void setFixedPower(Double fixedPower) {
        this.fixedPower = fixedPower;
    }
    public String getSnCode() {
		return snCode;
	}
	public void setSnCode(String snCode) {
		this.snCode = snCode;
	}
	public Integer getPvIndex() {
        return pvIndex;
    }
    public void setPvIndex(Integer pvIndex) {
        this.pvIndex = pvIndex;
    }
    public Long getPvModuleId() {
        return pvModuleId;
    }
    public void setPvModuleId(Long pvModuleId) {
        this.pvModuleId = pvModuleId;
    }
    public Integer getModulesNumPerString() {
        return modulesNumPerString;
    }
    public void setModulesNumPerString(Integer modulesNumPerString) {
        this.modulesNumPerString = modulesNumPerString;
    }
    public String getIsDefault() {
        return isDefault;
    }
    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
    public Long getModuleProductionDate() {
        return moduleProductionDate;
    }
    public void setModuleProductionDate(Long moduleProductionDate) {
        this.moduleProductionDate = moduleProductionDate;
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
