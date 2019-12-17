package com.interest.ids.common.project.bean.sm;

import java.io.Serializable;

public class QueryStationInfo implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Page<?> page;
    private StationInfoM station;
    
    public Page<?> getPage() {
        return page;
    }
    public void setPage(Page<?> page) {
        this.page = page;
    }
    public StationInfoM getStation() {
        return station;
    }
    public void setStation(StationInfoM station) {
        this.station = station;
    }
    
}
