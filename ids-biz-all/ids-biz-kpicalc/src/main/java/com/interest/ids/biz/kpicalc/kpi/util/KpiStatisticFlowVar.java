package com.interest.ids.biz.kpicalc.kpi.util;

import com.interest.ids.biz.kpicalc.kpi.calc.KpiStatistics;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class KpiStatisticFlowVar {

    public KpiStatisticFlowVar(Boolean statStatus) {
        this.statStatus = statStatus;
    }

    public KpiStatisticFlowVar() {
    }

    private KpiStatistics nextCalculator;

    private Map<String, Object> dataListPool = new HashMap<>();

    private TimeZone timeZone;

    private String redisKey;

    private Long startTime = System.currentTimeMillis();

    private Long endTime;

    //KPI计算状态：true 成功  false 失败
    private Boolean statStatus;

    private String statDevType;

    private Map<String, KpiCacheSet> cacheSettingMap = new HashMap<>();

    public KpiStatistics getNextCalculator() {
        return this.nextCalculator;
    }

    public void setNextCalculator(KpiStatistics nextCalculator) {
        this.nextCalculator = nextCalculator;
    }

    public Map<String, Object> getDataListPool() {
        return this.dataListPool;
    }

    public TimeZone getTimeZone() {
        return this.timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public Boolean getStatStatus() {
        return this.statStatus;
    }

    public void setStatStatus(Boolean statStatus) {
        this.statStatus = statStatus;
    }

    public String getStatDevType() {
        return this.statDevType;
    }

    public void setStatDevType(String statDevType) {
        this.statDevType = statDevType;
    }

    public Long getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getRedisKey() {
        return this.redisKey;
    }

    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
    }

    public KpiCacheSet getCacheSetting(String key) {
        return this.cacheSettingMap.get(key);
    }

    public KpiCacheSet getRandomCacheSetting() {
        for (Map.Entry<String, KpiCacheSet> entr : cacheSettingMap.entrySet()) {
            return entr.getValue();
        }
        return null;
    }

    public void setCacheSetting(String key, KpiCacheSet setting) {
        this.cacheSettingMap.put(key, setting);
    }

    @Override
    public String toString() {
        return "KpiStatisticFlowVar [timeZone=" + (this.timeZone != null ? timeZone.getID() : null) + ", redisKey="
                + this.redisKey + ", startTime=" + this.startTime + ", endTime=" + this.endTime + ", statStatus="
                + this.statStatus + ", statDevType=" + this.statDevType + "]";
    }
}
