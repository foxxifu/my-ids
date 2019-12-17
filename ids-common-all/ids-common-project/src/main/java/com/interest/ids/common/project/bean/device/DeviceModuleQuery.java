package com.interest.ids.common.project.bean.device;

public class DeviceModuleQuery 
{
    private Integer deviceTypeId;
    private int start;
    private int pageSize;
    private int total;
    private int page;
    private String esn;
    
    public String getEsn() {
        return esn;
    }
    public void setEsn(String esn) {
        this.esn = esn;
    }
    public Integer getDeviceTypeId() {
        return deviceTypeId;
    }
    public void setDeviceTypeId(Integer deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }
    public int getStart() {
        return start;
    }
    public void setStart(int start) {
        this.start = start;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
    public int getPage() {
        return page;
    }
    public void setPage(int page) {
        this.page = page;
    }
    
}
