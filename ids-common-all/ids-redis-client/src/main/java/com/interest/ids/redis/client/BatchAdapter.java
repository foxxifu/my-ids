package com.interest.ids.redis.client;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 
 * @author lhq
 *
 *
 */
public interface BatchAdapter {
	
	void batchPut(final Map<String,Object> datas);
	
	List<Object> batchGet(final Set<String> keys);
}
