package ywcai.ls.desk.manage;

import java.util.HashMap;
import java.util.List;

import org.apache.mina.core.session.IoSession;

import ywcai.ls.desk.cfg.MyConfig;
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
		//System.out.println("isSessionLink : "+session.toString());
		boolean flag=false;
		if(isUserLink(pToken))
		{			
			CurrentUser currentUser=userMap.get(pToken);
			//System.out.println("currentUser  : "+currentUser.slaveSession+" "+currentUser.masterSession);
			if(session==currentUser.slaveSession||session==currentUser.masterSession)
			{
				flag=true;
			}
		}
		//System.out.println("flag  : "+flag);
		return flag;
	}
	@Override
	public void CreateUser(String pToken,IoSession master,IoSession slave) {

		if(master==slave)
		{
			MesReqInf toSrc=new ProtocolReqString((byte) MyConfig.REQ_TYPE_DESK_LINK_OPEN, pToken,MyConfig.STR_OPEN_DESK_FAIL1);
			master.write(toSrc);
			return ;
		}
		SessionAssist masterSa= (SessionAssist)master.getAttribute("sa");
		SessionAssist slaveSa= (SessionAssist)slave.getAttribute("sa");
		if(!isUserLink(pToken))
		{
			CurrentUser currentUser=new CurrentUser(pToken,master,slave);
			userMap.put(pToken, currentUser);	
			masterSa.isCtrl=true;
			slaveSa.isCtrl=true;
			System.out.println("user : ["+currentUser.token+"]'s link is created");		
			updateClientStatus(pToken,masterSa,slaveSa);
			MesReqInf tomaster=new ProtocolReqString((byte) MyConfig.REQ_TYPE_DESK_LINK_OPEN, currentUser.token,MyConfig.STR_OPEN_DESK_MASTER);
			MesReqInf toslave=new ProtocolReqString((byte) MyConfig.REQ_TYPE_DESK_LINK_OPEN, currentUser.token,MyConfig.STR_OPEN_DESK_SLAVE);
			currentUser.masterSession.write(tomaster);
			currentUser.slaveSession.write(toslave);	
			ControlServer.logger.info("UserManage.CreateUser({},{}) , create success",pToken,currentUser);
		}
		else
		{
			MesReqInf toSrc=new ProtocolReqString((byte) MyConfig.REQ_TYPE_DESK_LINK_OPEN, pToken,MyConfig.STR_OPEN_DESK_FAIL);
			master.write(toSrc);
			//repeated create link . do nothing	
			//ControlServer.logger.info("UserManage.CreateUser({}) but the user is online , create fail",pUsername);
		}
	}
	@Override
	public void RemoveUser(SessionAssist sessionAssist,IoSession ioSession) {
		// TODO Auto-generated method stub
		//System.out.println("usermanage :"+sessionAssist.toString());
		if(isSessionLink(sessionAssist.token, ioSession))
		{
			//System.out.println("remove user map");
			CurrentUser currentUser=CoreMap.getUserMap().get(sessionAssist.token);	
			SessionAssist masterSa=(SessionAssist)currentUser.masterSession.getAttribute("sa");
			SessionAssist slaveSa=(SessionAssist)currentUser.slaveSession.getAttribute("sa");
			userMap.remove(sessionAssist.token);
			masterSa.isCtrl=false;
			slaveSa.isCtrl=false;
			MesReqInf result=new ProtocolReqString((byte) MyConfig.REQ_TYPE_DESK_SHOWDOWN, currentUser.token,MyConfig.STR_SHUTDOWN_DESK_OK);
			if(masterSa.isConn)
			{
				currentUser.masterSession.write(result);
				//System.out.println("send to master "+MyConfig.STR_SHUTDOWN_DESK_OK);
			}
			if(slaveSa.isConn)
			{
				currentUser.slaveSession.write(result);	
				//System.out.println("send to slave "+MyConfig.STR_SHUTDOWN_DESK_OK);
			}
			currentUser=null;
			updateClientStatus(sessionAssist.token,masterSa,slaveSa);	
			System.out.println("user : ["+sessionAssist.token+"]'s link is  disconnected");
			ControlServer.logger.info("UserManage.RemoveUser({})",sessionAssist.token);
		}
		else
		{
			sessionAssist.isCtrl=false;
			if(sessionAssist.isConn)
			{
				MesReqInf result=new ProtocolReqString((byte) MyConfig.REQ_TYPE_DESK_SHOWDOWN,sessionAssist.token,MyConfig.STR_OPEN_DESK_FAIL);
				ioSession.write(result);
			}
		}
	}
	private void updateClientStatus(String pToken,SessionAssist masterSa,SessionAssist slaveSa)
	{
		List<IoSession> sessionList=CoreMap.getSessionMap().get(pToken);
		String master=masterSa.nickname+","+masterSa.remoteIp+","+masterSa.isConn+","+masterSa.isCtrl+","+masterSa.dreviceType+","+masterSa.tempID;;
		String slave=slaveSa.nickname+","+slaveSa.remoteIp+","+slaveSa.isConn+","+slaveSa.isCtrl+","+slaveSa.dreviceType+","+slaveSa.tempID;;
		ProtocolReqString result1=new ProtocolReqString((byte)MyConfig.REQ_TYPE_CLIENT_LIST_UPDATE,pToken,master);
		ProtocolReqString result2=new ProtocolReqString((byte)MyConfig.REQ_TYPE_CLIENT_LIST_UPDATE,pToken,slave);
		for (IoSession ioSession : sessionList)
		{
			SessionAssist sa= (SessionAssist)ioSession.getAttribute("sa");
			if(sa.isConn)
			{
				ioSession.write(result1);	 
				ioSession.write(result2);
			}
		}
	}
}
