package com.interest.ids.dev.api.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.interest.ids.common.project.utils.SpringBeanContext;


/**
 * 
 * @author lhq
 *
 *
 */
public class JdbcExecutorUtils {
	
	
	public static boolean executeSql(String sql){
		
		DataSource ds = (DataSource) SpringBeanContext.getBean("dataSource");
		Connection conn = null;
		try {
			 conn = ds.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql);
			 ps.execute();
			 return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			
		}finally{
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
