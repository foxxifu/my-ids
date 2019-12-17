package com.interest.ids.commoninterface.service.sm;

import com.interest.ids.common.project.bean.sm.SystemParam;

public interface SystemParamService 
{
	int saveParam(SystemParam param);

	SystemParam getSystemParam();

	int updateParam(SystemParam param);
}
