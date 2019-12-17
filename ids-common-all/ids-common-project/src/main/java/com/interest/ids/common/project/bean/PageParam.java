package com.interest.ids.common.project.bean;

public class PageParam {
    protected String sortType;
    protected String sortColumn;
    protected int page;
    protected int pageSize ;
    protected String sId;
    private String orderBy;
    private String sort;

    public String getsId() {
        return sId;
    }
    public void setsId(String sId) {
        this.sId = sId;
    }
    public String getSortType() {
        return sortType;
    }
    public void setSortType(String sortType) {
        this.sortType = sortType;
    }
    public String getSortColumn() {
        return sortColumn;
    }
    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }
    public int getPage() {
        return page;
    }
    public void setPage(int page) {
        this.page = page;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public static String getWhereSids(String sIdName,String sId){
        String result = "";
        String sIds = getSplitStr(sId);
        result = getCondition(" where "+sIdName+" in (",sIds);
        return result;
    }

    public static String getAndSids(String sIdName,String sId){
        String result = "";
        String sIds = getSplitStr(sId);
        result = getCondition(" and "+sIdName+" in (",sIds);
        return result;
    }

    public static String getCondition(String left,String sIds){
        String result = "";
        if(sIds!=null && sIds.length()>0){
            result = left + sIds +") ";
        }
        return result;
    }

    public static String getSplitStr(String str){
        String result = "";
        if(str!=null && str.length()>0){
            String[] states = str.split(",");
            for(int i=0;i<states.length;i++){
                String s = ",";
                if(i==states.length-1){
                    s = "";
                }
                result += "'"+states[i]+"'"+s;
            }
        }
        return result;
    }
    public String getOrderBy() {
        return orderBy;
    }
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
    public String getSort() {
        return sort;
    }
    public void setSort(String sort) {
        this.sort = sort;
    }
}
