package com.interest.ids.dev.db.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;




/**
 * 
 * @author lhq
 *
 *
 */
public class Db2UnifiedSignals {
	
	
	public static void main(String[] args) throws Exception {
		
		
		Reader reader = new FileReader(new File("F:/unified.txt"));
		BufferedReader br = new BufferedReader(reader);
		String str = null;
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ids","root","root");
		
		
		while((str = br.readLine())!=null){
			String sql = "select * from ids_signal_info_t where signal_instance_name like ";
			
			String sql2 = "select * from ids_unification_model_t where unification_display_name like ";
			
			
			String[] data = str.split("==");
			if(data.length == 1){
				sql += "'%"+data[0]+"%'";
			}else{
				sql += "'%"+data[1]+"%'";
			}
			sql2 += "'%"+data[0]+"%'";
			//System.out.println(sql);
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			PreparedStatement ps2 = conn.prepareStatement(sql2);
			ResultSet rs2 = ps2.executeQuery();
			
			if(rs.next() && rs2.next()){
				
				System.out.println(rs.getObject("signal_instance_name")+"  "+rs.getObject("id")+"  "+rs.getObject("sig_address"));
				System.out.println(rs2.getObject("unification_display_name")+"  "+rs2.getObject("id"));
				Object signalName = rs.getObject("signal_instance_name");
				Object signalAddr = rs.getObject("sig_address");
				String version = "SUN2000_2.0";
				Object sid = rs.getObject("id");
				Object uid = rs2.getObject("id");
				String sql3 = "insert into ids_unification_info_t (siganl_model_id,signal_name,signal_version,signal_address,unification_signal_id) values" +
						"('"+sid+"','"+signalName+"','"+version+"','"+signalAddr+"','"+uid  +"')";
				PreparedStatement ps3 = conn.prepareStatement(sql3);
				ps3.executeUpdate();
			}
		}
	}

}
