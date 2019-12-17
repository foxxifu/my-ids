package com.interest.ids.biz.web.operation.cache;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import com.interest.ids.redis.utils.RedisUtil;
import com.interest.ids.redis.utils.SerializeUtil;

public class OperationWorkSiteCache {

    private static final Logger logger = LoggerFactory.getLogger(OperationWorkSiteCache.class);

    public static final String OPERATIONWORKSITE_REDIS_BASE_KEY = "operationworksite";

    public static final String TOTAL_SIZE = "totalsize";

    public static final String TOTAL_PAGE = "totalpage";

    public static final String RESULT_LIST = "resultlist";

    public static final String SEPERATOR = ":";
    
    public static final int OPERATION_DATA_EXPIRE_SECONDS = 120;

    /**
     * 将运维工作台初始界面数据放入缓存，根据用户ID进行缓存
     * 
     * @param userId
     */
    public static void putOperationStationData(Long userId, Map<String, Object> result) {
        if (result != null && result.size() > 0) {
            Jedis jedis = null;
            try {
                
                if(userId == null){
                    return;
                }
                
                jedis = RedisUtil.getJedis();

                // 先清除该用户缓存数据
                jedis.del(generateBaseKey(userId));

                // 缓存Map对象进行序列化
                String serializeStr = SerializeUtil.serialize2String(result);
                if (serializeStr != null) {
                    jedis.set(generateBaseKey(userId), serializeStr);
                    //设置过期时长1小时
                    jedis.expire(generateBaseKey(userId), OPERATION_DATA_EXPIRE_SECONDS);
                }
            } catch (Exception e) {
                logger.error("Save OperationStationVo to Cache get error.", e);
            } finally {
                RedisUtil.closeJeids(jedis);
            }
        }
    }

    /**
     * 获取用户缓存运维工作台初界面数据
     * 
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getOperationStationData(Long userId) {

        Map<String, Object> result = null;
        Jedis jedis = null;
        try {
            jedis = RedisUtil.getJedis();
            if(userId == null){
                return null;
            }
            
            // 获取用户缓存数据
            String readStr = jedis.get(generateBaseKey(userId));
            if(readStr != null){
                result = (Map<String, Object>) SerializeUtil.deSerialize(readStr);
            }

            return result;
        } catch (Exception e) {
            logger.error("get operation station data error.", e);
        } finally {
            RedisUtil.closeJeids(jedis);
        }

        return result;
    }

    /**
     * 生成缓存Key
     * 
     * @param userId
     * @return
     */
    public static String generateBaseKey(Long userId) {
        StringBuilder sb = new StringBuilder(OPERATIONWORKSITE_REDIS_BASE_KEY);
        sb.append(SEPERATOR).append(userId);

        return sb.toString();
    }
}
