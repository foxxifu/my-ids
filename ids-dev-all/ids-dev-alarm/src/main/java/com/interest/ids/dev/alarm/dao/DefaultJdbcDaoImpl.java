package com.interest.ids.dev.alarm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.stereotype.Service;






/**
 * 
 * @author lhq
 *
 *
 */
@Service("jdbcDao")
public class DefaultJdbcDaoImpl implements JdbcDaoService{
	
	@Resource(name="shardingDataSource")
	private DataSource ds;
	
	public List<Object[]> executeSql(String sql,Object... params) throws Exception{
		Connection conn = null;
		List<Object[]> list = null;
		try {
			conn = ds.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			if(params != null){
				for(int i=0;i<params.length;i++){
					
					ps.setObject(i, params[i]);
				}
			}
		
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			int size = metaData.getColumnCount();
			list = new ArrayList<Object[]>();
			while(rs.next()){
				Object[] data = new Object[size];
				for(int i=0;i<size;i++){
					data[i] = rs.getObject(i);
				}
				list.add(data);
			}
		} finally{
			if(conn != null){
				conn.close();
			}
		}
		return list;
	}
}
