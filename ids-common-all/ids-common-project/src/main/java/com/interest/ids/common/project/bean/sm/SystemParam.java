package com.interest.ids.common.project.bean.sm;

import java.io.Serializable;

import javax.persistence.Transient;

public class SystemParam implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Long id; //主键id
	private String description; //系统描述
	private String fileId; //login文件的id
	private String systemName; // 系统名称
	
	@Transient
	private String version;//'系统版本号，目前暂不保存在数据库中，而是保存在version.properties文件中'
	@Transient
	private String installDate;//'安装时间，目前暂不保存在数据库中，而是保存在version.properties文件中'
	
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getInstallDate() {
		return installDate;
	}
	public void setInstallDate(String installDate) {
		this.installDate = installDate;
	}
	
}
