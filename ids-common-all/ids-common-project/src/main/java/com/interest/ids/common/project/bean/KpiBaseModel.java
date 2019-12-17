package com.interest.ids.common.project.bean;

import java.io.Serializable;

public class KpiBaseModel implements Serializable{

    private static final long serialVersionUID = -7159559807557106210L;

    private Long collectTime;

    private String stationCode;

    private Long deviceId;

    private Long enterpriseId;

    public Long getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Long collectTime) {
        this.collectTime = collectTime;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode == null ? null : stationCode.trim();
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    @Override
    public int hashCode() {
    	int hashCode = 0;
    	if(this.getDeviceId() == null){
    		hashCode = this.getCollectTime().hashCode() + this.getStationCode().hashCode();
    	}else{
            hashCode = this.getCollectTime().hashCode() + this.getStationCode().hashCode()
                    + this.getDeviceId().hashCode();
    	}
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }

        if(obj == null) {
            return false;
        }

        if(obj instanceof KpiBaseModel) {
            KpiBaseModel bean = (KpiBaseModel) obj;
            if(this.getCollectTime() != null && this.getDeviceId() != null &&
                    this.getStationCode() != null && bean.getDeviceId() != null &&
                    bean.getCollectTime() != null && bean.getStationCode() != null){
                if (this.getCollectTime().intValue() == bean.getCollectTime().intValue() &&
                        this.getStationCode().equals(bean.getStationCode()) &&
                        this.getDeviceId().intValue() == bean.getDeviceId().intValue()) {
                    return true;
                }
            }
        }
        return false;
    }
}
