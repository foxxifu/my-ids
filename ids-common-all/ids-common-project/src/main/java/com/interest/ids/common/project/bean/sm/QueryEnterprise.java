package com.interest.ids.common.project.bean.sm;

import java.io.Serializable;

public class QueryEnterprise implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Page<?> page;
    private EnterpriseInfo enterprise;
    private Long userId;
    private String type_;
  
    public Page<?> getPage() {
        return page;
    }
    public void setPage(Page<?> page) {
        this.page = page;
    }
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
	public EnterpriseInfo getEnterprise() {
        return enterprise;
    }
    public void setEnterprise(EnterpriseInfo enterprise) {
        this.enterprise = enterprise;
    }
    
}
