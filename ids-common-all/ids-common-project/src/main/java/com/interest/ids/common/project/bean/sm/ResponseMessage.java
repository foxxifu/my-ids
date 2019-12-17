package com.interest.ids.common.project.bean.sm;

import java.io.Serializable;
import java.util.Arrays;


/**
 * 
 * @author lhq
 *
 */
public class ResponseMessage implements Serializable {
	
	
	private static final long serialVersionUID = 1545518814914151374L;
	private boolean success = true;
	private Object data = null;
	private long failCode;
	private String[] params;

	public ResponseMessage() {
		this.success = true;
	}
	
	public ResponseMessage(Object data) {
		this.success = false;
		this.data = data;
	} 

	public ResponseMessage(boolean b,long failCode) {
		this.success = b;
		this.failCode = failCode;
	}

	public ResponseMessage(Object data, String[] params) {
		this.success = false;
		this.data = data;
		this.params = params;
	}

	public ResponseMessage(boolean success, Object data){
		this.success = success;
		this.data = data;
	}

	public ResponseMessage(boolean success, Object data, String[] params) {
		this.success = success;
		this.data = data;
		this.params = params;
	}

	public String[] getParams() {
		return params;
	}

	public void setParams(String[] params) {
		this.params = params;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public long getFailCode() {
		return failCode;
	}

	public void setFailCode(long failCode) {
		this.failCode = failCode;
	}

	@Override
	public String toString() {
		return "ReplyInfo [success=" + success + ", data=" + data + ", params="
				+ Arrays.toString(params) + "]";
	}

	
}