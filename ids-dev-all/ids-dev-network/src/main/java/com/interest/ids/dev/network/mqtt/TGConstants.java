/**
 * generated by Platform Generator
 * @generated 2018-08-24 15:10
 */
package com.interest.ids.dev.network.mqtt;


import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class TGConstants {

    public final static String MQTT_TG_SERVER_CLIENT_ID = "tgServer";
    public final static byte MQTT_HEAD = 0x53;
    public final static Properties p;
    public static Map<Long,MqttCommand> mqttCommandMap=new ConcurrentHashMap<>();
    static {
		FileInputStream fis = null;
		p = new Properties();
		try
		{
		URL url = Thread.currentThread().getContextClassLoader().getResource("config/dev_network.properties");
		fis = new FileInputStream(url.getFile());
		p.load(fis);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


}