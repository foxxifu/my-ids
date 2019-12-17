package com.interest.ids.redis.client;

import redis.clients.jedis.Jedis;


/**
 * 
 * @author lhq
 *
 *
 */
public interface CacheMissHandler {
	
	
	 Object handle(Jedis jedis,Object key);

}
