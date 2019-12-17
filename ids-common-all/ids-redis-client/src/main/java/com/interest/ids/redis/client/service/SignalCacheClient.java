package com.interest.ids.redis.client.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.interest.ids.common.project.cache.Cache;
import com.interest.ids.redis.client.AbstractRedisAdapter;
import com.interest.ids.redis.utils.CacheConstant;


/**
 * 
 * @author lhq
 *
 *
 */
public class SignalCacheClient extends AbstractRedisAdapter implements Cache{
	
	public SignalCacheClient(JedisPool pool) {
		super(pool, Object.class);
	}

	private static final String SIGNAL_VALUE_PROFIX = "signal";
	
	//生成规则，按照SIGNAL_VALUE_PROFIX和deviceid和signalid的组合
	
	
	public void put(long deviceId,long signalId,Object value){
		String key = generateKey(deviceId,signalId);
		super.put(key, value);
	}
	
	public Object get(long deviceId,long signalId){
		String key = generateKey(deviceId,signalId);
		return super.get(key);
	}
	
	public Object get(long deviceId,String columnName){
		String key = generateKey(deviceId,columnName);
		return super.get(key);
	}
	// 根据前缀模糊查询 尽量不要使用存在风险  删除的前缀格式一定要保证唯一
	public void batchVagueDel(String prefix){
		super.batchVagueDel(prefix);
	}
	public Map<String,Object> batchGet(long deviceId,Set<String> columns){
		
		Set<String> keys = new LinkedHashSet<String>();
		List<String> list = new ArrayList<String>();
		for(String column : columns){
			String key = generateKey(deviceId,column);
			keys.add(key);
			list.add(column);
		}
		List<Object> datas = super.batchGet(keys);
		if(datas != null){
			Map<String,Object> map = new HashMap<String,Object>();
			if(datas.size() != keys.size()){
				return null;
			}
			
			for(int i=0;i<list.size();i++){
				map.put(list.get(i), datas.get(i));
				
			}
			return map;
		}
		return null;
	}
	// 生成用于批量模糊删除的键(没有特殊情况莫用)
	public String generateKeyToVagueDel(long deviceId){
		StringBuilder builder = new StringBuilder();
		builder.append(SIGNAL_VALUE_PROFIX).append(CacheConstant.DEFAULT_SPLIT_FLAG)
			   .append(deviceId).append(CacheConstant.DEFAULT_SPLIT_FLAG);
		return builder.toString().trim();
	}
	
	public String generateKey(long deviceId,long signalId){
		StringBuilder builder = new StringBuilder();
		builder.append(SIGNAL_VALUE_PROFIX).append(CacheConstant.DEFAULT_SPLIT_FLAG)
			   .append(deviceId).append(CacheConstant.DEFAULT_SPLIT_FLAG)
			   .append(signalId);
		return builder.toString().trim();
	}
	
	public String generateKey(long deviceId,String columnName){
		StringBuilder builder = new StringBuilder();
		builder.append(SIGNAL_VALUE_PROFIX).append(CacheConstant.DEFAULT_SPLIT_FLAG)
			   .append(deviceId).append(CacheConstant.DEFAULT_SPLIT_FLAG)
			   .append(columnName);
		return builder.toString().trim();
	}

	@Override
	public String getBizName() {
		
		return SIGNAL_VALUE_PROFIX;
	}

	

}
