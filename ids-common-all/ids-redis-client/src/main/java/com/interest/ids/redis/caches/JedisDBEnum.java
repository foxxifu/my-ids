package com.interest.ids.redis.caches;

public enum JedisDBEnum {
    //db0 db1
    DEFAULT(0L),SM(1L),KPI_STAT(2L),DEV_INFO(3L);

    private Long index;

    JedisDBEnum(Long index){
        this.index = index;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }
}
