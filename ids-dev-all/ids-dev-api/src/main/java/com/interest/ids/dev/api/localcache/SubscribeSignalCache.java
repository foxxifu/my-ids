package com.interest.ids.dev.api.localcache;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.interest.ids.common.project.bean.signal.SubscribeSignal;
import com.interest.ids.dev.api.utils.DevServiceUtils;
/**
 * 
 * @author lhq
 *
 *
 */
public class SubscribeSignalCache {
	
	private static ConcurrentMap<String, List<SubscribeSignal>> subSignals = new ConcurrentHashMap<String, List<SubscribeSignal>>();
	
	
	public static List<SubscribeSignal> get(String signalVersion){
		List<SubscribeSignal> signals = subSignals.get(signalVersion);
		if(signals == null){
			synchronized(SubscribeSignalCache.class){
				if(signals == null){
					signals = DevServiceUtils.getSubService().getSubSignalsByModelCode(signalVersion);
					if(signals != null){
						subSignals.put(signalVersion, signals);
					}
				}
			}
		}
		return signals;
	}

}
