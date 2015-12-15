package com.dfj.baiduclubchatserver.dao;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.dfj.baiduclubchat.common.User;

public class UserDao {
	public boolean login(int account, String password) {
		try {
			String sql = "select * from chat_user where uaccount=? and upassword=?";
			Connection conn = DBUtil.getDBUtil().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, account);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			if (rs != null && rs.next() == true) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean register(User u) {
		try {
			String sql = "insert into chat_user(upassword,unick,uavatar,utrends,usex,uage,ulev,uisonline,utime) values(?,?,?,?,?,?,?,?,?)";
			Connection conn = DBUtil.getDBUtil().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, u.getPassword());
			ps.setString(2, new String(u.getNick().getBytes(),"latin1"));
			ps.setInt(3, u.getAvatar());
			ps.setString(4, new String(u.getTrends().getBytes(),"latin1"));
			ps.setString(5, new String(u.getSex().getBytes(),"latin1"));
			ps.setInt(6, u.getAge());
			ps.setInt(7, u.getLev());
			ps.setInt(8, 0);
			ps.setString(9, new String(u.getTime().getBytes(),"latin1"));
			int r = ps.executeUpdate();
			if (r > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public int getNewAccount(){
		try {
			int res;
			String sql = "select max(uaccount) from chat_user";
			Connection conn = DBUtil.getDBUtil().getConnection();
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(sql);
			if(rs.next()){
				res = rs.getInt(1);
				return res;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	
	public boolean delBuddy(int myAccount,int dfAccount){
		try {
			String sql = "delete  from yq_buddy where baccount=? and bbuddy=?";
			Connection conn = DBUtil.getDBUtil().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, myAccount);
			ps.setInt(2, dfAccount);
			int r = ps.executeUpdate();
			if (r > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String getBuddy(int account){
		String res="";
		try {
			String sql = "select * from chat_friend where baccount="+account;
			Connection conn = DBUtil.getDBUtil().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				String s="";
				String sql2 = "select * from chat_user where uaccount="+rs.getInt("bfriend");
				Connection conn2 = DBUtil.getDBUtil().getConnection();
				PreparedStatement ps2 = conn2.prepareStatement(sql2);
				ResultSet rs2 = ps2.executeQuery();
				while(rs2.next()){
					String unick = rs2.getString("unick") == null?"":new String(rs2.getString("unick").getBytes("latin1"),"gbk");
					String utrends = rs2.getString("utrends") == null?"":new String(rs2.getString("utrends").getBytes("latin1"),"gbk");
					s=rs2.getInt("uaccount")+"_"+unick+"_"
							+rs2.getString("uavatar")+"_"+utrends+"_"+rs2.getInt("uisonline")+" ";
				}
				res+=s;	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	public String getUser(int account){
		String res="";
		try {
			String sql = "select * from chat_user where uaccount="+account;
			Connection conn = DBUtil.getDBUtil().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				String unick = rs.getString("unick") == null?"":new String(rs.getString("unick").getBytes("latin1"),"gbk");
				String utrends = rs.getString("utrends") == null?"":new String(rs.getString("utrends").getBytes("latin1"),"gbk");
				String usex = rs.getString("usex") == null?"":new String(rs.getString("usex").getBytes("latin1"),"gbk");
				res=res+rs.getInt("uaccount")+"_"+unick+"_"
						+rs.getString("uavatar")+"_"
						+utrends+"_"
						+usex+"_"+rs.getInt("uage")+"_"+rs.getInt("ulev");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	
	public boolean changeState(int account,int state){
		try {
			String sql = "update chat_user set uisonline=? where uaccount=?";
			Connection conn = DBUtil.getDBUtil().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, state);
			ps.setInt(2, account);
			int r = ps.executeUpdate();
			if (r > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}