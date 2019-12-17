package com.interest.ids.redis.client.service;

import redis.clients.jedis.JedisPool;

import com.interest.ids.common.project.cache.Cache;
import com.interest.ids.common.project.constant.DeviceConstant.ConnectStatus;
import com.interest.ids.redis.client.AbstractRedisAdapter;
import com.interest.ids.redis.utils.CacheConstant;

/**
 * 
 * @author lhq
 *
 *
 */
public class ConnStatusCacheClient extends AbstractRedisAdapter implements Cache{
	
	private static final String CONNSTATUS = "connStatus";

	public ConnStatusCacheClient(JedisPool pool) {
		super(pool, ConnectStatus.class);
	}

	
	public void put(Long devid,ConnectStatus status){
		String key = generateKey(devid);
		super.put(key, status);
	}
	
	public ConnectStatus get(Long devid){
		
		String key = generateKey(devid);
		return (ConnectStatus) super.get(key);
	}
	
	public String generateKey(long devid){
		StringBuilder key = new StringBuilder(CONNSTATUS);
		key.append(CacheConstant.SPLIT_FLAG$).append(devid);
		
		return key.toString();
	}
	
	
	@Override
	public String getBizName() {
		return CONNSTATUS;
	}

}
