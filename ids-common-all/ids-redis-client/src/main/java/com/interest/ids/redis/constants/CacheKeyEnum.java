package com.interest.ids.redis.constants;

public enum CacheKeyEnum {

    STATION("station"),
    REALTIMEKPI("realtimekpi"),
    DEVICE("device"),
    SIGNAL("signal"),
    DEVICEINFO("deviceInfo"),
    TEMPKEY("tempKey:");

    private String cacheKey;

    public String getCacheKey() {
        return cacheKey;
    }

    CacheKeyEnum(String cacheKey){
        this.cacheKey = cacheKey;
    }
}
