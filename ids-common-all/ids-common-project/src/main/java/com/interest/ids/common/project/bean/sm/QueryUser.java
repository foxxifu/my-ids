package com.interest.ids.common.project.bean.sm;

import java.io.Serializable;

public class QueryUser implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Page<?> page;
    private UserInfo user;
    private Long departmentId;
    
    public Long getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
	public Page<?> getPage() {
        return page;
    }
    public void setPage(Page<?> page) {
        this.page = page;
    }
    public UserInfo getUser() {
        return user;
    }
    public void setUser(UserInfo user) {
        this.user = user;
    }
    
}
