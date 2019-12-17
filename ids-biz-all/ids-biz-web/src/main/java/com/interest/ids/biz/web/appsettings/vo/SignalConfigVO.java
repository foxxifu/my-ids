package com.interest.ids.biz.web.appsettings.vo;

import java.util.Date;

/**
 * @Author: sunbjx
 * @Description: 电表VO
 * @Date: Created in 下午5:12 18-1-19
 * @Modified By:
 */
public class SignalConfigVO {

    // id
    private Long id;
    // 点表名称
    private String signalDataName;
    // 版本号
    private String version;
    // 设备类型id
    private Integer deviceTypeId;
    // 导入日期
    private Date createDate;
    
    private String protocolCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSignalDataName() {
        return signalDataName;
    }

    public void setSignalDataName(String signalDataName) {
        this.signalDataName = signalDataName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(Integer deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

	public String getProtocolCode() {
		return protocolCode;
	}

	public void setProtocolCode(String protocolCode) {
		this.protocolCode = protocolCode;
	}
    
    
}
