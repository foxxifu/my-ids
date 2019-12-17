package com.interest.ids.biz.web.operation.vo;

import java.io.Serializable;

public class OperationDeviceInfoVo implements Serializable {

    private static final long serialVersionUID = -3998286419839544396L;

    /**
     * 设备编码
     */
    private Long deviceId;
    
    /**
     * 设备类型
     */
    private Integer deviceTypeId;

    /**
     * 设备名称
     */
    private String devName;

    /**
     * 设备状态（1：通讯中断 2：故障 3：正常）
     */
    private int status;

    /**
     * 有功功率
     */
    private Double activePower;

    /**
     * 无功功率
     */
    private Double reactivePower;

    /**
     * 直流汇流箱平均电流
     */
    private Double photcI;

    /**
     * 直流汇流箱平均电压
     */
    private Double photcU;
    
    private Long parentId;

    /**
     * 设备故障处理状态（undo, doing 如果设备正常为null）
     */
    private String dealStatus;

    public OperationDeviceInfoVo() {

    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(Integer deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}

	public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Double getActivePower() {
        return activePower;
    }

    public void setActivePower(Double activePower) {
        this.activePower = activePower;
    }

    public Double getReactivePower() {
        return reactivePower;
    }

    public void setReactivePower(Double reactivePower) {
        this.reactivePower = reactivePower;
    }

    public Double getPhotcI() {
        return photcI;
    }

    public void setPhotcI(Double photcI) {
        this.photcI = photcI;
    }

    public Double getPhotcU() {
        return photcU;
    }

    public void setPhotcU(Double photcU) {
        this.photcU = photcU;
    }

    public String getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(String dealStatus) {
        this.dealStatus = dealStatus;
    }
    

    public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((deviceId == null) ? 0 : deviceId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OperationDeviceInfoVo other = (OperationDeviceInfoVo) obj;
        if (deviceId == null) {
            if (other.deviceId != null)
                return false;
        } else if (!deviceId.equals(other.deviceId))
            return false;
        return true;
    }

}
