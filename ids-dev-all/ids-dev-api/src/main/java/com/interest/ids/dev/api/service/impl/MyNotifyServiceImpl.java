package com.interest.ids.dev.api.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.interest.ids.commoninterface.service.notify.MyNotifyService;
import com.interest.ids.dev.api.cache.DevAndVersionInfoForDbCache;

/**
 * 可以通知使用的方法
 * @author wq
 *
 */
@Service
public class MyNotifyServiceImpl implements MyNotifyService{
	private static final Logger logger =LoggerFactory.getLogger(MyNotifyServiceImpl.class);
	
	/**
	 * 这里做清除的事情，是因为修改了设备或者修改了版本，如果这些做了修改，需要通知缓存区改变这些内容，避免数据的丢失，
	 * 否则只有等这个数据的过期时间到了才会去更新，主要是为了及时更新缓存
	 */
	@Override
	public void advice() {
		logger.info("do notify clear mqqt cache");
		DevAndVersionInfoForDbCache.cleanAll();
	}

}
