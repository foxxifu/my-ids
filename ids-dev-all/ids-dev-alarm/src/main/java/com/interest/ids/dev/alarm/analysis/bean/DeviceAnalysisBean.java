package com.interest.ids.dev.alarm.analysis.bean;

import io.netty.util.collection.IntObjectHashMap;
import com.interest.ids.common.project.bean.device.StationDevicePvModule;
/**
 * 
 * @author lhq
 * 
 *
 */
	
public class DeviceAnalysisBean {
	 //设备id
	 private long devId;
	 //组串容量配置
	 private IntObjectHashMap<Double> pvCap;
	 //总的容量
	 private Double totalCapacity = 0.0; 
	 //组串个数
	 private int pvSize;
	 
	 private String devName;
	 
	 public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}

	public DeviceAnalysisBean(long devId){
		 this.devId = devId;
	 }
	 
	 public void addPv(StationDevicePvModule pv){
		 if(pvCap == null){
			 pvCap = new IntObjectHashMap<Double>();
		 }
		 if(pv.getFixedPower() != null && pv.getFixedPower() > 0d){
			 totalCapacity += pv.getFixedPower();
			 this.pvSize ++;
			 pvCap.put(pv.getPvIndex(), pv.getFixedPower());
		 }
	 }
	 
	 public long getDevId() {
		return devId;
	 }
	 
	 public void setDevId(long devId) {
		 this.devId = devId;
	 }
	 
	 public IntObjectHashMap<Double> getPvCap() {
		 return pvCap;
	 }
	 
	 public void setPvCap(IntObjectHashMap<Double> pvCap) {
		this.pvCap = pvCap;
	 }
	 
	 public Double getTotalCapacity() {
		return totalCapacity;
	 }
	 
	 public void setTotalCapacity(Double totalCapacity) {
		this.totalCapacity = totalCapacity;
	 }
	 
	 public int getPvSize() {
		return pvSize;
	 }
	 
	 public void setPvSize(int pvSize) {
		this.pvSize = pvSize;
	 }
}
