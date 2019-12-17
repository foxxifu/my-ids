package com.interest.ids.common.project.bean.device;

import java.io.Serializable;
import java.util.List;

public class DevicePvCapacity implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long deviceId;
    private String stationCode;
    private Long enterpriseId;
    private String devName;
    private Integer devTypeId;
    private List<Double> pvs;
    /**
     * 组串的id
     */
    private String ids;
    /**
     * 组串的个数
     */
    private Integer num;
    
    public String getIds() {
        return ids;
    }
    public void setIds(String ids) {
        this.ids = ids;
    }
    public Integer getNum() {
        return num;
    }
    public void setNum(Integer num) {
        this.num = num;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }
    public String getStationCode() {
        return stationCode;
    }
    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }
    public Long getEnterpriseId() {
        return enterpriseId;
    }
    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }
    
    public String getDevName() {
		return devName;
	}
	public void setDevName(String devName) {
		this.devName = devName;
	}
	public Integer getDevTypeId() {
		return devTypeId;
	}
	public void setDevTypeId(Integer devTypeId) {
		this.devTypeId = devTypeId;
	}
	public List<Double> getPvs() {
        return pvs;
    }
    public void setPvs(List<Double> pvs) {
        this.pvs = pvs;
    }
}
