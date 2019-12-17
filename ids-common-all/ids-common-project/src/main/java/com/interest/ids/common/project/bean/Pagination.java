package com.interest.ids.common.project.bean;

/**
 * @Author: sunbjx
 * @Description: 此分页针对 mybatis mapper.xml
 * @Date: Created in 下午3:05 18-2-1
 * @Modified By:
 */
public class Pagination extends UserParams {

    // 当前页
    private int index = 1;
    // 页大小
    private int pageSize = 10;
    // 排序方式
    private boolean asc;
    // 排序字段
    private String oderBy;

    public int getStart() {
        return (index - 1) * pageSize;
    }

    public int getEnd() {
        return index * pageSize;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean getIsAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    public String getAsc() {
        return (asc) ? "ASC" : "DESC";
    }

    public String getOderBy() {
        return oderBy;
    }

    public void setOderBy(String oderBy) {
        this.oderBy = oderBy;
    }
}
