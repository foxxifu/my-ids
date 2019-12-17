package com.interest.ids.dev.network.handler;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import tk.mybatis.mapper.entity.Example;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.mapper.signal.DevInfoMapper;
import com.interest.ids.dev.network.mqtt.MQTTUtil;

@Component
public class MqttCollectJob {

	
	private static final Logger log = LoggerFactory.getLogger(MqttCollectJob.class);

	@Resource
	private DevInfoMapper devMapper;
	//5分钟一次
	@Scheduled(cron = "0 0/2 * * * ?")
	public void execute(){
		log.info("mqtt collect task start");
		Example ex = new Example(DeviceInfo.class);
		Example.Criteria criteria = ex.createCriteria();
		criteria.andEqualTo("protocolCode", DevTypeConstant.MQTT);
		//除了逻辑删除之外的设备
		criteria.andEqualTo("isLogicDelete",0);
		List<DeviceInfo> devs = devMapper.selectByExample(ex);
		MQTTUtil mqttUtil=new MQTTUtil();
		log.info("mqtt parent dev size is: "+devs.size());
//		for (DeviceInfo dev:devs) {
//			log.info("mqtt dev size is: "+devs.size());
//			for (DeviceInfo deviceInfo : devs) {
//				mqttUtil.publishDev(deviceInfo);
//			}
//		}
		for (DeviceInfo deviceInfo : devs) {
			try {
				mqttUtil.publishDev(deviceInfo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		log.info("mqtt collect task end");
	}


}
