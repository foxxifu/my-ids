package com.interest.ids.redis.client.service;

import redis.clients.jedis.JedisPool;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.cache.Cache;
import com.interest.ids.common.project.dto.SearchDeviceDto;
import com.interest.ids.redis.client.AbstractRedisAdapter;
import com.interest.ids.redis.utils.CacheConstant;

/**
 * 
 * @author lhq
 *
 *
 */
public class UnbindDeviceClient extends AbstractRedisAdapter implements Cache{
	
	private static final String PROFIX = "unbindDevice";

	public UnbindDeviceClient(JedisPool pool) {
		super(pool, SearchDeviceDto.class);
	}

	
	public void put(SearchDeviceDto device){
		String key = generateKey(device.getSn());
		super.put(key, device);
	}
	
	public SearchDeviceDto get(String sn){
		
		String key = generateKey(sn);
		return  (SearchDeviceDto) super.get(key);
	}
	
	public String generateKey(String esn){
		StringBuilder key = new StringBuilder(PROFIX);
		key.append(CacheConstant.SPLIT_FLAG$).append(esn);
		
		return key.toString();
	}
	
	
	@Override
	public String getBizName() {
		return PROFIX;
	}

}
