package com.interest.ids.common.project.mapper.signal;

import com.interest.ids.common.project.bean.signal.SignalVersionInfo;
import com.interest.ids.common.project.utils.CommonMapper;

public interface SignalVersionInfoMapper extends CommonMapper<SignalVersionInfo> {

    Long getParentId();

	void insertAndGetId(SignalVersionInfo pversion);

}