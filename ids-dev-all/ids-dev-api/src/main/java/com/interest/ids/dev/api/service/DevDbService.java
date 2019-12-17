package com.interest.ids.dev.api.service;

import com.interest.ids.common.project.bean.kpi.StatisticsScheduleTaskM;


/**
 * 
 * @author lhq
 *
 *
 */
public interface DevDbService {
	
	
	
	StatisticsScheduleTaskM getCurrentHourData(StatisticsScheduleTaskM param);
	
	void saveStatis(StatisticsScheduleTaskM param);

}
