package com.interest.ids.dev.alarm.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;


@Component
public class AnalysisDao {
	
	@Resource(name="jdbcDao")
	private JdbcDaoService daoService;
	
	public List<Object[]> getCombinerMaxPv(String stationCode,long startTime,long endTime){
		
		String sql = getMaxCombinerSql();
		try {
			List<Object[]> list = daoService.executeSql(sql,stationCode,startTime,endTime);
			return list;
		} catch (Exception e) {
			
		}
		return null;
	}
	
	public List<Object[]> getMaxPvData(String stationCode,long startTime,long endTime){
		
		String sql = getMaxPvDataSql().toString();
		try {
			List<Object[]> objs = daoService.executeSql(sql);
			return objs;
		} catch (Exception e) {
			
		}
		return null;
	}
	
	public List<Object[]> getActivePower(String stationCode,long startTime,long endTime){
		String sql = getActivePowerSql().toString();
		try {
			List<Object[]> objs = daoService.executeSql(sql);
			return objs;
		} catch (Exception e) {
			
		}
		return null;
	}
	
	
	
	public String getActivePowerSql(){
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT inv.device_id, inv.active_power FROM ( SELECT t.device_id, t.active_power FROM ids_inverter_string_data_t t WHERE t.station_code = ?")
		   .append(" AND t.collect_time >? AND t.collect_time <=?   ORDER BY t.collect_time DESC ) inv GROUP BY inv.device_id");
		return sql.toString();
	}
	
	public String getMaxCombinerSql(){
		
		StringBuilder sql = new StringBuilder();
		sql.append("select combiner.device_id,max(combiner.dc_i1),max(combiner.dc_i2),max(combiner.dc_i3),max(combiner.dc_i4),max(combiner.dc_i5),max(combiner.dc_i6),max(combiner.dc_i7),max(combiner.dc_i8),");
		sql.append("max(combiner.dc_i9),max(combiner.dc_i10),max(combiner.dc_i11),max(combiner.dc_i12),max(combiner.dc_i13),max(combiner.dc_i14),max(combiner.dc_i15),max(combiner.dc_i16),max(combiner.dc_i17),");
		sql.append("max(combiner.dc_i18),max(combiner.dc_i19),max(combiner.dc_i20),max(combiner.photc_u),min(combiner.dc_i1),min(combiner.dc_i2),min(combiner.dc_i3),");
		sql.append("min(combiner.dc_i4),min(combiner.dc_i5),min(combiner.dc_i6),min(combiner.dc_i7),min(combiner.dc_i8),min(combiner.dc_i9),min(combiner.dc_i10),min(combiner.dc_i11),");
		sql.append("min(combiner.dc_i12),min(combiner.dc_i13),min(combiner.dc_i14),min(combiner.dc_i15),min(combiner.dc_i16),min(combiner.dc_i17),min(combiner.dc_i18),min(combiner.dc_i19),");
		sql.append("min(combiner.dc_i20) from ids_combiner_dc_data_t combiner where combiner.station_code=? and combiner.collect_time>? and combiner.collect_time <=? ");
		sql.append("GROUP BY combiner.device_id");
		return sql.toString();
	}
	
	public String getMaxPvDataSql(){
		StringBuilder str = new StringBuilder();
		str.append("select inv.device_id,max(inv.pv1_i),max(inv.pv2_i),max(inv.pv3_i),max(inv.pv4_i),max(inv.pv5_i),max(inv.pv6_i),max(inv.pv7_i),")
		   .append("max(inv.pv8_i),max(inv.pv9_i),max(inv.pv10_i),max(inv.pv11_i),max(inv.pv12_i),max(inv.pv13_i),max(inv.pv14_i),max(inv.pv1_u),max(inv.pv2_u),")
		   .append("max(inv.pv3_u),max(inv.pv4_u),max(inv.pv5_u),max(inv.pv6_u),max(inv.pv7_u),max(inv.pv8_u),max(inv.pv9_u),max(inv.pv10_u),max(inv.pv11_u),")
		   .append("max(inv.pv12_u),max(inv.pv13_u),max(inv.pv14_u) from iems_inverter_string_t inv where ")
		   .append("inv.station_code=? and inv.collect_time>? and inv.collect_time <= ? group by inv.device_id");
		
		return str.toString();
	}

}
