package com.interest.ids.biz.kpicalc.kpi.util;

import com.interest.ids.common.project.spring.context.SystemContext;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.common.project.utils.MathUtil;
import com.interest.ids.redis.caches.JedisDBEnum;
import com.interest.ids.redis.client.JedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;
import redis.clients.jedis.ZParams.Aggregate;

import java.util.*;

public class KpiJedisCacheUtil {

    private static final Logger logger = LoggerFactory.getLogger(KpiJedisCacheUtil.class);


    // Redis 数据过期时间 2h
    private static final int EXPIRE_SECONDS = 2 * 3600;

    /**
     * 批量zadd数据到Redis数据库
     * @param data
     */
    public static void batchZadd(Map<String, Map<String, Double>> data) {
        Jedis jedis = null;
        Pipeline pipeline = null;
        try{
            jedis = getRedisInstanace();
            pipeline = jedis.pipelined();
            int count = 1;
            for (Map.Entry<String, Map<String, Double>> en : data.entrySet()){
                if(count % 1000 == 0)
                    pipeline.sync();
                pipeline.zadd(en.getKey(), en.getValue());
                pipeline.expire(en.getKey(), EXPIRE_SECONDS);
                count++;
            }
            pipeline.sync();
        } catch(Exception e){
            logger.error("Redis zadd data failed:", e);
        } finally{
            closeResource(jedis, pipeline);
        }
    }

    /**
     * 批量sadd 到Redis数据库
     * 
     * @param key
     * @param keys
     */
    public static void batchSadd(String key, Collection<String> keys) {
        Jedis jedis = null;
        Pipeline pipeline = null;
        try{
            jedis = getRedisInstanace();
            pipeline = jedis.pipelined();
            for (String s : keys){
                pipeline.sadd(key, s);
            }
            pipeline.expire(key, EXPIRE_SECONDS);
            pipeline.sync();
            pipeline.clear();
        } catch(Exception e){
            logger.error("redis sadd data failed:  key=" + key, e);
        } finally{
            closeResource(jedis, pipeline);
        }
    }
    
    /**
     * 批量hset 到Redis数据库
     * @param key
     * @param val
     */
    public static void hset(String key, Map<String, String> val){
        Jedis jedis = null;
        Pipeline pipeline = null;
        try{
            jedis = getRedisInstanace();
            pipeline = jedis.pipelined();
            for (Map.Entry<String, String> entr : val.entrySet()){
                pipeline.hset(key, entr.getKey(), entr.getValue());
            }
            pipeline.expire(key, EXPIRE_SECONDS);
            pipeline.sync();
            pipeline.clear();
        } catch(Exception e){
            logger.error("redis hset data failed:  key=" + key, e);
        } finally{
            closeResource(jedis, pipeline);
        }
    }
    
    /**
     * 获得存入的HashMap 结构数据
     * @param key
     * @return
     */
    public static Map<String, String> hgetAll(String key){
        Map<String, String> result = new HashMap<>();
        Jedis jedis = null;
        try{
            jedis = getRedisInstanace();
            result = jedis.hgetAll(key);
        }catch(Exception e){
            logger.error("redis hget map failed:  key=" + key, e);
        }finally{
            closeResource(jedis, null);
        }
        return result;
    }
    
    /**
     * 检索redis中匹配的key值
     * @param key pattern
     * @return
     */
    public static Set<String> sscan(String key) {
        Set<String> result = new HashSet<>();
        Jedis jedis = null;
        try{
            jedis = getRedisInstanace();
            String scanCursor = ScanParams.SCAN_POINTER_START;
            ScanResult<String> rs = null;
            do{
                rs = jedis.sscan(key, scanCursor);
                if(null != rs){
                    scanCursor = rs.getStringCursor();
                    List<String> scanResult = rs.getResult();
                    result.addAll(scanResult);
                }
            } while (!ScanParams.SCAN_POINTER_START.equals(scanCursor));
        } catch(Exception e){
            logger.error("redis sscan result got error: key=" + key, e);
        } finally{
            closeResource(jedis, null);
        }
        return result;
    }

    /**
     * 删除匹配的key
     * @param keyPattern
     */
    public static void deleteByScan(String keyPattern) {
        Jedis jedis = null;
        Pipeline pipeline = null;
        try{
            jedis = getRedisInstanace();
            pipeline = jedis.pipelined();
            String scanCursor = ScanParams.SCAN_POINTER_START;
            ScanParams scanParams = new ScanParams();
            scanParams.match(keyPattern);
            ScanResult<String> rs = null;
            List<String> matchedKeys = new ArrayList<>();
            do{
                rs = jedis.scan(scanCursor, scanParams);
                if(null != rs){
                    scanCursor = rs.getStringCursor();
                    List<String> scanResult = rs.getResult();
                    if(scanResult != null && scanResult.size() > 0){
                        matchedKeys.addAll(scanResult);
                    }
                }
            }
            while (!ScanParams.SCAN_POINTER_START.equals(scanCursor));

            if(matchedKeys != null && matchedKeys.size() > 0){
                pipeline.del(matchedKeys.toArray(new String[] {}));
                pipeline.sync();
            }
        }catch(Exception e){
            logger.error("redis delete keys failed: keyPattern=" + keyPattern, e);
        }finally{
            closeResource(jedis, pipeline);
        }
    }

    /**
     * 删除指定的key
     * @param key
     */
    public static void deleteByKey(String key) {
        Jedis jedis = null;
        try{
            jedis = getRedisInstanace();
            jedis.del(key);
        } catch(Exception e){
            logger.error("redis delete key failed: key=" + key, e);
        } finally{
            closeResource(jedis, null);
        }
    }

    /**
     * 对redis中有序集合进行排序
     * @param key
     * @return
     */
    public static Set<Tuple> zrangeWithScores(String key) {
        Set<Tuple> set = null;
        Jedis jedis = null;
        try{
            jedis = getRedisInstanace();
            set = jedis.zrangeWithScores(key, 0, -1);
        } catch(Exception e){
            logger.error("redis zrangeWithScores got error: key=" + key, e);
        } finally{
            closeResource(jedis, null);
        }
        return set;
    }

    /**
     * 对有序集合取并集，合并的元素对score 求和 作为该元素新的score值
     * @param key 合并之后存入的key
     * @param keys 参与计算的keys
     * @return
     */
    public static Long zunionstore(String key, Set<String> keys) {
        Jedis jedis = null;
        Long result = null;
        try{
            jedis = getRedisInstanace();
            result = jedis.zunionstore(key, keys.toArray(new String[] {}));
        }catch(Exception e){
            logger.error("redis zunionstore occer an error:  key=" + key, e);
        }finally{
            closeResource(jedis, null);
        }
        return result;
    }

    /**
     * Redis zunionstore 操作，根据计算方式处理相同元素
     * @param key 计算完毕存储到的key
     * @param keys 参与计算的keys
     * @param type Aggregate.MAX 求最大 Aggregate.MIN 求最小 Aggregate.SUM 求和
     * @return
     */
    public static Long zunionstore(String key, Set<String> keys, Aggregate type) {
        Jedis jedis = null;
        Long result = null;
        try{
            jedis = getRedisInstanace();
            result = jedis.zunionstore(key, new ZParams().aggregate(type), keys.toArray(new String[] {}));
        }catch(Exception e){
            logger.error("redis zunionstore got error:  key=" + key, e);
        }finally{
            closeResource(jedis, null);
        }
        return result;
    }

    /**
     * 返回所有匹配的实例，通过redis scan命令
     * @param keyPattern
     * @return
     */
    public static Set<String> scan(String keyPattern) {
        Jedis jedis = null;
        Set<String> result = null;
        try{
            jedis = getRedisInstanace();
            ScanParams scanParams = new ScanParams();
            scanParams.match(keyPattern);
            scanParams.count(1000);
            String scanCursor = ScanParams.SCAN_POINTER_START;
            result = new HashSet<>();
            ScanResult<String> rs = null;
            do{
                rs = jedis.scan(scanCursor, scanParams);
                if(null != rs){
                    scanCursor = rs.getStringCursor();
                    List<String> scanResult = rs.getResult();
                    result.addAll(scanResult);
                }
            } while (!ScanParams.SCAN_POINTER_START.equals(scanCursor));
        } catch(Exception e){
            logger.error("redis scan got error: key=" + keyPattern, e);
        } finally{
            closeResource(jedis, null);
        }
        return result;
    }

    /**
     * 根据排序方式对有序集合进行排序
     * @param key
     * @param rangeType
     * @param startIndex
     * @param endIndex
     * @return
     */
    public static Set<Tuple> zrangeWithScore(String key, int rangeType, int startIndex, int endIndex) {
        Jedis jedis = null;
        Set<Tuple> set = null;
        try{
            jedis = getRedisInstanace();
            set = new LinkedHashSet<>();
            if(KpiCacheSet.CACHE_RANGE_TYPE_ASC == rangeType){
                set = jedis.zrangeWithScores(key, startIndex, endIndex);
            }else{
                set = jedis.zrevrangeWithScores(key, startIndex, endIndex);
            }
        } catch(Exception e){
            logger.error("redis zrange with score failed: key=" + key, e);
        } finally{
            closeResource(jedis, null);
        }
        return set;
    }

    /**
     * 求 zrange all 升序
     * @param key
     * @return
     */
    public static Set<Tuple> zrangeAll(String key) {
        return zrangeWithScore(key, KpiCacheSet.CACHE_RANGE_TYPE_ASC, 0, -1);
    }

    /**
     * 对redis数据中指定的有序集合进行降序排序
     * @param key
     * @return
     */
    public static Set<Tuple> zrevrangeAll(String key) {
        return zrangeWithScore(key, KpiCacheSet.CACHE_RANGE_TYPE_DESC, 0, -1);
    }

    /**
     * 从kpi array 结构中获取某个数的结果
     * @param key
     * @param rangeType
     * @param defaultKey
     * @return
     */
    public static Double getScoreByArrayCache(String key, int rangeType, Double defaultKey) {
        Set<Tuple> set = zrangeWithScore(key, rangeType, 0, 0);
        if(null != set && set.size() > 0){
            Tuple t = set.toArray(new Tuple[] {})[0];
            String scoreString = t.getElement().split(",")[1];
            return MathUtil.formatDouble(scoreString, defaultKey);
        }
        return defaultKey != null ? defaultKey : null;
    }

    /**
     * 返回有序集合元素个数
     * @param key
     * @return
     */
    public static Long zcard(String key) {
        Jedis jedis = null;
        try{
            jedis = getRedisInstanace();
            return jedis.zcard(key);
        } catch(Exception e){
            logger.error("redis zcard got error: key=" + key, e);
        } finally{
            closeResource(jedis, null);
        }
        return 0L;
    }

    /**
     * 关闭通道
     * @param jedis
     * @param pipeline
     */
    public static void closeResource(Jedis jedis, Pipeline pipeline) {
        try{
            if(null != pipeline){
                pipeline.close();
            }
        } catch(Exception e){
            logger.error("close pipeline got error",e);
        } finally {
            if(null!=jedis){
                try {
                    jedis.close();
                } catch (Exception e){
                    logger.error("close jedis client got error",e);
                }
            }
        }
    }
    
    /**
     * 将score 唯一的zset类型数据存入Map中
     * @param allNodes
     * @return
     */
    public static Map<Double, Tuple> transformZsetToMap(Set<Tuple> allNodes){
        Map<Double, Tuple> result = new HashMap<>();
        if(CommonUtil.isNotEmpty(allNodes)){
            for(Tuple tuple : allNodes){
                result.put(tuple.getScore(), tuple);
            }
        }
        return result;
    }

    /**
     * 获取redis client引用
     */
    private static JedisClient getRedisClientInstance() {
        return (JedisClient) SystemContext.getBean("jedClient");
    }

    /**
     * 获取redis client , 连接到指定的redis db
     * @return
     */
    private static Jedis getRedisInstanace(){
        Jedis jedis = getRedisClientInstance().getJedis();
        jedis.select(JedisDBEnum.KPI_STAT.getIndex().intValue());
        return jedis;
    }
}
