package ywcai.ls.desk.core;


import org.apache.mina.core.session.IoSession;

import ywcai.ls.desk.manage.CoreMap;
import ywcai.ls.desk.manage.CurrentUser;
import ywcai.ls.desk.manage.SessionManageInf;
import ywcai.ls.desk.manage.UserManageInf;
import ywcai.ls.desk.protocol.ProtocolRes;

public class DataProcess implements DataProcessInf {
	
	public DataProcess()
	{
		System.out.println("数据处理模块启动");
	}
	@Override
	public void Process(IoSession session, ProtocolRes protocolRes,SessionManageInf sessionManageInf,UserManageInf userManageInf) {
		// TODO Auto-generated method stub	
		byte tag=protocolRes.getTag();
		switch (tag) {
		case 0x01://login
			loginIn(protocolRes,sessionManageInf,session);
			break;
		case 0x02://out
			loginOut(protocolRes,sessionManageInf,session);
			break;
		case 0x03://create connect
			connDesk(protocolRes, userManageInf,session);
			break;
		case 0x04://disconnect
			disConnDesk(protocolRes, userManageInf);
			break;
		case 0x05://send CMD
			sendCMD(protocolRes);
			break;
		case 0x06://send desk data
			sendDesk(protocolRes);
			break;
		default:
			System.out.println("unknow tag："+tag);
		}

	}
	//登录成功
	private void loginIn(ProtocolRes protocolRes, SessionManageInf sessionManageInf, IoSession session) {
		// TODO Auto-generated method stub
		session.setAttribute("username", protocolRes.getUsername());
		session.setAttribute("nickname", protocolRes.getData());
		sessionManageInf.addSession(protocolRes.getUsername(), session);
		
	}
	private void loginOut(ProtocolRes protocolRes, SessionManageInf sessionManageInf, IoSession session) {
		// TODO Auto-generated method stub
		sessionManageInf.removeSession(protocolRes.getUsername(), session);
	}


	private void connDesk(ProtocolRes protocolRes, UserManageInf userManageInf, IoSession session) {
		// TODO Auto-generated method stub
		CurrentUser cUser=new CurrentUser();
		cUser.userName=protocolRes.getUsername();
		cUser.masterSession=session;
		//data 需要携带 远端remote的索引。携带字符串，通过强制转换
		cUser.slaveSession=CoreMap.getSessionMap().get(protocolRes.getUsername()).get(Integer.parseInt(protocolRes.getData()));
		userManageInf.CreateUser(protocolRes.getUsername(), cUser);
	}
	
	private void disConnDesk(ProtocolRes protocolRes, UserManageInf userManageInf) {
		// TODO Auto-generated method stub
		userManageInf.RemoveUser(protocolRes.getUsername());
	}
	private void sendCMD(ProtocolRes protocolRes) {
		// TODO Auto-generated method stu
		//直接转发数据
	}
	private void sendDesk(ProtocolRes protocolRes) {
		// TODO Auto-generated method stub
		//直接转发数据
	}
	
}
