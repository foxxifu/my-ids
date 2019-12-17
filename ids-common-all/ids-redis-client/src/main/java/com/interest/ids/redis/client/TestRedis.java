package com.interest.ids.redis.client;

import com.interest.ids.redis.client.service.SignalCacheClient;

import org.springframework.context.support.ClassPathXmlApplicationContext;


public class TestRedis {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring/spring_redis.xml");
		SignalCacheClient c = (SignalCacheClient) ctx.getBean("signalCacheClient");
	   
	    
	   c.put(12L, 23L, 3.098);
	   
	   Object o = c.get(12L, 23L);
	   System.out.println(o);
	}

}
