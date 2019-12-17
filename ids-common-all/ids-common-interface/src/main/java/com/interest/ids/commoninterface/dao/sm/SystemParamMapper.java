package com.interest.ids.commoninterface.dao.sm;

import com.interest.ids.common.project.bean.sm.SystemParam;

public interface SystemParamMapper {
	int saveParam(SystemParam param);

	SystemParam getSystemParam();

	int updateParam(SystemParam param);
}
