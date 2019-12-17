package com.interest.ids.common.project.ftp;



/**
 * 
 * @author lhq
 *
 */
public class FtpUserInfo {
	
	private String host;
	
	private int port; 
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * ftp路径
	 */
	private String ftpPath="";
	
	/**
	 * 是否可读
	 */
	private boolean write = true;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFtpPath() {
		return ftpPath;
	}

	public void setFtpPath(String ftpPath) {
		this.ftpPath = ftpPath;
	}

	public boolean isWrite() {
		return write;
	}

	public void setWrite(boolean write) {
		this.write = write;
	}

	@Override
	public String toString() {
		return "FtpUserInfo [userName=" + userName + ", password=" + password
				+ ", ftpPath=" + ftpPath + ", write=" + write + "]";
	}
}
