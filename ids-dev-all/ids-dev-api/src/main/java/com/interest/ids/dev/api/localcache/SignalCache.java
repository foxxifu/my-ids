package com.interest.ids.dev.api.localcache;

import io.netty.util.collection.IntObjectHashMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.interest.ids.common.project.bean.signal.SignalInfo;
import com.interest.ids.common.project.utils.SpringBeanContext;
import com.interest.ids.dev.api.service.SignalService;


/**
 * 
 * @author lhq
 *
 *
 */
public class SignalCache {
	
	private static Map<String,IntObjectHashMap<SignalInfo>> map = new ConcurrentHashMap<String,IntObjectHashMap<SignalInfo>>();
	
		
	public static void put(String esn,IntObjectHashMap<SignalInfo> data){
		map.put(esn, data);
	}
	
	private static SignalService getSignalService(){
		
		SignalService service = (SignalService) SpringBeanContext.getBean("signalService");
		return service;
	}

}
