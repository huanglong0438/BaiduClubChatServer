/**
 * ��������ĳ���ͻ��˵�ͨ���߳�
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.dfj.baiduclubchat.common.ClubMessage;
import com.dfj.baiduclubchat.common.ClubMessageType;


public class ServerConClientThread extends Thread {
	Socket s;
	ObjectInputStream ois = null;
	public ServerConClientThread(Socket s){
		this.s=s;
		try {
			ois = new ObjectInputStream(s.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		while(true){
			//ObjectInputStream ois = null;
			ClubMessage m = null;
			try {
				//ois=new ObjectInputStream(s.getInputStream());
				m=(ClubMessage) ois.readObject();
				//�Դӿͻ���ȡ�õ���Ϣ���������жϣ�����Ӧ�Ĵ���
				if(m.getType().equals(ClubMessageType.COM_MES)){//�������ͨ��Ϣ��
					Thread.sleep(2000);
					DoWhatAndSendMes.sendMes(m);
				}else if(m.getType().equals(ClubMessageType.GROUP_MES)){ //�����Ⱥ��Ϣ
					DoWhatAndSendMes.sendGroupMes(m);
				}else if(m.getType().equals(ClubMessageType.GET_ONLINE_FRIENDS)){//�������������б�
					DoWhatAndSendMes.sendBuddyList(m);
				}else if(m.getType().equals(ClubMessageType.DEL_BUDDY)){ //�����ɾ������
					DoWhatAndSendMes.delBuddy(m);
				}
			} catch (Exception e) {
				//System.out.println(e.getMessage());
				try {
					s.close();
					ois.close();
				} catch (IOException e1) {
					System.out.println(e1.getMessage());
					//e1.printStackTrace();
				}
				e.printStackTrace();
				break;
			}
		}
	}
}
