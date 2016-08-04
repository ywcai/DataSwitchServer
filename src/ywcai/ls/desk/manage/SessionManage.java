package ywcai.ls.desk.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.mina.core.session.IoSession;

import ywcai.ls.desk.cfg.MyConfig;
import ywcai.ls.desk.control.ControlServer;
import ywcai.ls.desk.protocol.ProtocolReqString;

public class SessionManage implements SessionManageInf {

	public HashMap<String,List<IoSession>> sessionMap=null;
	public HashMap<String , CurrentUser> userMap=null;
	private UserManageInf userManage=null;
	public SessionManage(UserManageInf userManageInf)
	{
		sessionMap=CoreMap.getSessionMap();
		userMap=CoreMap.getUserMap();
		userManage=userManageInf;
		System.out.println("会话管理模块启动");
	}

	//don't has any session and not create hash map 
	private boolean isNotLoginAny(String token)
	{
		boolean flag=!sessionMap.containsKey(token);
		return flag;
	}

	//is this user name login some time before
	private boolean isExist(String token,IoSession ioSession)
	{
		boolean flag=getSessionList(token).contains(ioSession);
		return flag;
	}
	private void callBackLoginIn(SessionAssist sAssist,IoSession ioSession)
	{
		sAssist.isConn=true;
		List<IoSession> sessionList=getSessionList(sAssist.token);
		int sessionCount=sessionList.size();
		String data=MyConfig.STR_LOGIN_RESULT_OK+"#";
		for (int i=0;i<sessionCount;i++) 
		{
			SessionAssist sAssist2=(SessionAssist)sessionList.get(i).getAttribute("sa");
			if(sAssist2!=sAssist)
			{
				String content=sAssist2.nickname+","+sAssist2.remoteIp+","+sAssist2.isConn+","+sAssist2.isCtrl+","+sAssist2.dreviceType+","+sAssist2.tempID;
				data=data+""+content+"|";
			}
		}
		if(sAssist.isConn&&ioSession.getRemoteAddress()!=null)
		{
			ProtocolReqString result=new ProtocolReqString((byte) MyConfig.REQ_TYPE_USER_LOGIN_IN,sAssist.token,data);
			ioSession.write(result);
		}
		else
		{
			//ControlServer.logger.info("callBackLoginOut() ,user: [ {} ] want write data to null session",username);
		}
	}
	private void callBackLoginOut(SessionAssist sAssist,IoSession ioSession)
	{

		if(ioSession.getRemoteAddress()!=null&&sAssist.isConn!=false)
		{
			ProtocolReqString result=new ProtocolReqString((byte) MyConfig.REQ_TYPE_USER_LOGIN_OUT,sAssist.token,MyConfig.STR_OUT_RESULT_OK);
			ioSession.write(result);
			//System.out.println("服务器向客户端回执了关闭连接的请求 ： "+sAssist.token+ " , session : "+ ioSession);
			
		}
		else
		{
			//System.out.println("服务器试图向客户端回执关闭连接的请求，但该连接已经丢失 ： "+sAssist.token+ " , session : "+ ioSession);
			//ControlServer.logger.info("callBackLoginOut() ,user: [ {} ] want write data to null session",username);
		}
		sAssist.isConn=false;

	}
	
	private void updateClientUI(SessionAssist sAssist,IoSession session)
	{			
		String content=sAssist.nickname+","+sAssist.remoteIp+","+sAssist.isConn+","+sAssist.isCtrl+","+sAssist.dreviceType+","+sAssist.tempID;
		List<IoSession> sessionList=getSessionList(sAssist.token);
		ProtocolReqString result=new ProtocolReqString((byte) MyConfig.REQ_TYPE_CLIENT_LIST_UPDATE,sAssist.token,content);
		for (IoSession ioSession : sessionList) 
		{
			SessionAssist sa= (SessionAssist)ioSession.getAttribute("sa");
			if(sa.isConn)
			{
				ioSession.write(result);	 
			}
		}
	}

	@Override
	public void addSession(SessionAssist sAssist, IoSession session) {
		// TODO Auto-generated method stub
		List<IoSession> list=getSessionList(sAssist.token);
		//checkToken(),校验令牌，成功执行否则直接关闭socket，等待补充完整校验方法
		//checkNickname();
//		for (IoSession ioSession : list) {
//			SessionAssist sa= (SessionAssist)ioSession.getAttribute("sa");
//			if(sa.nickname.equals(sAssist.nickname))
//			{
//				ProtocolReqString result=new ProtocolReqString((byte) MyConfig.REQ_TYPE_USER_LOGIN_OUT,sAssist.token,MyConfig.STR_OUT_RESULT_OK);
//				ioSession.write(result);
//				return;
//			}
//		}
		
		
		if(!sAssist.isConn)
		{
			list.add(session);
			sessionMap.put(sAssist.token, list);
			callBackLoginIn(sAssist,session);
			updateClientUI(sAssist,session);
			ControlServer.logger.info("SessionManage.addSession({},{}) " ,sAssist.token,session);
		}
		else
		{
			//is exist , do nothing;
			//ControlServer.logger.info("SessionManage.addSession({},{}) , the session is exist , do nothing",pUsername,session);
		}

	}
	@Override
	public void removeSession(SessionAssist sAssist, IoSession session) {
		// TODO Auto-generated method stub
		if(sAssist.isCtrl)
		{
			userManage.RemoveUser(sAssist, session);
		}
		List<IoSession> list=getSessionList(sAssist.token);	
		if(isExist(sAssist.token,session))
		{
			list.remove(session);
			if(sAssist.isNormalClose)
			{
				callBackLoginOut(sAssist,session);	
			}
			updateClientUI(sAssist,session);
			ControlServer.logger.info("SessionManage.removeSession({},{})",sAssist.token,session);
		}
		else
		{
			//isn't exist , do nothing;
			//ControlServer.logger.info("SessionManage.removeSession({},{}) , the session isn't exist , do nothing",pUsername,session.toString());
		}
		//System.out.println("连接关闭成功  ： token : "+sAssist.token+" , ip : "+sAssist.remoteIp + " , session ip : "+session.getRemoteAddress());

	}

	@Override
	public List<IoSession> getSessionList(String pToken) {
		List<IoSession> list=null;
		if(!isNotLoginAny(pToken))
		{
			list=sessionMap.get(pToken);
		}
		else 
		{
			list=new ArrayList<IoSession>();	
		}
		return list;
	}
}
