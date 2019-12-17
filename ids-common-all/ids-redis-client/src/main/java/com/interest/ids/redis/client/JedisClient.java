package com.interest.ids.redis.client;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

/**
 * 
 * @author lhq
 *
 *
 */
public class JedisClient {
	private static final Logger logger = LoggerFactory.getLogger(JedisClient.class);

	private Pool<Jedis> redisPool;

	public Pool<Jedis>  getRedisPool() {
		return redisPool;
	}

	public void setRedisPool(Pool<Jedis>  redisPool) {
		this.redisPool = redisPool;
	}

	public Jedis getJedis() {
		try {
			Jedis jedis = redisPool.getResource();
			if (jedis == null) {
				logger.warn("There is no avilable redis..");
				return null;
			}
			return jedis;
		} catch (Exception e) {
			logger.error("redisPool has activeNum ["+redisPool.getNumActive()+"] idleNum ["+redisPool.getNumIdle()+"]");
			logger.error("getJedis has error",e);
		}
		return null;
	}

	public void release(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}
	public void init(){
		
	}
}
