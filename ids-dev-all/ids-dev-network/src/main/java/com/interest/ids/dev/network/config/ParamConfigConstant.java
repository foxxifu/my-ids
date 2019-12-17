package com.interest.ids.dev.network.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * 
 * @author lhq
 *
 *
 */
public class ParamConfigConstant {
	
	//104协议ACK失败的时候关闭通道，默认为true
	public static boolean ackTimeoutClose = true;
	//104协议总召间隔时间，默认为15分钟
	public static Long wholeCallPeriod = 900000L; 
	
	public static int default_data_expire_time;
	
	
	
	static{
		FileInputStream fis = null;
		try {
			Properties p = new Properties();
			URL url = Thread.currentThread().getContextClassLoader().getResource("config/dev_network.properties");
			fis = new FileInputStream(url.getFile());
			p.load(fis);
			wholeCallPeriod = Long.valueOf(p.getProperty("whole.call.period"));
			ackTimeoutClose = Boolean.valueOf(p.getProperty("ack.timeout.close"));
			//总召时间的两倍
			default_data_expire_time = (int) (wholeCallPeriod/1000 * 2);
		}catch(Exception e){
		   throw new RuntimeException(e);
		}finally{
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					
				}
			}
		}
		
	}
}
