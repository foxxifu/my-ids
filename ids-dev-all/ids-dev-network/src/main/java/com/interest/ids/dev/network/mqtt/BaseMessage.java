package com.interest.ids.dev.network.mqtt;



import java.io.Serializable;
import java.util.Map;

import com.interest.ids.common.project.bean.alarm.AlarmModel;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.signal.SignalInfo;

/**
 * 保存解析结果
 * @author yaokun
 */
public class BaseMessage implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2569409033766261467L;
	//2byte帧长度
    private int length;
    //16byte签名
    private byte[] sign;
    //1byte协议版本
    private byte version;
    //16byte采集器设备 SN 号
    private String sn;
    //1byte数据类型(0x02:主动上传数据包类型，0x04:回复下行数据包类型)
    private byte dataType;
    //4byte时间戳
    private long timeStamp;
    //1字节信号强度
    private byte signPower;
    private DeviceInfo dev;

    public DeviceInfo getDev() {
		return dev;
	}

	public void setDev(DeviceInfo dev) {
		this.dev = dev;
	}

	public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getSign() {
        return sign;
    }

    public void setSign(byte[] sign) {
        this.sign = sign;
    }

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public byte getDataType() {
        return dataType;
    }

    public void setDataType(byte dataType) {
        this.dataType = dataType;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public byte getSignPower() {
        return signPower;
    }

    public void setSignPower(byte signPower) {
        this.signPower = signPower;
    }

    public Map<SignalInfo, String> getValueMap() {
        return valueMap;
    }

    public void setValueMap(Map<SignalInfo, String> valueMap) {
        this.valueMap = valueMap;
    }

    private Map<SignalInfo,String> valueMap;
    private Map<AlarmModel,Boolean> alarmMap;

	public Map<AlarmModel, Boolean> getAlarmMap() {
		return alarmMap;
	}

	public void setAlarmMap(Map<AlarmModel, Boolean> alarmMap) {
		this.alarmMap = alarmMap;
	}


}
