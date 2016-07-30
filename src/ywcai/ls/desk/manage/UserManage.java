package ywcai.ls.desk.manage;

import java.util.HashMap;
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
	private boolean isUserLink(String pUsername)
	{
		boolean flag=userMap.containsKey(pUsername);
		return flag;
	}
	private boolean isSessionLink(String pUsername,IoSession session)
	{
		boolean flag=false;
		if(isUserLink(pUsername))
		{			
			CurrentUser currentUser=userMap.get(pUsername);
			if(session==currentUser.slaveSession||session==currentUser.masterSession)
			{
				flag=true;
			}
		}
		return flag;
	}
	@Override
	public void CreateUser(String pUsername,IoSession master,IoSession slave) {

		if(!isUserLink(pUsername))
		{
			CurrentUser currentUser=new CurrentUser(pUsername,master,slave);
			userMap.put(pUsername, currentUser);
			System.out.println("user : ["+currentUser.userName+"]'s link is created");
			//return the create success message to client
			MesReqInf tomaster=new ProtocolReqString((byte) 0x03, currentUser.userName,"master");
			MesReqInf toslave=new ProtocolReqString((byte) 0x03, currentUser.userName,"slave");
			currentUser.masterSession.write(tomaster);
			currentUser.slaveSession.write(toslave);
			ControlServer.logger.info("UserManage.CreateUser({},{}) , create success",pUsername,currentUser);
		}
		else
		{
			MesReqInf toSrc=new ProtocolReqString((byte) 0x03, pUsername,"has_link");
			master.write(toSrc);
			//repeated create link . do nothing	
			//ControlServer.logger.info("UserManage.CreateUser({}) but the user is online , create fail",pUsername);
		}
	}
	@Override
	public void RemoveUser(String pUsername,IoSession ioSession) {
		// TODO Auto-generated method stub
		if(isSessionLink(pUsername, ioSession))
		{
			CurrentUser currentUser=CoreMap.getUserMap().get(pUsername);		
			userMap.remove(pUsername);		
			//return the remove success message to client
			MesReqInf result=new ProtocolReqString((byte) 0x04, currentUser.userName,"disconnect_ok");
			currentUser.masterSession.write(result);
			currentUser.slaveSession.write(result);	
			currentUser=null;
			System.out.println("user : ["+pUsername+"]'s link is  disconnected");
			ControlServer.logger.info("UserManage.RemoveUser({})",pUsername);
		}
		else
		{
			MesReqInf result=new ProtocolReqString((byte) 0x04,pUsername,"not_link");
			ioSession.write(result);
			//ControlServer.logger.info("UserManage.RemoveUser({}), this sesion is not online.",pUsername);	
		}
	}
}
