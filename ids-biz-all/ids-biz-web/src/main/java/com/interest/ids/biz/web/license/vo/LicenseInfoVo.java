package com.interest.ids.biz.web.license.vo;

import java.io.Serializable;

public class LicenseInfoVo implements Serializable {

    private static final long serialVersionUID = -465191529019934361L;

    /**
     * 导入时间
     */
    private Long importDate;
    /**
     * 导入用户
     */
    private String importUser;
    /**
     * 可接入逆变器数
     */
    private Integer totalDevNum;
    /**
     * 已接入逆变器数
     */
    private Integer installedDevNum;
    /**
     * 可接入总组件容量
     */
    private Double totalCapacity;
    /**
     * 已接入组件容量
     */
    private Double installedCapacity;

    public Long getImportDate() {
        return importDate;
    }

    public void setImportDate(Long importDate) {
        this.importDate = importDate;
    }

    public String getImportUser() {
        return importUser;
    }

    public void setImportUser(String importUser) {
        this.importUser = importUser;
    }

    public Integer getTotalDevNum() {
        return totalDevNum;
    }

    public void setTotalDevNum(Integer totalDevNum) {
        this.totalDevNum = totalDevNum;
    }

    public Integer getInstalledDevNum() {
        return installedDevNum;
    }

    public void setInstalledDevNum(Integer installedDevNum) {
        this.installedDevNum = installedDevNum;
    }

    public Double getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(Double totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public Double getInstalledCapacity() {
        return installedCapacity;
    }

    public void setInstalledCapacity(Double installedCapacity) {
        this.installedCapacity = installedCapacity;
    }

    @Override
    public String toString() {
        return "LicenseInfoVo [importDate=" + importDate + ", importUser=" + importUser + ", totalDevNum="
                + totalDevNum + ", installedDevNum=" + installedDevNum + ", totalCapacity=" + totalCapacity
                + ", installedCapacity=" + installedCapacity + "]";
    }

}
