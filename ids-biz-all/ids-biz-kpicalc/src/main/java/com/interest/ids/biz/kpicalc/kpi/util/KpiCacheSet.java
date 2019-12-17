package com.interest.ids.biz.kpicalc.kpi.util;

import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class KpiCacheSet {

    /**
     * 缓存排序方式：升序
     */
    public static final int CACHE_RANGE_TYPE_ASC = 0;

    /**
     * 缓存排序方式：降序
     */
    public static final int CACHE_RANGE_TYPE_DESC = 1;

    /**
     * Redis 缓存key值分隔符':'
     */
    public static final String CACHE_KEY_SPLITER_COLON = ":";

    // 缓存前缀， 用于区分不同的缓存
    private String baseCacheKey;
    // 表名
    private String tableName;
    // 采集时间格式
    private SimpleDateFormat collectTimeFmt;
    // 统计时间格式
    private SimpleDateFormat statTimeFmt;

    public KpiCacheSet() { }

    public KpiCacheSet(String baseCacheKey, String tableName, String collectTimeFmtStr, String statTimeFmtStr,
                       TimeZone timeZone) {
        if(null == timeZone){
            timeZone = TimeZone.getDefault();
        }
        this.baseCacheKey = baseCacheKey;
        this.tableName = tableName;

        if(StringUtils.isNotEmpty(collectTimeFmtStr)){
            this.collectTimeFmt = new SimpleDateFormat(collectTimeFmtStr);
            this.collectTimeFmt.setTimeZone(timeZone);
        }
        if(StringUtils.isNotEmpty(statTimeFmtStr)){
            this.statTimeFmt = new SimpleDateFormat(statTimeFmtStr);
            this.statTimeFmt.setTimeZone(timeZone);
        }
    }

    public String getBaseKeyFmt(String sId, String busiCode, Long time, String lastStr) {
        return getBaseKeyWithCondition(sId, busiCode, time, true, lastStr);
    }

    public String getBaseKeyWithCondition(String sId, String deviceId, Long time, boolean needFmt, String lastStr) {
        StringBuffer sb = new StringBuffer(this.getBaseCacheKey());

        if(StringUtils.isNotEmpty(sId)){
            sb.append(CACHE_KEY_SPLITER_COLON).append(sId);
        }
        if(StringUtils.isNotEmpty(deviceId)){
            sb.append(CACHE_KEY_SPLITER_COLON).append(deviceId);
        }
        if(null != time){
            if(needFmt){
                sb.append(CACHE_KEY_SPLITER_COLON).append(statTimeFmt.format(time));
            }else {
                sb.append(CACHE_KEY_SPLITER_COLON).append(time);
            }
        }
        if(StringUtils.isNotEmpty(lastStr)){
            sb.append(lastStr);
        }
        return sb.toString();
    }

    // 构造缓存key，用于存/取统计的电站序列
    public String getStatStationKey() {
        return baseCacheKey + "-" + "STATISTICSTATION";
    }

    // 构造缓存key，用于存/取统计维度对应数据，如：设备
    public String getStatisticDimCacheKey() {
        return baseCacheKey + "-" + "STATISTICDIM";
    }

    public SimpleDateFormat getCollectTimeFmt() {
        return collectTimeFmt;
    }

    public void setCollectTimeFmt(SimpleDateFormat collectTimeFmt) {
        this.collectTimeFmt = collectTimeFmt;
    }

    public SimpleDateFormat getStatTimeFmt() {
        return statTimeFmt;
    }

    public void setStatTimeFmt(SimpleDateFormat statTimeFmt) {
        this.statTimeFmt = statTimeFmt;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getBaseCacheKey() {
        return baseCacheKey;
    }

    public void setBaseCacheKey(String baseCacheKey) {
        this.baseCacheKey = baseCacheKey;
    }
}
