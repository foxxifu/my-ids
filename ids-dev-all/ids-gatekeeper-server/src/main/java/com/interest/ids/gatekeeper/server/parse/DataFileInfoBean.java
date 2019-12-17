package com.interest.ids.gatekeeper.server.parse;

/**
 * 
 * @author lhq
 *
 *
 */
public class DataFileInfoBean {

	private String fileName;

	private int dataType;

	private int sessionId;

	public DataFileInfoBean(String fileName, int dataType, int sessionId) {
		super();
		this.fileName = fileName;
		this.dataType = dataType;
		this.sessionId = sessionId;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public int getSessionId() {
		return sessionId;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
