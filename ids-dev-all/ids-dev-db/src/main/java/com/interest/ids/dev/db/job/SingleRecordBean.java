package com.interest.ids.dev.db.job;

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
	
	private long  dataTime;
	
	private String tableName;
	
	public SingleRecordBean(DeviceInfo dev,String tableName,long  dataTime) {
		super();
		this.dev = dev;
		this.tableName = tableName;
		this.dataTime = dataTime;
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



	private Map<String,Object> map;

	public DeviceInfo getDev() {
		return dev;
	}

	public void setDev(DeviceInfo dev) {
		this.dev = dev;
	}

	public long getDataTime() {
		return dataTime;
	}

	public void setDataTime(long dataTime) {
		this.dataTime = dataTime;
	}

	public Map<String,Object> getMap() {
		return map;
	}

	public void setMap(Map<String,Object> map) {
		this.map = map;
	}
	
}
