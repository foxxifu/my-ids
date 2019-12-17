package com.interest.ids.dev.starter.web;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.interest.ids.common.project.utils.SpringBeanContext;
import com.interest.ids.dev.api.service.Iec104Service;
import com.interest.ids.dev.network.mqtt.MQTTClientStarter;
import com.interest.ids.dev.network.mqtt.TGConstants;
import com.interest.ids.dev.network.mqtt.TGMQTTSubscriber;


public class DevMain {

	private static final Logger log = LoggerFactory.getLogger(DevMain.class);

	public static void main(String[] args) throws Exception {
		log.info("start dev main");
		String osType = System.getProperty("os.name");
		if(osType.startsWith("Linux")){
			new ClassPathXmlApplicationContext("spring-jetty-linux.xml");
		}else{
			new ClassPathXmlApplicationContext("spring-jetty.xml");
		}
		
		/*SignalCacheClient client = (SignalCacheClient) SpringBeanContext.getBean("signalCacheClient");
		Set<String> set = new HashSet<>();
		set.add("mppt_3_cap");
		set.add("ab_u");
		set.add("day_cap");
		Map<String,Object> map = client.batchGet(4L, set);
		//Object data = client.get(4L, "mppt_3_cap");
		System.out.println(map);*/
		initDc();
		initMQTT();
		log.info("dev start success");
	}
	
	/*private static void testShardingDb() throws Exception{
		long time = System.currentTimeMillis();
		String sql = "insert into ids_inverter_string_t (collect_time,station_code,device_id,busi_code) values ('"+time+"','123456',12,'569845')";
		DataSource ds = (DataSource) SpringBeanContext.getBean("shardingDataSource");
		Connection c = ds.getConnection();
		PreparedStatement ps = c.prepareStatement(sql);
		ps.execute();
	}*/
	
	private static void initDc(){
		Iec104Service iecService = (Iec104Service) SpringBeanContext.getBean("iec104Service");
		iecService.initDc();
	}
	//启动mqtt订阅和注册
	private static void initMQTT() {
		MQTTClientStarter mqttClientStarter=(MQTTClientStarter)SpringBeanContext.getBean("mqttClientStarter");
		mqttClientStarter.setMqttConnectionString(TGConstants.p.getProperty("mqtt.host"))
				.setClientId(TGConstants.MQTT_TG_SERVER_CLIENT_ID)
				.setUserName("admin")
				.setPassword("admin@public")
				.initMQTTClient();
		mqttClientStarter.registerSubscriber(new TGMQTTSubscriber());
	}
}
