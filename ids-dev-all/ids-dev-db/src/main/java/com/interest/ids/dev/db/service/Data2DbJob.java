package com.interest.ids.dev.db.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.signal.SignalVersionInfo;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.mapper.signal.DevInfoMapper;
import com.interest.ids.dev.api.cache.DevAndVersionInfoForDbCache;
import com.interest.ids.dev.api.utils.DevServiceUtils;
import com.interest.ids.dev.db.job.MqttDevice2DbRunnable;
import com.interest.ids.dev.db.job.MutiDevice2DbRunnable;
import com.interest.ids.dev.db.manager.pool.DevDbPool;
import com.interest.ids.redis.client.service.SignalCacheClient;

@Component
public class Data2DbJob {
	
	private static final Logger log = LoggerFactory.getLogger(Data2DbJob.class);
	
	@Autowired
	private DevInfoMapper devInfoMapper;
	@Autowired
    private SignalCacheClient signalCacheClient;
	//5分钟一次
	@Scheduled(cron = "0 0/5 * * * ?")
	public void execute(){
		log.info("data 2 db task");
		//时间做统一处理
		final long time = System.currentTimeMillis()/10000 * 10000;
		
		List<DeviceInfo> multDeivces = 
				//DevServiceUtils.getDevService().getDevicesByLinkedHostAndType(NetUtils.getLocalIp(), DevTypeConstant.MULTIPURPOSE_DEV_TYPE);
				DevServiceUtils.getDevService().getDcDevices();
		if(multDeivces != null && multDeivces.size() > 0){
			for(int i=0;i<multDeivces.size();i++){
				log.info("dc is .."+multDeivces);
				DevDbPool.getPool().execute(new MutiDevice2DbRunnable(multDeivces.get(i),time));
			}
		}
		// 查询mqtt的版本信息,使用缓存，如果缓存没有，就查询数据库
		List<SignalVersionInfo> mqttVersionList = DevAndVersionInfoForDbCache.getVersionList(DevTypeConstant.MQTT);
		if(!CollectionUtils.isEmpty(mqttVersionList)) { // 如果有mqtt协议的版本信息
			for(SignalVersionInfo s : mqttVersionList){
				DevDbPool.getPool().execute(new MqttDevice2DbRunnable(s,time));
			}
		}
	}
	/**
	 * 每一次晚上23:56分将所有组串式逆变器的当日发电量清0
	 */
	@Scheduled(cron = "0 56 23 * * ?")
	public void execute2(){
		Map<String, Object> dayCapacityMap = new HashMap<>();
		Example example = new Example(DeviceInfo.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("devTypeId", 1);
		criteria.andEqualTo("isLogicDelete", 0);
		List<DeviceInfo> deviceInfos = devInfoMapper.selectByExample(example);
		if(null != deviceInfos){
			for(DeviceInfo devInfo : deviceInfos){
				String catchKey = signalCacheClient.generateKey(devInfo.getId(), "day_capacity");
				dayCapacityMap.put(catchKey, 0.00d);
			}
			signalCacheClient.batchPut(dayCapacityMap);
		}
	}
}
