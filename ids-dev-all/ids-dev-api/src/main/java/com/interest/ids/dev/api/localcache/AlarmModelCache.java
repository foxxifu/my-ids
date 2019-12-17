package com.interest.ids.dev.api.localcache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.interest.ids.common.project.bean.alarm.AlarmModel;
import com.interest.ids.common.project.cache.CacheElement;
import com.interest.ids.dev.api.utils.CacheConstant;
import com.interest.ids.dev.api.utils.DevServiceUtils;


/**
 * 
 * @author lhq
 * 告警模型cache
 *
 */
public class AlarmModelCache {
	
	private static Map<String,Object> models = new ConcurrentHashMap<>();
	
	
	public static void put(String key,Object model){
		CacheElement element = new CacheElement(key,model,CacheConstant.DEFAULT_CACHE_EXPIRE_TIME);
		models.put(key, element);
	}

	public static AlarmModel getAlarmModel(String stationCode,Long modelId){
		
		String key = stationCode+modelId;
		Object element =  models.get(key);
		
		if(element == null){
			//防止缓存穿透
			AlarmModel data = DevServiceUtils.getDevService().getAlarmModel(modelId, stationCode);
			if(data == null){
				return null;
			}else{
				put(key,data);
				return data;
			}
		}
		
		CacheElement cacheData = (CacheElement)element;
		if(cacheData.isExpired()){
			models.remove(key);
			AlarmModel data = DevServiceUtils.getDevService().getAlarmModel(modelId, stationCode);
			if(data != null){
				put(key,data);
				return data;
			}
		}
		return (AlarmModel) cacheData.getCacheData();
	}
	

}
