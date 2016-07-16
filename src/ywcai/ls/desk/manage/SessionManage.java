package ywcai.ls.desk.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.mina.core.session.IoSession;
import ywcai.ls.desk.control.ControlServer;
import ywcai.ls.desk.protocol.ProtocolReqString;

public class SessionManage implements SessionManageInf {

	public HashMap<String,List<IoSession>> sessionMap=null;
	public HashMap<String , CurrentUser> userMap=null;
	public SessionManage()
	{
		sessionMap=CoreMap.getSessionMap();
		userMap=CoreMap.getUserMap();
		System.out.println("会话管理模块启动");
	}
	
	//don't has any session and not create hash map 
	private boolean isNotLoginAny(String pUsername)
	{
		boolean flag=!sessionMap.containsKey(pUsername);
		return flag;
	}
	
	//is this session login some time before
	private boolean isExist(String pUsername,IoSession ioSession)
	{
		boolean flag=getSessionList(pUsername).contains(ioSession);
		return flag;
	}
	private void callBackLoginIn(String username,IoSession ioSession)
	{
		if(ioSession!=null)
		{
		ProtocolReqString result=new ProtocolReqString((byte) 0x01,username,"login_ok");
		ioSession.write(result);
		}
		else
		{
		ControlServer.logger.info("callBackLoginOut() ,user: [ {} ] want write data to null session",username);
		}
	}
	private void callBackLoginOut(String username,IoSession ioSession)
	{

		if(ioSession!=null)
		{
		ProtocolReqString result=new ProtocolReqString((byte) 0x02,username,"login_out_ok");
		ioSession.write(result);
		ioSession.closeNow();
		}
		else
		{
		ControlServer.logger.info("callBackLoginOut() ,user: [ {} ] want write data to null session",username);
		}
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
		ProtocolReqString result=new ProtocolReqString((byte) 0x07,username,data);
		for(int i=0;i<sessionCount;i++)
		{
			sessionList.get(i).write(result);	     
		}
	}

	@Override
	public void addSession(String pUsername, IoSession session) {
		// TODO Auto-generated method stub
		List<IoSession> list=getSessionList(pUsername);
		if(!isExist(pUsername,session))
		{
			list.add(session);
			sessionMap.put(pUsername, list);
			callBackLoginIn(pUsername,session);
			updateClientUI(pUsername);
			ControlServer.logger.info("SessionManage.addSession({},{}) " ,pUsername,session);
		}
		else
		{
			//is exist , do nothing;
			ControlServer.logger.info("SessionManage.addSession({},{}) , the session is exist , do nothing",pUsername,session);
		}
		
	}
	@Override
	public void removeSession(String pUsername, IoSession session) {
		// TODO Auto-generated method stub
		List<IoSession> list=getSessionList(pUsername);	
		if(isExist(pUsername,session))
		{
			list.remove(session);
			callBackLoginOut(pUsername,session);	
			updateClientUI(pUsername);
			ControlServer.logger.info("SessionManage.removeSession({},{})",pUsername,session);
		}
		else
		{
			//isn't exist , do nothing;
			ControlServer.logger.info("SessionManage.removeSession({},{}) , the session isn't exist , do nothing",pUsername,session.toString());
		}
		
	}

	@Override
	public List<IoSession> getSessionList(String pUsername) {
		List<IoSession> list=null;
		if(!isNotLoginAny(pUsername))
		{
			list=sessionMap.get(pUsername);
		}
		else 
		{
			list=new ArrayList<IoSession>();	
		}
		return list;
	}
}
