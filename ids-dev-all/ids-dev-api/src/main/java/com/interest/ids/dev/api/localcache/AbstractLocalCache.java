package com.interest.ids.dev.api.localcache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.interest.ids.common.project.cache.Cache;
import com.interest.ids.common.project.cache.CacheElement;


/**
 * 
 * @author lhq
 *
 *
 */
public abstract class AbstractLocalCache implements Cache{
	
	private Map<Object,CacheElement> map = new ConcurrentHashMap<>();
	
	public AbstractLocalCache(){
		
	}

	
	@Override
	public void put(Object key, Object value) {
		CacheElement element = new CacheElement(key,value);
		map.put(key, element);
	}

	@Override
	public Object get(Object key) {
		CacheElement element = map.get(key);
		if(element != null){
			return element.getCacheData();
		}
		return null;
	}
	
	abstract Object getData(Object key);

}
