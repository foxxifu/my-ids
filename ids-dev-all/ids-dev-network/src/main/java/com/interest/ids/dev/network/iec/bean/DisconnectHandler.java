package com.interest.ids.dev.network.iec.bean;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.cache.CacheElement;
import com.interest.ids.common.project.constant.DeviceConstant.ConnectStatus;
import com.interest.ids.dev.api.handler.BizEventHandler.DataMsgType;
import com.interest.ids.dev.api.handler.BizHandlerBus;
import com.interest.ids.dev.api.handler.DataDto;


/**
 * 
 * @author lhq
 * 设备断链后的处理操作
 *
 */
public class DisconnectHandler {
	
	private static final Logger log = LoggerFactory.getLogger(DisconnectHandler.class);

	
	private Map<Long,CacheElement> map = new ConcurrentHashMap<>();
	
	private static DisconnectHandler handler = new DisconnectHandler();
	
	//过期时间15分钟
	private static final long EXPIRE_TIME = 15 * 60 * 1000;
	
	private DisconnectHandler(){
		new Thread(new CheckThread(),"connection-check").start();
	}
	
	public static DisconnectHandler getHandler(){
		return handler;
	}
	
	public void onDisconnect(DeviceInfo dev){
		CacheElement element = new CacheElement(dev.getId(),dev,EXPIRE_TIME);
		map.put(dev.getId(), element);
	}
	
	
	public void onConnect(DeviceInfo dev){
		CacheElement element = map.get(dev.getId());
		if(element != null){
			map.remove(dev.getId());
		}
		DataDto dto = new DataDto(DataMsgType.CONNECTION,dev,ConnectStatus.CONNECTED);
    	BizHandlerBus.handle(dto);
	}
	
	
	class CheckThread implements Runnable{

		
		@Override
		public void run() {
			while(true){
				try {
					
					Collection<CacheElement> values = map.values();
					for(CacheElement ele : values){
						if(ele.isExpired()){
							DataDto dto = new DataDto(DataMsgType.CONNECTION,(DeviceInfo) ele.getCacheData(),ConnectStatus.DISCONNECTED);
					    	BizHandlerBus.handle(dto);
						}
					}
					
				} catch (Exception e) {
					log.error("check thread error",e);
				}
				try {
					Thread.sleep(30 * 1000L);
				} catch (InterruptedException e) {
					
				}
			}
		}
	}

}
