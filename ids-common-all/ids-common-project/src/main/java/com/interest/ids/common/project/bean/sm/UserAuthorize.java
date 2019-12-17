package com.interest.ids.common.project.bean.sm;

import java.io.Serializable;

public class UserAuthorize implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long userId;
	private Long authorizeId;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getAuthorizeId() {
		return authorizeId;
	}
	public void setAuthorizeId(Long authorizeId) {
		this.authorizeId = authorizeId;
	}
	
}
