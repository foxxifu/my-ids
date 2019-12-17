package com.interest.ids.common.project.bean.sm;

import java.io.Serializable;
import java.util.Date;

public class QueryRole implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Page<?> page;
    private RoleInfo role;
    private Date startTime;
    private Date endTime;
    private Long userId;
    private String type_;
    
    public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getType_() {
		return type_;
	}
	public void setType_(String type_) {
		this.type_ = type_;
	}
	public Date getStartTime() {
        return startTime;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public Date getEndTime() {
        return endTime;
    }
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    public Page<?> getPage() {
        return page;
    }
    public void setPage(Page<?> page) {
        this.page = page;
    }
    public RoleInfo getRole() {
        return role;
    }
    public void setRole(RoleInfo role) {
        this.role = role;
    }
    
}
