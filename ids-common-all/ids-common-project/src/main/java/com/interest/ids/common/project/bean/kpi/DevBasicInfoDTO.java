package com.interest.ids.common.project.bean.kpi;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.interest.ids.common.project.bean.device.PvCapacityM;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.constant.DispersionConstant;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * 设备基础信息封装：将设备所接组串信息合并到此类中
 */
public class DevBasicInfoDTO implements Serializable {

    private static final long serialVersionUID = 5038465011487271225L;

    // 业务编号
    private Long deviceId;
    // 电站ID
    private String stationCode;
    // 设备名称
    private String deviceName;
    // 设备类型名称
    private String devTypeName;
    // 设备类型ID
    private Integer devTypeId;
    // 设备组串容量：<组串编号，组串容量>
    private Map<Integer, Double> modulePVCap;
    // 设备总的装机容量
    private Double devCapacity;
    // 连接个数
    private int connPVCount;

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

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDevTypeName() {
        return devTypeName;
    }

    public void setDevTypeName(String devTypeName) {
        this.devTypeName = devTypeName;
    }

    public Integer getDevTypeId() {
        return devTypeId;
    }

    public void setDevTypeId(Integer devTypeId) {
        this.devTypeId = devTypeId;
    }

    public Map<Integer, Double> getModulePVCap() {
        return modulePVCap;
    }

    public void setModulePVCap(Map<Integer, Double> modulePVCap) {
        this.modulePVCap = modulePVCap;
    }

    public Double getDevCapacity() {
        return devCapacity;
    }

    public void setDevCapacity(Double devCapacity) {
        this.devCapacity = devCapacity;
    }

    public int getConnPVCount() {
        return connPVCount;
    }

    public void setConnPVCount(int connPVCount) {
        this.connPVCount = connPVCount;
    }

    public DevBasicInfoDTO(PvCapacityM devCap) throws IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        super();
        this.deviceId = devCap.getDeviceId();
        this.modulePVCap = new HashMap<Integer, Double>();
        this.devCapacity = 0d;
        for (int i = 1; i <= DispersionConstant.PV_COUNT; i++) {
            Double pvCap = (Long) PropertyUtils.getProperty(devCap, "PV" + i) / 1000d;
            if (pvCap != null && pvCap > 0) {
                this.modulePVCap.put(i, pvCap);
                devCapacity += pvCap;
            }

        }
        this.connPVCount = this.modulePVCap.size();
    }

    public DevBasicInfoDTO(DeviceInfo dev) {
        this.stationCode = dev.getStationCode();
        this.deviceName = dev.getDevName();
        this.devTypeId = dev.getDevTypeId();
        this.deviceId = dev.getId();
        // 初始化装机容容量
        this.devCapacity = 0d;
    }

    public void setPVCapacity(PvCapacityM devCap)
            throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        this.deviceId = devCap.getDeviceId();
        this.modulePVCap = new HashMap<Integer, Double>();
        for (int i = 1; i <= DispersionConstant.PV_COUNT; i++) {
            // 转kw
            Object pvCapObj = PropertyUtils.getProperty(devCap, "pv" + i);
            if(pvCapObj != null){
                Double pvCap = (Long)pvCapObj  / 1000d;
                if (pvCap != null && pvCap > 0) {
                    this.modulePVCap.put(i, pvCap);
                    devCapacity += pvCap;
                }
            }
        }
        this.connPVCount = this.modulePVCap.size();
    }
}
