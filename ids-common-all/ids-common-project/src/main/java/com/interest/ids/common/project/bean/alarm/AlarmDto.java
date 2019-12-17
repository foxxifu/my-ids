package com.interest.ids.common.project.bean.alarm;



import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.interest.ids.common.project.bean.alarm.AlarmConstant.AlarmStatus;
import com.interest.ids.common.project.bean.signal.DeviceInfo;





public class AlarmDto {
    
    
    private DeviceInfo dev;
    
    private AlarmStatus status;
    //触发时间，活动告警时为产生时间，恢复告警为恢复时间
    private Long occurDate;
    //一般告警模式，华为 Modbus 告警， 设备中断告警
    private Long alarmId;
    
    private Long causeId;
    
    private Long seqNum;
    //第三方 Bit 位告警模式
    private Long sigAddress;
    
    private Integer bitIndex;
    //第三方 遥测告警模式
    private Map<Long, Integer> alarmFlagMap;
    
    private Map<Long, Integer> alarmDetailMap;
    /**
     * 告警的设备编号
     */
    private String stationCode;
    
    
    
    public AlarmDto() {
        super();
    }

    /**
     * HwModbus 告警构造方法
     * @param alarmSource
     * @param dev
     * @param status
     * @param occurDate
     * @param alarmId
     * @param causeId
     */
    public AlarmDto(DeviceInfo dev, AlarmStatus status, Long occurDate,
            Long alarmId, Long causeId) {
        super();
        this.dev = dev;
        this.status = status;
        this.occurDate = occurDate;
        this.alarmId = alarmId;
        this.causeId = causeId;
        this.stationCode = dev.getStationCode();
    }
    
    /**
     * 品联数采下挂设备告警构造方法
     * @param alarmSource
     * @param dev
     * @param status
     * @param occurDate
     * @param sigAddress
     * @param bitIndex
     */
    public AlarmDto(DeviceInfo dev, AlarmStatus status, Long occurDate,
            Long sigAddress, Integer bitIndex) {
        super();
        this.dev = dev;
        this.status = status;
        this.occurDate = occurDate;
        this.sigAddress = sigAddress;
        this.bitIndex = bitIndex;
        this.stationCode = dev.getStationCode();
    }
    
    /**
     * 第三方遥测告警构造方法
     * @param alarmSource
     * @param dev
     * @param occurDate
     * @param alarmFlagMap
     * @param alarmDetailMap
     */
    public AlarmDto(DeviceInfo dev, Long occurDate,
            Map<Long, Integer> alarmFlagMap, Map<Long, Integer> alarmDetailMap) {
        super();
        this.dev = dev;
        this.occurDate = occurDate;
        this.alarmFlagMap = alarmFlagMap;
        this.alarmDetailMap = alarmDetailMap;
        this.stationCode = dev.getStationCode();
    }

    public Long getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(Long seqNum) {
        this.seqNum = seqNum;
    }


    public DeviceInfo getDev() {
        return dev;
    }

    public void setDev(DeviceInfo dev) {
        this.dev = dev;
    }

    public AlarmStatus getStatus() {
        return status;
    }

    public void setStatus(AlarmStatus status) {
        this.status = status;
    }

    public Long getOccurDate() {
        return occurDate;
    }

    public void setOccurDate(Long occurDate) {
        this.occurDate = occurDate;
    }

    public Long getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(Long alarmId) {
        this.alarmId = alarmId;
    }

    public Long getCauseId() {
        return causeId;
    }

    public void setCauseId(Long causeId) {
        this.causeId = causeId;
    }

    public Long getSigAddress() {
        return sigAddress;
    }

    public void setSigAddress(Long sigAddress) {
        this.sigAddress = sigAddress;
    }

    public Integer getBitIndex() {
        return bitIndex;
    }

    public void setBitIndex(Integer bitIndex) {
        this.bitIndex = bitIndex;
    }

    public Map<Long, Integer> getAlarmFlagMap() {
        return alarmFlagMap;
    }

    public void setAlarmFlagMap(Map<Long, Integer> alarmFlagMap) {
        this.alarmFlagMap = alarmFlagMap;
    }

    public Map<Long, Integer> getAlarmDetailMap() {
        return alarmDetailMap;
    }

    public void setAlarmDetailMap(Map<Long, Integer> alarmDetailMap) {
        this.alarmDetailMap = alarmDetailMap;
    }

    public String getStationCode() {
		return stationCode;
	}
	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}
	@Override
    public String toString() {
        final int maxLen = 10;
        return "AlarmTransFormEntity ["  + ", dev=" + dev + ", status=" + status
                + ", occurDate=" + occurDate + ", alarmId=" + alarmId + ", causeId=" + causeId + ", seqNum=" + seqNum
                + ", sigAddress=" + sigAddress + ", bitIndex=" + bitIndex + ", alarmFlagMap="
                + (alarmFlagMap != null ? toString(alarmFlagMap.entrySet(), maxLen) : null) + ", alarmDetailMap="
                + (alarmDetailMap != null ? toString(alarmDetailMap.entrySet(), maxLen) : null) + "]";
    }

    private String toString(Collection<?> collection, int maxLen) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        int i = 0;
        for (Iterator<?> iterator = collection.iterator(); iterator.hasNext() && i < maxLen; i++){
            if(i > 0)
                builder.append(", ");
            builder.append(iterator.next());
        }
        builder.append("]");
        return builder.toString();
    }

}
