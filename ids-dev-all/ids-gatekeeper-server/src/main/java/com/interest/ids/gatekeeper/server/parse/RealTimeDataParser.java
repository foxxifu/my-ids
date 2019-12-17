package com.interest.ids.gatekeeper.server.parse;

import java.util.HashMap;
import java.util.Map;

import com.interest.ids.common.project.utils.FastJsonSerializable;
import com.interest.ids.common.project.utils.SpringBeanContext;
import com.interest.ids.redis.client.service.SignalCacheClient;

/**
 * 
 * @author lhq
 *
 *
 */
public class RealTimeDataParser implements SocketDataParse {

	@SuppressWarnings("unchecked")
	@Override
	public void parse(byte[] data) {

		SignalCacheClient client = (SignalCacheClient) SpringBeanContext
				.getBean("signalCacheClient");
		HashMap<Object, Map<String, Object>> map = FastJsonSerializable.decode(
				data, HashMap.class);
		for (Map.Entry<Object, Map<String, Object>> entry : map.entrySet()) {

			Object key = entry.getKey();
			Map<String, Object> values = entry.getValue();
			Long id = null;
			if (key instanceof Integer) {
				id = ((Integer) key).longValue();

			}
			if (key instanceof Long) {
				id = (Long) key;
			}
			if (key != null) {
				Map<String, Object> putDats = new HashMap<String, Object>();
				for (Map.Entry<String, Object> entr : values.entrySet()) {
					String column = entr.getKey();
					String cacheKey = client.generateKey(id, column);
					putDats.put(cacheKey, entr.getValue());
				}
				if (putDats.size() > 0) {
					client.batchPut(putDats);
				}
			}

		}
	}

}
