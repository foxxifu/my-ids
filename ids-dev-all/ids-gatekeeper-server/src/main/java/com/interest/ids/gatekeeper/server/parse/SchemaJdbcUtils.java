package com.interest.ids.gatekeeper.server.parse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.interest.ids.common.project.spring.propholder.CustomPropertyConfigurer;


/**
 * 
 * @author lhq
 *
 *
 */
public class SchemaJdbcUtils {
	
	static{
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
	}
	
	public static List<String> getColumns(String table) {
		
		//gatekeeper.jdbc.url=jdbc:mysql://127.0.0.1:3306/ids

		String url = CustomPropertyConfigurer.getProperties("gatekeeper.jdbc.url");
		String userName = CustomPropertyConfigurer.getProperties("gatekeeper.mysql.user");
		String pwd = CustomPropertyConfigurer.getProperties("gatekeeper.mysql.pwd");
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url,userName,pwd);
			String sql = "select COLUMN_NAME from 'COLUMNS' where table_name='"+table+"' order by ORDINAL_POSITION";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			List<String> list = new ArrayList<String>();
			while(rs.next()){
				list.add(rs.getString(0));
			}
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
		
	}

}
