package com.interest.ids.dev.api.localcache;

import com.interest.ids.common.project.bean.kpi.StatisticsScheduleTaskM;
import com.interest.ids.common.project.cache.LruCache;
import com.interest.ids.common.project.utils.SpringBeanContext;
import com.interest.ids.dev.api.service.DevDbService;

/**
 * 
 * @author lhq
 *
 *
 */
public class StatisDataCache extends LruCache{
	
	
	public StatisDataCache() {
		//按照一种设备类型每小时一条数据，那么4种设备12小时48条数据加上天数据总共52条数据，和map做一个计算得出192，向下取整数150
		super(150);
	}
	
	public StatisticsScheduleTaskM get(StatisticsScheduleTaskM param){
		String key = generateKey(param);
		if(key != null){
			StatisticsScheduleTaskM p = (StatisticsScheduleTaskM) super.get(key);
			if(p == null){
				DevDbService service = (DevDbService) SpringBeanContext.getBean("devDbService");
				StatisticsScheduleTaskM dbParam = service.getCurrentHourData(param);
				if(dbParam != null){
					super.put(key, dbParam);
					return dbParam;
				}
				return null;
			}else{
				return p;
			}
		}
		return null;
	}
	
	
	private String generateKey(StatisticsScheduleTaskM param){
		if(param != null){
			StringBuilder key = new StringBuilder(param.getStationCode());
			key.append(param.getBusiType()).append(param.getStatDate()).
			    append(param.getStatType()).append(param.getDealState());
			return key.toString();
		}
		return null;
	}
}
