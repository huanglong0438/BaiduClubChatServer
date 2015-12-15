import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;


import com.dfj.baiduclubchat.common.ClubMessage;
import com.dfj.baiduclubchat.common.ClubMessageType;
import com.dfj.baiduclubchat.common.User;
import com.dfj.baiduclubchatserver.dao.GroupDao;
import com.dfj.baiduclubchatserver.dao.UserDao;



public class DoWhatAndSendMes {
	static UserDao udao=new UserDao();
	static GroupDao gdao=new GroupDao();
	
	public static void sendMes(ClubMessage m){
		try{
			//取得接收人的通信线程
			ServerConClientThread scc=ManageServerConClient.getClientThread(m.getReceiver());
			ObjectOutputStream oos=new ObjectOutputStream(scc.s.getOutputStream());
			//向接收人发送消息
			oos.writeObject(m);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void sendBuddyList(ClubMessage m){
		try{
			//操作数据库，返回好友列表，顺带群列表
			String res=udao.getBuddy(m.getSender())+","+gdao.getGroup();
			//发送好友列表到客户端
			ServerConClientThread scc=ManageServerConClient.getClientThread(m.getSender());
			ObjectOutputStream oos=new ObjectOutputStream(scc.s.getOutputStream());
			ClubMessage ms=new ClubMessage();
			ms.setType(ClubMessageType.RET_ONLINE_FRIENDS);
			ms.setContent(res);
			oos.writeObject(ms);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void delBuddy(ClubMessage m){
		try{
			if(udao.delBuddy(m.getSender(), m.getReceiver())){
				ServerConClientThread scc=ManageServerConClient.getClientThread(m.getSender());
				ObjectOutputStream oos=new ObjectOutputStream(scc.s.getOutputStream());
				ClubMessage ms=new ClubMessage();
				ms.setType(ClubMessageType.SUCCESS);
				oos.writeObject(ms);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void sendGroupMes(ClubMessage m){
		try{
			List<Integer> list=gdao.getGroupMember(m.getReceiver());
			for(int raccount:list){
				//暂只支持向在线的群成员发送消息
				if(ManageServerConClient.isOnline(raccount)){
					System.out.println(raccount+":"+ManageServerConClient.isOnline(raccount));
					//如果是自己则不发送
					if(raccount!=m.getSender()){
						System.out.println(m.getSender()+"-"+raccount+"已发送");
						ServerConClientThread scc=ManageServerConClient.getClientThread(raccount);
						ObjectOutputStream oos=new ObjectOutputStream(scc.s.getOutputStream());
						//只需改变接收者和发送者信息
						ClubMessage mes=new ClubMessage();
						
						mes.setGaccount(m.getReceiver());
						mes.setGroupNick(gdao.getGroupNick(m.getReceiver()));
						mes.setSender(m.getReceiver());
						mes.setSenderAvatar(m.getSenderAvatar());
						mes.setSenderNick(m.getSenderNick());
						mes.setReceiver(raccount);
						mes.setContent(m.getContent());
						mes.setSendTime(m.getSendTime());
						mes.setType(m.getType());
						oos.writeObject(mes);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
