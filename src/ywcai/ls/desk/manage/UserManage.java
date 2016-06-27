package ywcai.ls.desk.manage;

import java.util.HashMap;

import ywcai.ls.desk.control.ControlServer;
import ywcai.ls.desk.protocol.ProtocolReq;

public class UserManage implements UserManageInf {
	public HashMap<String , CurrentUser> userMap=null;
	public UserManage()
	{
		userMap=CoreMap.getUserMap();
		System.out.println("链接管理模块启动");
	}

	@Override
	public void CreateUser(String Username, CurrentUser currentUser) {
		// TODO Auto-generated method stub
		userMap.put(Username, currentUser);
		System.out.println("user : ["+currentUser.userName+"] create the link succuss");
		//向两边客户端返回连接成功信号
		ProtocolReq result=new ProtocolReq((byte) 0x03, currentUser.userName,"connect_ok");
		currentUser.masterSession.write(result);
		currentUser.slaveSession.write(result);		
		ControlServer.logger.info("UserManage.CreateUser({},{})",Username,currentUser);
	}

	@Override
	public void RemoveUser(String Username) {
		// TODO Auto-generated method stub
		if(userMap.containsKey(Username))
		{
		CurrentUser currentUser=CoreMap.getUserMap().get(Username);		
		userMap.remove(Username);		
		//向两边客户端返回断开连接成功信号
		ProtocolReq result=new ProtocolReq((byte) 0x04, currentUser.userName,"disconnect_ok");
		currentUser.masterSession.write(result);
		currentUser.slaveSession.write(result);	
		System.out.println("user : ["+currentUser.userName+"] dis connect the link succuss");
		currentUser=null;
		ControlServer.logger.info("UserManage.RemoveUser({})",Username);
		}
		else
		{
		ControlServer.logger.info("UserManage.RemoveUser({}), have no current user",Username);	
		}
	}

}
