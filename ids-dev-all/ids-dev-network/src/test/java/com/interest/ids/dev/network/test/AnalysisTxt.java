package com.interest.ids.dev.network.test;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;


public class AnalysisTxt {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		FileInputStream fis = new FileInputStream("F:/music.txt");
		byte[] data = new byte[fis.available()];
		fis.read(data);
		String txt = new String(data);
		
		int index = 1;
		String fix1 = "<span class=\"txt\">";
		String fix2 = "a href=\"";
		String fix3 = "<b title=\"";
		String fix4 = "<span title=\"";
		String tt = "http://music.163.com/song?id=";
		Map<String,String> map = new HashMap<String,String>();
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/spider","root","root");
		while(index>0){
			int a = txt.indexOf(fix1,index);
			int b = txt.indexOf(fix2,a);
			//int c = txt.indexOf("\"",b);
			int c = txt.indexOf("\">",b);
			String href = txt.substring(b+fix2.length(), c);
			
			int d = txt.indexOf(fix3,c);
			int m = txt.indexOf("\">",d);
			int n = txt.indexOf(fix4,m);
			
			int x = txt.indexOf("\">",n);
			
			String author = txt.substring(n+fix4.length(),x);
			
			String name = txt.substring(d+fix3.length(),m);
			if(map.containsKey(href)){
				break;
			}
			System.out.println(href+"   " +name+"  "+author);
			index = m;
			String id = href.substring(tt.length());
			String sql = "insert into urls_t (url,name,author) values ('"+id+"','"+name+"','"+author+"')";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.execute();
			map.put(href, name);
		}
	}
	
	
	
	static class UrlBean{
		
		private String url;
		
		private String name;
		
		private String author;
		
		public UrlBean(String url, String name, String author) {
			super();
			this.url = url;
			this.name = name;
			this.author = author;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAuthor() {
			return author;
		}

		public void setAuthor(String author) {
			this.author = author;
		}
	}

}
