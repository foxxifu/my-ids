package com.interest.ids.common.project.bean.sm;

import java.io.Serializable;

public class AuthorizeMResource implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long authId;
	private Long resourceId;
	
	public Long getAuthId() {
		return authId;
	}
	public void setAuthId(Long authId) {
		this.authId = authId;
	}
	public Long getResourceId() {
		return resourceId;
	}
	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}
	
}
