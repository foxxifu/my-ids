package com.interest.ids.dev.db.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.signal.SignalVersionInfo;
import com.interest.ids.common.project.constant.DeviceConstant.ConnectStatus;
import com.interest.ids.common.project.spring.propholder.CustomPropertyConfigurer;
import com.interest.ids.common.project.utils.SpringBeanContext;
import com.interest.ids.dev.api.bean.SingleRecordBean;
import com.interest.ids.dev.api.cache.DevAndVersionInfoForDbCache;
import com.interest.ids.dev.api.localcache.UnificationCache;
import com.interest.ids.dev.api.utils.DevServiceUtils;
import com.interest.ids.redis.client.service.SignalCacheClient;


/**
 * MQTT设备入口的线程
 * @author wq
 *
 *
 */
public class MqttDevice2DbRunnable implements Runnable{
	
	private static final Logger log = LoggerFactory.getLogger(MqttDevice2DbRunnable.class);

	private long collectTime; 
	
	private SignalVersionInfo version;
	
	public MqttDevice2DbRunnable(SignalVersionInfo version,long collectTime){
		this.version = version;
		this.collectTime = collectTime;
	}
	
	
	@Override
	public void run() {
		// 1.查询mqtt的版本对应的设备,使用缓存，缓存里面考虑了如果查询不到就去数据库中查询，数据每隔一段时间数据会过期，数据库中查询
		List<DeviceInfo> childDevs = DevAndVersionInfoForDbCache.devDevList(version.getSignalVersion());
		if(CollectionUtils.isEmpty(childDevs)){ // 设备不存在
			log.info("no mqtt device,versionId = {}, versionCode = {}", version.getId(), version.getSignalVersion());
			return;
		}
		// 2.对每一个设备循环遍历去入库
		for(DeviceInfo childDev : childDevs){
			if(childDev.getStationCode() == null){
				//如果设备的station为空，则不入库
				log.info("mqtt dev's station is null, devId = {}, devName = {}", childDev.getId(), childDev.getDevName());
				continue;
			}
			// 如果设备连接状态不是连接的，不入库对应的数据
			if(ConnectStatus.CONNECTED != DevServiceUtils.getConnStatusCacheClient().get(childDev.getId())) {
				log.info("mqtt dev is not connected, devId = {}, devName = {}", childDev.getId(), childDev.getDevName());
				continue;
			}
			// 获取入库的表明
			String tableName = CustomPropertyConfigurer.getProperties(childDev.getDevTypeId().toString());
			if(tableName == null){
				continue;
			}
			// 获取入库的归一化配置
			Map<Integer,List<String>> map = UnificationCache.get0(childDev);
			if(map != null && map.size() > 0){
				SingleRecordBean bean = new SingleRecordBean(childDev,tableName,collectTime,collectTime); // 组装入库的数据
				Map<String,Object> datas = new HashMap<String,Object>();
				bean.setMap(datas);
				
				List<String> columns = new ArrayList<>();
				for(Map.Entry<Integer, List<String>> entry : map.entrySet()) {
					columns.addAll(entry.getValue());
				}
				for(String column : columns){
					if (StringUtils.isBlank(column)) { // 去掉空的数据
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
