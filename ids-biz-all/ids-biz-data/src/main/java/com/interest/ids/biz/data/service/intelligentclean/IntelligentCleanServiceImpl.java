package com.interest.ids.biz.data.service.intelligentclean;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interest.ids.common.project.bean.intelligentclean.IntelligentCleanParamT;
import com.interest.ids.commoninterface.dao.intelligentclean.IntelligentCleanMapper;
import com.interest.ids.commoninterface.service.intelligentclean.IIntelligentCleanService;
/**
 * 智能清洗service实现类
 * 
 * @author claude
 *
 */
@Service("intelligentCleanService")
public class IntelligentCleanServiceImpl implements IIntelligentCleanService {
	
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(IntelligentCleanServiceImpl.class);
	
	@Autowired
	IntelligentCleanMapper intelligentCleanMapper;

	@Override
	public List<IntelligentCleanParamT> getIntelligentCleanParams(String stationCode) {
		List<IntelligentCleanParamT> params = null;
		if(!StringUtils.isEmpty(stationCode)){
			params = this.intelligentCleanMapper.getStationIntelligentCleanParams(stationCode);
		}
		if(params == null || params.size() == 0){
			params = this.intelligentCleanMapper.getSystemIntelligentCleanParams();
		}
		return params;
	}

	@Override
	public List<Map<String, Long>> getStationInverters(String stationCode) {
		return this.intelligentCleanMapper.getStationInverters(stationCode);
	}

}
