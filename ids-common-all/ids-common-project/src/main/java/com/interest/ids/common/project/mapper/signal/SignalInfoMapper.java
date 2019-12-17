package com.interest.ids.common.project.mapper.signal;

import com.interest.ids.common.project.bean.signal.SignalInfo;
import com.interest.ids.common.project.utils.CommonMapper;


public interface SignalInfoMapper extends CommonMapper<SignalInfo> {

	/**
	 * 通过更新点表的modelId
	 */
	void updateSignalModelId();

}