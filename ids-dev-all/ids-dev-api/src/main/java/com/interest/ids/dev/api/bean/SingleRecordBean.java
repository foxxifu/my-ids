package com.interest.ids.dev.api.bean;

import java.util.HashMap;
import java.util.Map;

import com.interest.ids.common.project.bean.signal.DeviceInfo;


/**
 * 
 * @author lhq
 *
 *
 */
public class SingleRecordBean {
	
	private DeviceInfo dev;
	
	private Long  dataTime;
	
	private String tableName;
	
	private long collectTime;
	
	public SingleRecordBean(DeviceInfo dev,String tableName,long  dataTime,long collectTime) {
		super();
		this.dev = dev;
		this.tableName = tableName;
		this.dataTime = dataTime;
		this.collectTime = collectTime;
	}
	
	
	
	public SingleRecordBean(DeviceInfo dev,String tableName) {
		super();
		this.dev = dev;
		this.tableName = tableName;
	}
	
	public SingleRecordBean(){
		
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public long getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(long collectTime) {
		this.collectTime = collectTime;
	}



	private Map<String,Object> map = new HashMap<String,Object>(32);

	public DeviceInfo getDev() {
		return dev;
	}

	public void setDev(DeviceInfo dev) {
		this.dev = dev;
	}

	public Long getDataTime() {
		return dataTime;
	}

	public void setDataTime(Long dataTime) {
		this.dataTime = dataTime;
	}

	public Map<String,Object> getMap() {
		return map;
	}

	public void setMap(Map<String,Object> map) {
		this.map = map;
	}
	
}
