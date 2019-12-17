package com.interest.ids.dev.api.utils;

import com.interest.ids.common.project.utils.SpringBeanContext;
import com.interest.ids.dev.api.service.Data2DbService;
import com.interest.ids.dev.api.service.DevDeviceService;
import com.interest.ids.dev.api.service.NormalizedService;
import com.interest.ids.dev.api.service.SignalService;
import com.interest.ids.dev.api.service.SubscribeSignalService;
import com.interest.ids.redis.client.DefaultCacheClient;
import com.interest.ids.redis.client.service.ConnStatusCacheClient;
import com.interest.ids.redis.client.service.UnbindDeviceClient;

/**
 * 
 * @author lhq
 * 统一不做可见性处理
 *
 */
public class DevServiceUtils {
	
	private static DevDeviceService devService;
		
	private static NormalizedService unService;
	
	private static SignalService signalService;
	
	private static DefaultCacheClient defaultClient;
	
	private static UnbindDeviceClient unbindClient;
	
	private static Data2DbService data2DbService;
	
	private static SubscribeSignalService subService;
	
	private static ConnStatusCacheClient connStatusCacheClient;
	
	public static DevDeviceService getDevService(){
		if(devService == null){
			
			devService = (DevDeviceService) SpringBeanContext.getBean("devService");
		}
		return devService;
	} 
	
	public static Data2DbService getData2DbService(){
		if(data2DbService == null){
			
			data2DbService = (Data2DbService) SpringBeanContext.getBean("data2DbService");
		}
		return data2DbService;
	} 
	
	
	public static NormalizedService getUnificationService(){
		
		if(unService == null){
			unService = (NormalizedService) SpringBeanContext.getBean("unificationService");
		}
		return unService;
	}
	
	public static SignalService getSignalService(){
		if(signalService == null){
			
			signalService = (SignalService) SpringBeanContext.getBean("signalService");
			
		}
		return signalService;
	}
	
	public static DefaultCacheClient getCacheClient(){
		
		if(defaultClient == null){
			
			defaultClient = (DefaultCacheClient) SpringBeanContext.getBean("defaultCacheClient");
			
		}
		return defaultClient;
	}
	
	public static UnbindDeviceClient getUnbindDeviceClient(){
		if(unbindClient == null){
			
			unbindClient = (UnbindDeviceClient) SpringBeanContext.getBean("unBindDeviceClient");
		}
		return unbindClient;
	} 
	
	public static SubscribeSignalService getSubService(){
		
		if(subService == null){
			
			subService = (SubscribeSignalService) SpringBeanContext.getBean("subscribeSignalService");
			
		}
		return subService;
	}
	
	public static ConnStatusCacheClient getConnStatusCacheClient(){
		if(connStatusCacheClient == null) {
			connStatusCacheClient = (ConnStatusCacheClient)SpringBeanContext.getBean("connCacheClient");
		}
		return connStatusCacheClient;
	}

}
