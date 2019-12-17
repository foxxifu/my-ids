package com.interest.ids.common.project.bean.device;

public class DevicePvModuleDto {

    private Long devId;

    /**
     * 组串编号：pv1
     */
    private String pvCode;

    /**
     * 组串容量
     */
    private Double pvCapacity;

    /**
     * 组串组件数
     */
    private Integer unitNum;

    /**
     * 组件生产厂家
     */
    private String manufacturer;

    /**
     * 组件型号
     */
    private String moduleVersion;

    /**
     * 组件类型： 1:多晶 2:单晶 3:N型单晶 4:PERC单晶(单晶PERC) 5:单晶双玻 6:多晶双玻 7:单晶四栅60片 8:单晶四栅72片
     * 9:多晶四栅60片 10:多晶四栅72片
     */
    private String moduleType;

    public Long getDevId() {
        return devId;
    }

    public void setDevId(Long devId) {
        this.devId = devId;
    }

    public String getPvCode() {
        return pvCode;
    }

    public void setPvCode(String pvCode) {
        this.pvCode = pvCode;
    }

    public Double getPvCapacity() {
        return pvCapacity;
    }

    public void setPvCapacity(Double pvCapacity) {
        this.pvCapacity = pvCapacity;
    }

    public Integer getUnitNum() {
        return unitNum;
    }

    public void setUnitNum(Integer unitNum) {
        this.unitNum = unitNum;
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

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

}
