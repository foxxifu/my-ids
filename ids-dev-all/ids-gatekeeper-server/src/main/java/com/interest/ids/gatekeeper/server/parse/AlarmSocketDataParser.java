package com.interest.ids.gatekeeper.server.parse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.interest.ids.common.project.bean.alarm.AlarmM;
import com.interest.ids.common.project.bean.alarm.ClearedAlarmM;
import com.interest.ids.common.project.utils.FastJsonSerializable;
import com.interest.ids.common.project.utils.SpringBeanContext;
import com.interest.ids.dev.api.service.DevBizService;

/**
 * 告警解析器
 * 
 * @author lhq
 *
 */
public class AlarmSocketDataParser implements SocketDataParse {

	private static final Logger log = LoggerFactory
			.getLogger(AlarmSocketDataParser.class);

	@Override
	public void parse(byte[] data) {

		AlarmM alarm = FastJsonSerializable.decode(data, AlarmM.class);
		DevBizService service = (DevBizService) SpringBeanContext
				.getBean("devBizService");
		ClearedAlarmM clearAlarm = new ClearedAlarmM();
		BeanUtils.copyProperties(alarm, clearAlarm);

		try {
			// 保持幂等
			if (alarm.getRecoverTime() != null) {
				service.recoverAlarm(alarm, alarm, clearAlarm);
			} else {

				service.newAlarm(alarm, clearAlarm);
			}
		} catch (Exception e) {
			log.error("gatekeeper save alarm error", e);
		}

	}
}
