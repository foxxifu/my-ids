package com.interest.ids.redis.caches;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.redis.utils.RedisUtil;

public class BenchmarkInvCache {

    private static final Logger logger = LoggerFactory.getLogger(BenchmarkInvCache.class);
    
    public static final String BASE_KEY = "BENCHMARK_INV";
    
    public static String generateCacheKey(String stationCode){
        if (CommonUtil.isEmpty(stationCode)) {
            return null;
        }
        
        return BASE_KEY + "@" + stationCode;
    }
    
    /**
     * 将自动识别的标杆逆变器存入Redis中
     * @param stationCode
     * @param bechmarkInvIds
     */
    public static void storeBenchmarkInverter(String stationCode, Set<Long> benchmarkInvIds) {
        if (CommonUtil.isEmpty(stationCode) || CommonUtil.isEmpty(benchmarkInvIds)) {
            logger.warn("stationCode or benchmark inverter ID is empty, can't permanent.");
            return;
        }
        
        Jedis jedis = null;
        try {
            jedis = RedisUtil.getJedis(JedisDBEnum.DEV_INFO.getIndex().intValue());
            if (jedis != null) {
                String cacheKey = generateCacheKey(stationCode);
                // 先清除该缓存
                jedis.del(cacheKey);
                
                // 将逆变器设备编号存入内存中
                String[] invIds = new String[benchmarkInvIds.size()];
                int i = 0;
                for (Long devId : benchmarkInvIds) {
                    invIds[i] = String.valueOf(devId);
                    i ++;
                }
                
                jedis.sadd(cacheKey, invIds);
            }
        } catch (Exception e) {
            logger.error("store benchmark inverter failed.", e);
        } finally {
            RedisUtil.closeJeids(jedis);
        }
    }
    
    public static Set<Long> getBenchmarkInvIds(String stationCode){
        if (CommonUtil.isEmpty(stationCode)) {
            return null;
        }
        
        Set<Long> result = null;
        
        Jedis jedis = null;
        try {
            jedis = RedisUtil.getJedis(JedisDBEnum.DEV_INFO.getIndex().intValue());
            Set<String> invIds = jedis.smembers(generateCacheKey(stationCode));
            if (invIds != null) {
                result = new HashSet<Long>();
                for (String devId : invIds) {
                    result.add(Long.valueOf(devId));
                }
            }
        } catch (Exception e) {
            logger.error("get benchmark inverter failed.", e);
        } finally {
            RedisUtil.closeJeids(jedis);
        }
                
        return result;
    }
    
    public static Map<String, Set<Long>> getAllBenchmarkInvIds(){

        Map<String, Set<Long>> result = new HashMap<>();
        
        Jedis jedis = null;
        try {
            jedis = RedisUtil.getJedis(JedisDBEnum.DEV_INFO.getIndex().intValue());
            Set<String> cacheKeys = jedis.keys(generateCacheKey("*"));
            if (cacheKeys != null && cacheKeys.size() > 0) {
                for (String cacheKey : cacheKeys) {
                    Set<String> invIds = jedis.smembers(cacheKey);
                    if (invIds != null) {
                        Set<Long> temp = new HashSet<>();
                        for (String devId : invIds) {
                            temp.add(Long.valueOf(devId));
                        }
                        
                        String stationCode = cacheKey.substring(cacheKey.indexOf(BASE_KEY + "@") + 1);
                        result.put(stationCode, temp);
                    }
                }
            }
            
            
        } catch (Exception e) {
            logger.error("get benchmark inverter failed.", e);
        } finally {
            RedisUtil.closeJeids(jedis);
        }
                
        return result;
    }
}
