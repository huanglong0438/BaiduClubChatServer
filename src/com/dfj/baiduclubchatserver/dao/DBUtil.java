package com.dfj.baiduclubchatserver.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBUtil {
	private static DBUtil dbutil;
	private DBUtil(){
		
	}
	public synchronized static DBUtil getDBUtil(){
		if(dbutil==null){
			dbutil=new DBUtil();
		}
		
		return dbutil;
	}
	
	public Connection getConnection(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String sql;
			Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/baidu_club_chat", "root", "19920822");
			return conn;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

}
