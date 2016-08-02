package ywcai.ls.desk.manage;

import java.util.HashMap;
import java.util.List;

import org.apache.mina.core.session.IoSession;

import ywcai.ls.desk.control.ControlServer;
import ywcai.ls.desk.protocol.MesReqInf;
import ywcai.ls.desk.protocol.ProtocolReqString;

public class UserManage implements UserManageInf {
	public HashMap<String , CurrentUser> userMap=null;

	public UserManage()
	{
		userMap=CoreMap.getUserMap();
		System.out.println("链接管理模块启动");
	}
	private boolean isUserLink(String pToken)
	{
		boolean flag=userMap.containsKey(pToken);
		return flag;
	}
	private boolean isSessionLink(String pToken,IoSession session)
	{
		boolean flag=false;
		if(isUserLink(pToken))
		{			
			CurrentUser currentUser=userMap.get(pToken);
			if(session==currentUser.slaveSession||session==currentUser.masterSession)
			{
				flag=true;
			}
		}
		return flag;
	}
	@Override
	public void CreateUser(String pToken,IoSession master,IoSession slave) {

		if(!isUserLink(pToken))
		{
			CurrentUser currentUser=new CurrentUser(pToken,master,slave);
			userMap.put(pToken, currentUser);
			
			master.setAttribute("isCtrl", true);
			slave.setAttribute("isCtrl", true);
			
			System.out.println("user : ["+currentUser.token+"]'s link is created");
			
			//updateClientStatus(pToken,master.getRemoteAddress().toString(),slave.getRemoteAddress().toString(),(byte)MyConfig.REQ_TYPE_CLIENT_STATUS_USED);
			//return the create success message to client
			
			MesReqInf tomaster=new ProtocolReqString((byte) 0x03, currentUser.token,"master"+"|"+slave.getRemoteAddress());
			MesReqInf toslave=new ProtocolReqString((byte) 0x03, currentUser.token,"slave"+"|"+master.getRemoteAddress());
			
			currentUser.masterSession.write(tomaster);
			currentUser.slaveSession.write(toslave);
			
			ControlServer.logger.info("UserManage.CreateUser({},{}) , create success",pToken,currentUser);
		}
		else
		{
			MesReqInf toSrc=new ProtocolReqString((byte) 0x03, pToken,"has_link");
			master.write(toSrc);
			//repeated create link . do nothing	
			//ControlServer.logger.info("UserManage.CreateUser({}) but the user is online , create fail",pUsername);
		}
	}
	@Override
	public void RemoveUser(String pToken,IoSession ioSession) {
		// TODO Auto-generated method stub
		if(isSessionLink(pToken, ioSession))
		{
			System.out.println("remove user "+pToken+": ip "+ioSession.getRemoteAddress().toString());
			CurrentUser currentUser=CoreMap.getUserMap().get(pToken);	
			
			MesReqInf result=new ProtocolReqString((byte) 0x04, currentUser.token,"disconnect_ok");
			if(currentUser.masterSession!=null)
			{
			currentUser.masterSession.write(result);
			}
			if(currentUser.slaveSession!=null)
			{
			currentUser.slaveSession.write(result);	
			}
			//updateClientStatus(pToken,currentUser.masterSession.getRemoteAddress().toString(),currentUser.slaveSession.getRemoteAddress().toString(),(byte)MyConfig.REQ_TYPE_CLIENT_STATUS_NO);	
			currentUser=null;
			userMap.remove(pToken);	
			System.out.println("user : ["+pToken+"]'s link is  disconnected");
			ControlServer.logger.info("UserManage.RemoveUser({})",pToken);
		}
		else
		{
			
			MesReqInf result=new ProtocolReqString((byte) 0x04,pToken,"not_link");
			ioSession.write(result);
		}
	}
	private void updateClientStatus(String pToken,String master,String slave,byte status)
	{
		List<IoSession> sessionList=CoreMap.getSessionMap().get(pToken);
		int sessionCount=sessionList.size();
		String data=master+"|"+slave;
		ProtocolReqString result=new ProtocolReqString(status,pToken,data);
		for(int i=0;i<sessionCount;i++)
		{
			if(sessionList.get(i)!=null)
			{
			sessionList.get(i).write(result);	 
			}
		}
	}
	
}
