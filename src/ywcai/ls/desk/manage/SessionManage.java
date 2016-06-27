package ywcai.ls.desk.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.mina.core.session.IoSession;
import ywcai.ls.desk.control.ControlServer;
import ywcai.ls.desk.protocol.ProtocolReq;

public class SessionManage implements SessionManageInf {

	public HashMap<String,List<IoSession>> sessionMap=null;
	public SessionManage()
	{
		sessionMap=CoreMap.getSessionMap();
		System.out.println("会话管理模块启动");
	}

	@Override
	public void addSession(String username, IoSession session) {
		// TODO Auto-generated method stub
		
		if (!sessionMap.containsKey(username)) {
			List<IoSession> list=new ArrayList<IoSession>();
			list.add(session);
			sessionMap.put(username, list);
			ControlServer.logger.info("SessionManage.addSession({},{}) , put hashmap",username,session);
		}
		else
		{
			if(!getSessionList(username).contains(session))
			{
				getSessionList(username).add(session);
				ControlServer.logger.info("SessionManage.addSession({},{}) , create new session" ,username,session);
			}
			else
			{
				//do nothing;
				ControlServer.logger.info("SessionManage.addSession({},{}) , do nothing",username,session);
			}
		}
		callBackLoginIn(username,session);
		updateClientUI(username);
	}
	@Override
	public void removeSession(String username, IoSession session) {
		// TODO Auto-generated method stub
		getSessionList(username).remove(session);
		callBackLoginOut(username,session);		
		if(getSessionList(username).isEmpty())
		{
			sessionMap.remove(username);	
		}
		else
		{
			updateClientUI(username);
		}
		ControlServer.logger.info("SessionManage.removeSession({},{})",username,session);
	}

	@Override
	public List<IoSession> getSessionList(String username) {
		// TODO Auto-generated method stub
		List<IoSession> list=sessionMap.get(username);
		return list;
	}

	private void callBackLoginIn(String username,IoSession ioSession)
	{
		ProtocolReq result=new ProtocolReq((byte) 0x02,username,"login_ok");
		ioSession.write(result);
	}
	
	private void callBackLoginOut(String username,IoSession ioSession)
	{
		ProtocolReq result=new ProtocolReq((byte) 0x02,username,"out_ok");
		ioSession.write(result);
	}

	private void updateClientUI(String username)
	{
		List<IoSession> sessionList=getSessionList(username);
		int sessionCount=sessionList.size();
		String data="";
		for(int i=0;i<sessionCount;i++)
		{
				String  nickname=sessionList.get(i).getAttribute("nickname").toString();
				String  ipaddr=sessionList.get(i).getRemoteAddress().toString();
				data+=i+"|"+nickname+"|"+ipaddr;
				if(i!=sessionCount-1)
				{
					data+=",";
				}
		}
		ProtocolReq result=new ProtocolReq((byte) 0x07,username,data);
		for(int i=0;i<sessionCount;i++)
		{
			sessionList.get(i).write(result);
		}
	}

}
