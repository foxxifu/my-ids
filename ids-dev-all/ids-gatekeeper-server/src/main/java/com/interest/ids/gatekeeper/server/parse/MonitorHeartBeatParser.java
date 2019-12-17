package com.interest.ids.gatekeeper.server.parse;

import com.interest.ids.common.project.utils.FastJsonSerializable;
import com.interest.ids.gatekeeper.server.manager.MonitorHeartBeatJob;

/**
 * 监控心跳数据的检测
 * @author wq
 *
 */
public class MonitorHeartBeatParser implements SocketDataParse {

	@Override
	public void parse(byte[] data) {
		String stationCode = FastJsonSerializable.decode(data, String.class);
		if (stationCode == null) { // 没有获取到电站信息
			return;
		}
		MonitorHeartBeatJob.put(stationCode); // 修改当前监控电站的当前接收到的连接时间
	}

}
