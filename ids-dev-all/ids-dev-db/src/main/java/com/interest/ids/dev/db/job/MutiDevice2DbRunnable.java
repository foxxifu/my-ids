package com.interest.ids.dev.db.job;

import io.netty.util.collection.IntObjectHashMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.signal.SignalInfo;
import com.interest.ids.common.project.spring.propholder.CustomPropertyConfigurer;
import com.interest.ids.common.project.utils.SpringBeanContext;
import com.interest.ids.dev.api.bean.SingleRecordBean;
import com.interest.ids.dev.api.localcache.SignalLocalCache;
import com.interest.ids.dev.api.localcache.UnificationCache;
import com.interest.ids.dev.api.utils.DevServiceUtils;
import com.interest.ids.redis.client.service.SignalCacheClient;


/**
 * 
 * @author lhq
 *
 *
 */
public class MutiDevice2DbRunnable implements Runnable{
	
	private static final Logger log = LoggerFactory.getLogger(MutiDevice2DbRunnable.class);

	private long collectTime; 
	
	private DeviceInfo dev;
	
	public MutiDevice2DbRunnable(DeviceInfo dev,long collectTime){
		this.dev = dev;
		this.collectTime = collectTime;
	}
	
	
	@Override
	public void run() {
		
		List<DeviceInfo> childDevs = DevServiceUtils.getDevService().getChildDevices(dev.getId());
		if(childDevs != null){
			IntObjectHashMap<SignalInfo> signals = SignalLocalCache.getSignalsByDevId(dev.getId());
			if(signals != null){
				for(int i=0;i<childDevs.size();i++){
					DeviceInfo childDev = childDevs.get(i);
					if(childDev.getStationCode() == null){
						//如果设备的station为空，则不入库
						continue;
					}
					String tableName = CustomPropertyConfigurer.getProperties(childDev.getDevTypeId().toString());
					if(tableName == null){
						continue;
					}
					Map<Integer,List<String>> map = UnificationCache.get0(childDev);
					if(map != null && map.size() > 0){
						SingleRecordBean bean = new SingleRecordBean(childDev,tableName,collectTime,collectTime);
						Map<String,Object> datas = new HashMap<String,Object>();
						bean.setMap(datas);
						List<String> columns = new ArrayList<>();
						for(Map.Entry<Integer, List<String>> entry : map.entrySet()) {
							columns.addAll(entry.getValue());
						}
						for(String column : columns){
							if (StringUtils.isBlank(column)) { // 去掉空的column的字段
								continue;
							}
							SignalCacheClient client = (SignalCacheClient) SpringBeanContext.getBean("signalCacheClient");
							Object value = client.get(childDev.getId(), column);
							if(value != null){
								datas.put(column, value);
							}
						
						}
						DevServiceUtils.getData2DbService().save(bean);
					}

				}
			}
		}
	}
}
