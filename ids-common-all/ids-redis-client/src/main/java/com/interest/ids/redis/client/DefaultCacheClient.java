package com.interest.ids.redis.client;

import redis.clients.jedis.JedisPool;


/**
 * 
 * @author lhq
 *
 *
 */
public class DefaultCacheClient extends AbstractRedisAdapter{
	
	public DefaultCacheClient(JedisPool pool) {
		super(pool,Object.class);
	}

	private static final String bizName = "default";

	@Override
	public String getBizName() {
		
		return bizName;
	}

}
