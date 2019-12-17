package com.interest.ids.common.project.bean;

import java.io.Serializable;
import java.util.List;

public class PageData<T> implements Serializable

{
    private static final long serialVersionUID = -1004916467335366945L;

    private Long count;

    /**
     * 分页查询到的数据
     */
    private List<T> list;

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

}
