package com.interest.ids.biz.kpicalc.kpi.service.cache;

import com.interest.ids.biz.kpicalc.kpi.util.KpiCacheSet;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IKpiCacheService {

    /**
     * 将给定时间范围内的性能数据存入缓存中
     * @param sTime
     * @param eTime
     * @param list
     * @param setting
     */
    void loadDbDataToCache(Long sTime, Long eTime, List<Object[]> list, KpiCacheSet setting);

    /**
     * 查询Redis 数据库返回set类型数据
     * @param key
     * @return
     */
    Set<String> getSetFromRedis(String key);


    /**
     * 从hash中取得map
     */
    Map<String, String> getMapFromRedis(String key);

    /**
     * 通过key获取single 结构唯一值
     * @param key
     * @return
     */
    Double getKpiSingleValue(String key);
}
