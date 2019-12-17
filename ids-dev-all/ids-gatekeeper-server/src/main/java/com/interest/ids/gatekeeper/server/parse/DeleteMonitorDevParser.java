package com.interest.ids.gatekeeper.server.parse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.interest.ids.common.project.utils.FastJsonSerializable;
import com.interest.ids.common.project.utils.SpringBeanContext;
import com.interest.ids.dev.api.service.DevBizService;

/**
 * 删除监控设备的方法，这里主要是因为监控删除了点表，将数据物理删除了，这里就需要将这些设备删除
 * @author wq
 *
 */
public class DeleteMonitorDevParser implements SocketDataParse {
	
	private static final Logger log = LoggerFactory.getLogger(DeleteMonitorDevParser.class);
	
	private DevBizService devBizService;

	@Override
	public void parse(byte[] data) {
		String devIdsStr = FastJsonSerializable.decode(data, String.class); 
		if (StringUtils.isEmpty(devIdsStr)) {
			log.warn("no get dev info to delete");
			return;
		}
		// 删除设备信息
		getDevBizService().deleteMonitorDevByIds(devIdsStr);
		// 删除是比较重要的信息，在删除的时候就需要打印对应的日志，方便误删的排查
		log.info("delete monitor dev success: devIds = {}", devIdsStr);
	}
	
	private DevBizService getDevBizService() {
		if (devBizService == null) {
			devBizService = (DevBizService) SpringBeanContext.getBean("devBizService");
		}
		return devBizService;
	}

}
