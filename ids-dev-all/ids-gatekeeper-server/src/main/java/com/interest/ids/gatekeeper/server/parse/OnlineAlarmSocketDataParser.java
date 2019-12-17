package com.interest.ids.gatekeeper.server.parse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.bean.alarm.AlarmM;
import com.interest.ids.common.project.mapper.signal.AlarmMapper;
import com.interest.ids.common.project.utils.FastJsonSerializable;
import com.interest.ids.common.project.utils.SpringBeanContext;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 实时告警清除的方法
 * @author wq
 *
 */
public class OnlineAlarmSocketDataParser implements SocketDataParse {
	private static final Logger log = LoggerFactory.getLogger(OnlineAlarmSocketDataParser.class);
	// 告警的mapper
	private AlarmMapper alarmMapper;
	
	@SuppressWarnings("unchecked")
	@Override
	public void parse(byte[] data) {
		List<Long> alarmIds = FastJsonSerializable.decode(data, ArrayList.class);
		if(alarmIds != null && alarmIds.size() > 0) { // 删除实时告警里面的内容
			Example infoExample = new Example(AlarmM.class);
			Criteria c = infoExample.createCriteria();
			c.andIn("id", alarmIds);
			int count = getAlarmMapper().deleteByExample(infoExample);
			log.info("count:" +  count);
		}
	}

	private AlarmMapper getAlarmMapper() {
		if (alarmMapper == null) {
			alarmMapper = (AlarmMapper) SpringBeanContext.getBean("alarmMapper");
		}
		return alarmMapper;
	}
}
