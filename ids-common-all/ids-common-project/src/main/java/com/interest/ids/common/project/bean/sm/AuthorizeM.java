package com.interest.ids.common.project.bean.sm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AuthorizeM implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//鏉冮檺鍜岃祫婧愮殑涓�瀵瑰鍏宠仈鍏崇郴
	private List<ResourceM> resoureceMs = new ArrayList<ResourceM>();
	
	public List<ResourceM> getResoureceMs() {
		return resoureceMs;
	}
	public void setResoureceMs(List<ResourceM> resoureceMs) {
		this.resoureceMs = resoureceMs;
	}
	
	private Long id;
	private String auth_name;
	private String description;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAuth_name() {
		return auth_name;
	}
	public void setAuth_name(String auth_name) {
		this.auth_name = auth_name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
