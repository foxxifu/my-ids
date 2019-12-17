package com.interest.ids.common.project.bean.alarm;

import java.io.Serializable;

import com.interest.ids.common.project.bean.sm.Page;

public class QueryWorkFlowDefect implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Page page;
    private WorkFlowDefectDto defect;
    
    public Page getPage() {
        return page;
    }
    public void setPage(Page page) {
        this.page = page;
    }
    public WorkFlowDefectDto getDefect() {
        return defect;
    }
    public void setDefect(WorkFlowDefectDto defect) {
        this.defect = defect;
    }
}
