package com.interest.ids.common.project.ftp;

/**
 * 
 * @author lhq
 *
 *
 */
public class FtpServerInfo  {
	
	
	private String host;
	private Integer port;
	private String userName;
	private String password;
	private String homePath;
	public FtpServerInfo(){}
	public FtpServerInfo(String host, Integer port, String userName, String password,
			String homePath) {
		super();
		this.host = host;
		this.port = port;
		this.userName = userName;
		this.password = password;
		this.homePath = homePath;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

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

	public String getHomePath() {
		return homePath;
	}

	public void setHomePath(String homePath) {
		this.homePath = homePath;
	}

}