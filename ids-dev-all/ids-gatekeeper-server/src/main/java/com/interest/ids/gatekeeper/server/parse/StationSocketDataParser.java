package com.interest.ids.gatekeeper.server.parse;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.utils.FastJsonSerializable;
import com.interest.ids.common.project.utils.SpringBeanContext;
import com.interest.ids.dev.api.service.DevBizService;

/**
 * 电站sokcet流数据修改的操作
 * @author wq
 *
 */
public class StationSocketDataParser implements SocketDataParse {
	private static final Logger log = LoggerFactory.getLogger(StationSocketDataParser.class);
	private DevBizService devBizService;
	@Override
	public void parse(byte[] data) {
		StationInfoM station = FastJsonSerializable.decode(data, StationInfoM.class);
		if (station == null || station.getStationCode() == null) {
			log.warn("no station need update");
			return;
		}
		// 1. 查询设备新虽然后面修改的时候会去做查询，这里查询出来设置为了避免上面修改的被覆盖
		StationInfoM oldStation = getDevBizService().getStationByCode(station.getStationCode());
		if (oldStation == null){ // TODO 考虑新增的情况
			log.warn("no station find,please hand send station info");
			return;
		} else { // 修改
			oldStation.setStationName(station.getStationName());
			oldStation.setInstalledCapacity(station.getInstalledCapacity());
			oldStation.setStationBuildStatus(station.getStationBuildStatus());
			oldStation.setOnlineType(station.getOnlineType());
			oldStation.setFloorSpace(station.getFloorSpace());
			oldStation.setLifeCycle(station.getLifeCycle());
			oldStation.setStationAddr(station.getStationAddr());
			oldStation.setStationDesc(station.getStationDesc());
			oldStation.setContactPeople(station.getContactPeople());
			oldStation.setPhone(station.getPhone());
			if (station.getLatitude() != null) {
				oldStation.setLatitude(station.getLatitude());
			}
			if (station.getLongitude() != null) {
				oldStation.setLongitude(station.getLongitude());
			}
			oldStation.setAreaCode(station.getAreaCode());
			oldStation.setUpdateDate(new Date()); // 设置更新时间
			// 更新电站
			getDevBizService().updateStationInfoMById(oldStation);
		}
		log.info("parse station update success", station);
	}
	private DevBizService getDevBizService() {
		if (devBizService == null) {
			devBizService = (DevBizService) SpringBeanContext.getBean("devBizService");
		}
		return devBizService;
	}
}
