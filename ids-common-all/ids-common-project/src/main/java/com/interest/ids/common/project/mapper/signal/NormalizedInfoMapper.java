package com.interest.ids.common.project.mapper.signal;

import java.util.List;

import com.interest.ids.common.project.bean.signal.NormalizedInfo;
import com.interest.ids.common.project.utils.CommonMapper;

public interface NormalizedInfoMapper extends CommonMapper<NormalizedInfo>{
	
	List<NormalizedInfo> getInfoByDeviceType(Integer deviceType);

}
