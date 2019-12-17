package com.interest.ids.dev.db.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Arrays;


public class Db2SignalsInfo {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Reader reader = new FileReader(new File("F:/2.txt"));
		BufferedReader br = new BufferedReader(reader);
		String str = null;
		
		
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ids","root","root");
		
		
		while((str = br.readLine())!=null){
			String[] datas = str.split("	");
			System.out.println(Arrays.toString(datas)+"  "+datas.length);
			String sql = "insert into ids_signal_info_t (signal_instance_name,device_id,signal_unit,signal_type,data_type,sig_address,reg_num" +
			") values ('"+datas[0]+"','1','"+datas[3]+"','1','1','"+datas[5]+"','"+datas[6]+"')";
			System.out.println(sql);
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.execute();
		}

	}

}
