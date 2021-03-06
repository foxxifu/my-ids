package com.interest.ids.dev.api.localcache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.cache.CacheElement;
import com.interest.ids.dev.api.utils.CacheConstant;
import com.interest.ids.dev.api.utils.DevServiceUtils;


/**
 * 
 * @author lhq
 *
 *
 */
public class DeviceCache {
	
	public static final String DEV_ENS_PROFIX = "devesn:";
	
	public static final String DEV_ID_PROFIX = "devid:";
	
	private static Map<String,AtomicInteger> count = new ConcurrentHashMap<>();
	
	private static Map<String,Object> devices = new ConcurrentHashMap<>();
	
	public static void put(String key,Object dev){
		CacheElement element = new CacheElement(key,dev,CacheConstant.Default_DEV_EXPIRE_TIME);
		devices.put(key, element);
	}
	
	public static DeviceInfo getData(String key){
		
		Object element =  devices.get(key);
		
		if(element == null){
			//防止缓存穿透
			DeviceInfo data = DevServiceUtils.getDevService().getDeviceBySn(key);
			if(data == null){
				AtomicInteger num = count.get(key);
				if(num == null){
					synchronized(count){
						if(num == null){
							//直接计数为1
							num = new AtomicInteger(1);
							count.put(key, num);
						}
					}
				}else{
					int a = num.incrementAndGet();
					if(a > CacheConstant.DEFAULT_COUNT){
						//缓存穿透
						devices.put(key, CacheConstant.DEFAULT_NULL_VALUE);
					}
				}
			}else{
				put(key,data);
				return data;
			}
		}
		if(element == null || CacheConstant.DEFAULT_NULL_VALUE.equals(element)){
			return null;
		}
		CacheElement cacheData = (CacheElement)element;
		return (DeviceInfo) cacheData.getCacheData();
	}
	
	public static DeviceInfo getDeviceById(Long id){
		if(id == null){
			return null;
		}
		String key = DEV_ID_PROFIX+id;
		Object element =  devices.get(key);
		if(element == null){
			//防止缓存穿透
			DeviceInfo data = DevServiceUtils.getDevService().getDeviceByID(id);
			if(data == null){
				AtomicInteger num = count.get(key);
				if(num == null){
					synchronized(count){
						if(num == null){
							//直接计数为1
							num = new AtomicInteger(1);
							count.put(key, num);
						}
					}
				}else{
					int a = num.incrementAndGet();
					if(a>CacheConstant.DEFAULT_COUNT){
						devices.put(key, CacheConstant.DEFAULT_NULL_VALUE);
					}
				}
			}else{
				put(key,data);
				return data;
			}
		}
		if(element == null || CacheConstant.DEFAULT_NULL_VALUE.equals(element)){
			return null;
		}
		CacheElement cacheData = (CacheElement)element;
		return (DeviceInfo) cacheData.getCacheData();
	}
	
}
