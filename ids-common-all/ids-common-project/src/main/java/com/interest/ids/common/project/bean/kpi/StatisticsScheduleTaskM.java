package com.interest.ids.common.project.bean.kpi;

import javax.persistence.Table;
import java.io.Serializable;

@Table(name="ids_statistics_schedule_task_t")
public class StatisticsScheduleTaskM implements Serializable{

    private static final long serialVersionUID = 6709930611985111908L;

    private String stationCode;
    private String busiType;
    private Long statDate;
    private Integer statType;
    private Long createTime;
    private Integer dealState;

    public StatisticsScheduleTaskM(String stationCode, String busiType, Long statDate) {
        this.stationCode = stationCode;
        this.busiType = busiType;
        this.statDate = statDate;
    }

    public StatisticsScheduleTaskM() {
    }


    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public String getBusiType() {
        return busiType;
    }

    public void setBusiType(String busiType) {
        this.busiType = busiType;
    }

    public Long getStatDate() {
        return statDate;
    }

    public void setStatDate(Long statDate) {
        this.statDate = statDate;
    }

    public Integer getStatType() {
        return statType;
    }

    public void setStatType(Integer statType) {
        this.statType = statType;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getDealState() {
        return dealState;
    }

    public void setDealState(Integer dealState) {
        this.dealState = dealState;
    }
}
