package com.interest.ids.redis.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import com.interest.ids.common.project.cache.Cache;
import com.interest.ids.common.project.utils.FastJsonSerializable;


/**
 * 
 * @author lhq
 *
 *
 */
public abstract class AbstractRedisAdapter implements Cache,BatchAdapter{
	
	protected JedisPool pool;
	
	protected String bizProfix;
		
	protected Class<?> clazz; 
	
	protected int keySize;
	
	protected CacheMissHandler missHandler;
	
	public AbstractRedisAdapter(JedisPool pool,Class<?> clazz){
		this.pool = pool;
		this.clazz = clazz;
	}
	
	
	private Object execute(RedisCallback callback) {
	    Jedis jedis = pool.getResource();
	    try {
	    	
	    	return callback.doWithRedis(jedis);
	      
	    } finally {
	      jedis.close();
	    }
	 }
	
	public void batchPut(final Map<String,Object> datas){
		
		execute(new RedisCallback() {
		      @Override
		      public Object doWithRedis(Jedis jedis) {
		    	  Pipeline pipeline = jedis.pipelined();
		    	  for(Map.Entry<String, Object> entry:datas.entrySet()){
		    		  pipeline.set(FastJsonSerializable.encode(entry.getKey()), FastJsonSerializable.encode(entry.getValue()));
		    	  }
		    	  pipeline.sync();
		    	  try {
					pipeline.close();
				} catch (IOException e) {
					
				}
		    	  return null;
		      }
		 });
	}
	
	//目前只支持统一的时间设置
	public void batchPutWithExipre(final Map<String,Object> datas,final int expireTime){
		
		execute(new RedisCallback() {
		      @Override
		      public Object doWithRedis(Jedis jedis) {
		    	  Pipeline pipeline = jedis.pipelined();
		    	  for(Map.Entry<String, Object> entry:datas.entrySet()){
		    		  pipeline.set(FastJsonSerializable.encode(entry.getKey()), FastJsonSerializable.encode(entry.getValue()));
		    		  pipeline.setex(FastJsonSerializable.encode(entry.getKey()), expireTime, FastJsonSerializable.encode(entry.getValue()));
		    	  }
		    	  pipeline.sync();
		    	  try {
					pipeline.close();
				} catch (IOException e) {
					
				}
		    	  return null;
		      }
		 });
	}
	
	public List<Object> batchGet(final Set<String> keys){
		
		//String[] kk = new String[keys.size()];
		
		Jedis jedis = pool.getResource();
		Pipeline pipeline = null;
		try {
			pipeline = jedis.pipelined();
			byte[][] keyArray = getKeys(keys);
			
			Response<List<byte[]>> res = pipeline.mget(keyArray);
			//Response<List<String>> res2 = pipeline.mget(kk);
			pipeline.sync();
			List<byte[]> list = res.get();
			//List<String> list2 = res2.get();
			if(list != null){
				List<Object> values = new ArrayList<>();
				for(byte[] cell:list){
					if(cell != null){
						values.add(FastJsonSerializable.decode(cell, Object.class));
					}else{
						values.add(null);
					}
				}
				return values;
			}
		}finally{
			jedis.close();
			try {
				pipeline.close();
			} catch (IOException e) {
				
			}
		}
		
		return null;
	}
	
	private byte[][] getKeys(Set<String> keys){
		byte[][] keyArray = new byte[keys.size()][];
		int i = 0;
		for(String key:keys){
			byte[] data = FastJsonSerializable.encode(key);
			keyArray[i]=data;
			i += 1;
		}
		return keyArray;
	}
	
	public List<Object> getValuesByPattern(String pattern){
		Jedis jedis = pool.getResource();
		
		try {
			Set<String> set = jedis.keys(pattern);
			
			return batchGet(set);
		} finally{
			jedis.close();
		}
		
	}
	
	public void batchVagueDel(String prefix){
		Jedis jedis = pool.getResource();
		try {
			Set<String> keys = jedis.keys(prefix);
			for(String s : keys){
				jedis.del(s);
			}
		} finally {
			jedis.close();
		}
	}


	public void put(final Object key, final Object value) {
		
		
	    execute(new RedisCallback() {
	      @Override
	      public Object doWithRedis(Jedis jedis) {
	        jedis.set(FastJsonSerializable.encode(key), FastJsonSerializable.encode(value));
	        return null;
	      }
	    });
	  }
	
	public Object get(final Object key) {
	    return execute(new RedisCallback() {
	      @Override
	      public Object doWithRedis(Jedis jedis) {
	    	byte[] data =  jedis.get(FastJsonSerializable.encode(key));
	    	if(data != null){
	    		return FastJsonSerializable.decode(data,clazz);
	    	}
	    	else{
	    		if(missHandler != null){
	    			return missHandler.handle(jedis,key);
	    		}
	    		return null;
	    	}
	      }
	    });
	  }

	  
	  public Object removeObject(final Object key) {
	    return execute(new RedisCallback() {
	      @Override
	      public Object doWithRedis(Jedis jedis) {
	        return jedis.hdel(FastJsonSerializable.encode(key));
	      }
	    });
	  }
	  
	  
	  public void clear() {
	    execute(new RedisCallback() {
	      @Override
	      public Object doWithRedis(Jedis jedis) {
	        jedis.del(FastJsonSerializable.encode(bizProfix));
	        return null;
	      }
	    });

	  }
	  
	  public abstract String getBizName();

}
