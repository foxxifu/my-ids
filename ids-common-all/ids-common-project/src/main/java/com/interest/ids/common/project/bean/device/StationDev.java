package com.interest.ids.common.project.bean.device;

import java.io.Serializable;

/**
 * 设备接入
 * @author xm
 *
 */
public class StationDev implements Serializable
{
    
    private static final long serialVersionUID = 9174186974503184712L;
    
    private String stationCode;
    private String esn;
    private String devName;
    private String devModelVersion;
    public String getStationCode() {
        return stationCode;
    }
    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }
    public String getEsn() {
        return esn;
    }
    public void setEsn(String esn) {
        this.esn = esn;
    }
    public String getDevName() {
        return devName;
    }
    public void setDevName(String devName) {
        this.devName = devName;
    }
    public String getDevModelVersion() {
        return devModelVersion;
    }
    public void setDevModelVersion(String devModelVersion) {
        this.devModelVersion = devModelVersion;
    }
    
}
