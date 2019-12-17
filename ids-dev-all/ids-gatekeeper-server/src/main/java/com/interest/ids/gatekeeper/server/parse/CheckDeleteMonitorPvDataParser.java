package com.interest.ids.gatekeeper.server.parse;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.utils.FastJsonSerializable;
import com.interest.ids.common.project.utils.SpringBeanContext;
import com.interest.ids.commoninterface.service.station.StationForDevProjectService;

/**
 * 检测传递过来的pv信息是否需要删除
 * @author wq
 *
 */
public class CheckDeleteMonitorPvDataParser implements SocketDataParse {

	private static final Logger log = LoggerFactory.getLogger(CheckDeleteMonitorPvDataParser.class);
	
	private StationForDevProjectService stationForDevProjectService;
	
	@SuppressWarnings("unchecked")
	@Override
	public void parse(byte[] data) {
		try {
			HashMap<String, Object> map = FastJsonSerializable.decode(data, HashMap.class);
			if (map == null) {
				log.warn("has error data::", map);
				return;
			}
			int type = (Integer) map.get("type");
			if (type == 1){ // 1：代表检测的是ids_pv_capacity_t
				if (map.get("id") == null || map.get("deviceId") == null || map.get("stationCode") == null){
					log.warn("has error data::", map);
					return;
				}
				// 检查并删除数据
				getStationForDevProjectService().deleteMonitorPvConfig(map);
			} else if (type == 2) { // 2：代表检测的是ids_center_vert_detail_t
				if (map.get("ids") == null || map.get("deviceId") == null ) {
					log.warn("has error data::", map);
					return;
				}
				getStationForDevProjectService().deleteMonitorCenterPvConfig(map);
			}
			
		} catch (Exception e) {
			log.error("check pv deleteable has exception::", e);
		}
	}
	
	private StationForDevProjectService getStationForDevProjectService() {
		if (stationForDevProjectService == null) {
			stationForDevProjectService = (StationForDevProjectService) SpringBeanContext.getBean("stationForDevProjectService");
		}
		return stationForDevProjectService;
	}

}
