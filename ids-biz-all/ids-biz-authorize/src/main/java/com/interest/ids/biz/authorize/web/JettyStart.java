package com.interest.ids.biz.authorize.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.interest.ids.common.project.utils.SpringBeanContext;
import com.interest.ids.redis.client.JedisClient;

public class JettyStart {

    private static final Logger log = LoggerFactory.getLogger(JettyStart.class);

    @SuppressWarnings({ "resource", "unused" })
	public static void main(String[] args) throws Exception {

        log.info("start main");

        new ClassPathXmlApplicationContext("spring-jetty.xml");
        JedisClient client = (JedisClient) SpringBeanContext
                .getBean("jedClient");
        // client.getJedis().set("zhangsan", "lisi");
        startModbus();
    }

    public static void startModbus() {
        /*
         * ModbusServer server = new ModbusServer(new NettyServerConfig()); try
         * { server.start(); } catch (Exception e) {
         * 
         * }
         */
    }

}
