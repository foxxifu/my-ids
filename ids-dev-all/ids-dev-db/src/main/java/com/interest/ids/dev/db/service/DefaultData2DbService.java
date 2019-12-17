package com.interest.ids.dev.db.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.interest.ids.common.project.bean.kpi.StatisticsScheduleTaskM;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.utils.SpringBeanContext;
import com.interest.ids.dev.api.bean.SingleRecordBean;
import com.interest.ids.dev.api.localcache.StatisDataCache;
import com.interest.ids.dev.api.service.Data2DbService;
import com.interest.ids.dev.api.service.DevDbService;
import com.interest.ids.dev.db.utils.DevDbutils;


@Service("data2DbService")
public class DefaultData2DbService implements Data2DbService{

	private static final Logger log = LoggerFactory.getLogger(DefaultData2DbService.class);
	
	protected static StatisDataCache cache = new StatisDataCache();
	
	public void save(SingleRecordBean bean){
		StringBuilder sql = new StringBuilder("insert into ");
		sql.append(bean.getTableName()).append(" ");
		
		StringBuilder prop = new StringBuilder("(collect_time,station_code,dev_id,dev_name");
		
		StringBuilder values = new StringBuilder();
		
		values.append("(").append(bean.getDataTime()).append(",'")
			  .append(bean.getDev().getStationCode()).append("','")
			  .append(bean.getDev().getId()).append("','")
			  .append(bean.getDev().getDevName()).append("'");
		
		for(Map.Entry<String, Object> entry : bean.getMap().entrySet()){
			String column = entry.getKey();
			Object value = entry.getValue();
			prop.append(",").append(column);
			values.append(",'").append(value).append("'");
		}
		prop.append(")");
		values.append(")");
		
		sql.append(prop).append(" values ").append(values);

		DataSource ds = (DataSource) SpringBeanContext.getBean("shardingDataSource");
		Connection conn = null;
		try {
			 conn = ds.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql.toString());
			 ps.execute();
		} catch (Exception e) {
			log.error("execute sql error ",e);
		}finally{
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {

				}
			}
		}
		log.debug("execute sql {}",sql.toString());
		
		try {
			afterSave(bean);
		} catch (Exception e) {
			log.error("",e);
		}
		
	}
	


	
	public void afterSave(SingleRecordBean bean) {
		
		DeviceInfo dev = bean.getDev();
		String typeString = DevDbutils.getDevTypeString(dev.getDevTypeId());
		if(typeString == null){
			return;
		}
		long time = DevDbutils.getHourByMills(bean.getCollectTime());
		StatisticsScheduleTaskM param = new StatisticsScheduleTaskM();
		param.setStationCode(dev.getStationCode());
		param.setBusiType(typeString);
		param.setStatDate(time);
		param.setDealState(0);
		//先查找0的    0：小时的任务
		param.setStatType(0);
		getStatisData(param);
		//日计算           1： 日的任务
		param.setStatDate(DevDbutils.getDayStartMills(bean.getCollectTime()));
		param.setStatType(1);
		getStatisData(param);
	}
	
	private void getStatisData(StatisticsScheduleTaskM param){
		StatisticsScheduleTaskM p = cache.get(param);
		//这里要加锁，不然回引起并发问题
		if(p == null){
			
			DevDbService service = (DevDbService) SpringBeanContext.getBean("devDbService");
			service.saveStatis(param);
		}
	}

	

}
