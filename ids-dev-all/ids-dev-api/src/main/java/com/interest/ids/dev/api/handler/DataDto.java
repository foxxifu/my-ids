package com.interest.ids.dev.api.handler;

import java.util.Map;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.dev.api.handler.BizEventHandler.DataMsgType;

/**
 * 
 * @author lhq
 *
 *
 */
public class DataDto implements java.io.Serializable{
	
	private static final long serialVersionUID = -1844111434175548011L;

	private DataMsgType msgType;
	
	private Object msg;
	
	private String snCode;
	
	private DeviceInfo dev;
	
	private Integer secondAddr;

	private Map<String,Object> attachMent;
	
	public void setDev(DeviceInfo dev) {
		this.dev = dev;
	}
	
	public DataDto(DataMsgType msgType,DeviceInfo dev, Object msg ) {
		super();
		this.msgType = msgType;
		this.dev = dev;
		this.msg = msg;
		
	}
	
	public DataDto(DataMsgType msgType,String snCode, Object msg ) {
		super();
		this.msgType = msgType;
		this.snCode = snCode;
		this.msg = msg;
		
	}
	
	
	
	public DataDto(DataMsgType msgType, Object msg, String snCode,
			Integer secondAddr) {
		super();
		this.msgType = msgType;
		this.msg = msg;
		this.snCode = snCode;
		this.secondAddr = secondAddr;
	}

	public DeviceInfo getDev() {
		return dev;
	}

	
	public Integer getSecondAddr() {
		return secondAddr;
	}

	public void setSecondAddr(Integer secondAddr) {
		this.secondAddr = secondAddr;
	}

	public String getSnCode() {
		return snCode;
	}

	public void setSnCode(String snCode) {
		this.snCode = snCode;
	}

	public DataMsgType getMsgType() {
		return msgType;
	}

	public void setMsgType(DataMsgType msgType) {
		this.msgType = msgType;
	}

	public Object getMsg() {
		return msg;
	}

	public void setMsg(Object msg) {
		this.msg = msg;
	}


	public Map<String, Object> getAttachMent() {
		return attachMent;
	}

	public void setAttachMent(Map<String, Object> attachMent) {
		this.attachMent = attachMent;
	}
}
