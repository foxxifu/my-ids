package com.interest.ids.redis.client;

import redis.clients.jedis.Jedis;


/**
 * 
 * @author lhq
 *
 *
 */
public class CacheMissSqlHandler implements CacheMissHandler{
	
	private String sql;

	
	
	public CacheMissSqlHandler(){
		
	}


	@Override
	public Object handle(Jedis jedis, Object key) {
		
		return null;
	}


	
	
	 

}
