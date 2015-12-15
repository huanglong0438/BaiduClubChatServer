import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import com.dfj.baiduclubchat.common.ClubMessage;
import com.dfj.baiduclubchat.common.ClubMessageType;
import com.dfj.baiduclubchat.common.User;
import com.dfj.baiduclubchatserver.dao.UserDao;


public class Server {	
	
	public static void main(String[] args) {
		try {
			String flag = new String();
			ServerSocket server = new ServerSocket(9999);
			Socket client;
			System.out.println("server started");
			
			while(true){
				client = server.accept();
				System.out.println("receive connection");
				ObjectInputStream ois=new ObjectInputStream(client.getInputStream());
				User u= new User();
				u = (User) ois.readObject();
				ClubMessage m = new ClubMessage();
				ObjectOutputStream oos=new ObjectOutputStream(client.getOutputStream());

				if(u.getOperation().equals("login")){
					int account = u.getAccount();
					String password = u.getPassword();
					UserDao udao = new UserDao();
					boolean b = udao.login(account, password);
					if(b){
						System.out.println("["+account+"]上线了！");
						udao.changeState(account, 1);
						String user=udao.getUser(account);
						m.setType(ClubMessageType.SUCCESS);
						m.setContent(user);
						oos.writeObject(m);
						ServerConClientThread cct=new ServerConClientThread(client);//单开一个线程，让该线程与该客户端保持连接
						ManageServerConClient.addClientThread(u.getAccount(),cct);
						cct.start();//启动与该客户端通信的线程
					}
					else{
						m.setType(ClubMessageType.FAIL);
						oos.writeObject(m);
					}
				}
				else if(u.getOperation().equals("register")){
		        	UserDao udao=new UserDao();
		        	if(udao.register(u)){
		        		//返回一个注册成功的信息包
		        		int account = udao.getNewAccount();
						m.setType(ClubMessageType.SUCCESS);
						m.setReceiver(account);
						oos.writeObject(m);
		        	}
		        	else{
		        		m.setType(ClubMessageType.FAIL);
		        		oos.writeObject(m);
		        	}
		        }
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	
	

}
