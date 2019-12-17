package com.interest.ids.common.project.bean.sm;

import java.io.Serializable;
import java.util.List;

public class Page<T> implements Serializable

{
    private static final long serialVersionUID = -1004916467335366945L;

    /**
     * 默认每页显示条数
     */
    public static final int DEFAULT_PAGE_SIZE = 10;
    /**
     * 默认起始页
     */
    public static final int DEFAULT_PAGE_INDEX = 1;

    private int start;
    private int pageSize;
    private int count;

    /**
     * 分页查询到的数据
     */
    private List<T> list;
    private Integer index; // 当前页
    private Integer allSize; // 总分页数

    public Integer getAllSize() {
        return allSize;
    }

    public void setAllSize(Integer allSize) {
        this.allSize = allSize;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

}
