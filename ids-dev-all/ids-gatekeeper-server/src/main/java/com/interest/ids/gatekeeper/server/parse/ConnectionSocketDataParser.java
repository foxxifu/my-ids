package com.interest.ids.gatekeeper.server.parse;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.constant.DeviceConstant.ConnectStatus;
import com.interest.ids.common.project.utils.FastJsonSerializable;
import com.interest.ids.common.project.utils.SpringBeanContext;
import com.interest.ids.redis.client.service.ConnStatusCacheClient;

/**
 * 解析加载文件
 * 
 * @author lhq
 *
 */
public class ConnectionSocketDataParser implements SocketDataParse {
	private static final Logger log = LoggerFactory.getLogger(ConnectionSocketDataParser.class);

	@SuppressWarnings("unchecked")
	@Override
	public void parse(byte[] data) {
		HashMap<Long, String> map = FastJsonSerializable.decode(data, HashMap.class);
		for (Map.Entry<Long, String> entry : map.entrySet()) {
			ConnStatusCacheClient connClient = (ConnStatusCacheClient) SpringBeanContext.getBean("connCacheClient");
			ConnectStatus satus = null;
			if (entry.getValue() != null) { // 转换设备状态的枚举类对象为对应的枚举类
				try {
					satus = ConnectStatus.valueOf(entry.getValue().toUpperCase());
				}catch (Exception e) {
					log.error("cast to devstatus failed status = {}", entry.getValue(), e);
				}
			}
			connClient.put(entry.getKey(), satus);
		}
	}

}
