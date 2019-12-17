package com.interest.ids.dev.api.service;

import com.interest.ids.common.project.bean.device.DcConfig;


/**
 * 
 * @author lhq
 *
 *
 */
public interface Iec104Service {
	
	
	void initDc();
	
	/**
	 * 新增配置
	 */
	void newConfig(DcConfig config);
	
	/**
	 * 更新配置
	 */
	void updateConfig(DcConfig config);

}
