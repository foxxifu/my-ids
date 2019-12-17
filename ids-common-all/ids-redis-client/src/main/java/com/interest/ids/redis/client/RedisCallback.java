
package com.interest.ids.redis.client;

import redis.clients.jedis.Jedis;

/**
 * 
 * @author lhq
 *
 *
 */
public interface RedisCallback {

    Object doWithRedis(Jedis jedis);
}
