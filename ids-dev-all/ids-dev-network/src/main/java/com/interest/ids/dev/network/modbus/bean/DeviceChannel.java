package com.interest.ids.dev.network.modbus.bean;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author lhq
 * 
 * 
 */
public class DeviceChannel {

	private Channel channel;
	// 初始化状态：1 未初始化； 2 初始化中； 3 初始化完成
	private int initState;
	// 采集器类型，
	private DcType dcType;
	//snCode
	private String snCode;
	
	private long joinTime;
	
	private int devNum;

	private Map<Integer,CommunicateTaskBean>  communicateTaskBeanMap;


	private boolean isRc4;

	
	


	public DeviceChannel(Channel channel) {
		this.channel = channel;
		this.joinTime = System.currentTimeMillis();
		this.initState = 1;
	}
	
	
	public long getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(long joinTime) {
		this.joinTime = joinTime;
	}

	public String getSnCode() {
		return snCode;
	}

	public void setSnCode(String snCode) {
		this.snCode = snCode;
	}

	

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public int getInitState() {
		return initState;
	}

	public void setInitState(int initState) {
		this.initState = initState;
	}

	public DcType getDcType() {
		return dcType;
	}

	public void setDcType(DcType dcType) {
		this.dcType = dcType;
	}
	

	public boolean isinitComplete(){
		return initState == 3;
	}
	
	public int getDevNum() {
		return devNum;
	}


	public void setDevNum(int devNum) {
		this.devNum = devNum;
	}

	public Map<Integer, CommunicateTaskBean> getCommunicateTaskBeanMap() {
		return communicateTaskBeanMap;
	}

	public boolean isRc4() {
		return isRc4;
	}

	public void setRc4(boolean rc4) {
		isRc4 = rc4;
	}



	public void setCommunicateTaskBeanMap(Map<Integer, CommunicateTaskBean> communicateTaskBeanMap) {
		this.communicateTaskBeanMap = communicateTaskBeanMap;
	}

	public void addCommunicateTaskBean(CommunicateTaskBean communicateTaskBean){
		if(communicateTaskBeanMap==null){
			communicateTaskBeanMap=new HashMap<>();
		}
		communicateTaskBeanMap.put(communicateTaskBean.getCommunicateNo(),communicateTaskBean);
	}

	public int getHead(){
		if(isRc4){
			return
					0x4000;
		}else{
			return 0;
		}
	}

	public  int addDev() {
		synchronized (this) {
			devNum++;
			return devNum;
		}
	}


	public enum DcType {
		// 采集棒，带网闸数采，不带网闸的数采
		DC_ROD, DC_GATEKEEPER, DC_NO_GATE_KEEPER;
	}

}
