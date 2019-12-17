package com.interest.ids.gatekeeper.server.parse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.utils.FastJsonSerializable;
import com.interest.ids.common.project.utils.SpringBeanContext;
import com.interest.ids.dev.api.service.DevBizService;

/**
 * 设备通过监控获取的流数据处理
 * @author wq
 *
 */
public class DevinfoSocketParser implements SocketDataParse {
	private static final Logger log = LoggerFactory.getLogger(DevinfoSocketParser.class);
	
	private DevBizService devBizService;
	
	@SuppressWarnings("unchecked")
	@Override
	public void parse(byte[] data) {
		DeviceInfo dev = FastJsonSerializable.decode(data, DeviceInfo.class);
		if (dev == null) {
			log.warn("parse data is empty");
			return;
		}
		DeviceInfo oldDev = getDevBizService().getDevById(dev.getId()); // 这个是否考虑使用缓存的数据
		if (oldDev == null) {
			log.warn("no dev devId = " + dev.getId());
			return;
		}
		try {
			// 更新值有10个点
			oldDev.setDevAlias(dev.getDevAlias());
			oldDev.setMatrixId(dev.getMatrixId());
			oldDev.setPhalanxId(dev.getPhalanxId());
			oldDev.setSnCode(dev.getSnCode());
			oldDev.setDevIp(dev.getDevIp());
			oldDev.setDevPort(dev.getDevPort());
			oldDev.setSecondAddress(dev.getSecondAddress());
			if (dev.getLatitude() != null) {
				oldDev.setLatitude(dev.getLatitude());
			}
			if (dev.getLongitude() != null) {
				oldDev.setLongitude(dev.getLongitude());
			}
			oldDev.setIsLogicDelete(dev.getIsLogicDelete());
			// 更新设备
			getDevBizService().updateDevByDevId(oldDev);
		} catch(Exception e) {
			log.error("update dev failed::", e);
		}
		log.info("update dev success");
	}
	private DevBizService getDevBizService() {
		if (devBizService == null) {
			devBizService = (DevBizService) SpringBeanContext.getBean("devBizService");
		}
		return devBizService;
	}
}
