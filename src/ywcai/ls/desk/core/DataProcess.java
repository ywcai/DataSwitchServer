package ywcai.ls.desk.core;


import org.apache.mina.core.session.IoSession;

import ywcai.ls.desk.manage.CoreMap;
import ywcai.ls.desk.manage.SessionManageInf;
import ywcai.ls.desk.manage.UserManageInf;
import ywcai.ls.desk.protocol.MesReqInf;
import ywcai.ls.desk.protocol.MesResInf;
import ywcai.ls.desk.protocol.ProtocolReqString;
import ywcai.ls.desk.protocol.ProtocolReqByte;

public class DataProcess implements DataProcessInf {
	
	public DataProcess()
	{
		System.out.println("数据处理模块启动");
	}
	//登录成功
	private void loginIn(MesResInf mesRes, SessionManageInf sessionManageInf, IoSession session) {
		// TODO Auto-generated method stub
		session.setAttribute("username", mesRes.getUsername());
		session.setAttribute("nickname", mesRes.getData());
		sessionManageInf.addSession(mesRes.getUsername(), session);
		
	}
	private void loginOut(MesResInf mesRes, SessionManageInf sessionManageInf, IoSession session) {
		// TODO Auto-generated method stub
		sessionManageInf.removeSession(mesRes.getUsername(), session);
	}


	private void createLink(MesResInf mesRes, UserManageInf userManageInf, IoSession session) {
		String userName=mesRes.getUsername();
		IoSession master=session;
		//data has the remote session's index
		IoSession slave=CoreMap.getSessionMap().get(mesRes.getUsername()).get(Integer.parseInt((String)mesRes.getData()));
		
		userManageInf.CreateUser(userName,master,slave);
	}
	private void shutDownLink(MesResInf mesRes, UserManageInf userManageInf,IoSession session) {
		// TODO Auto-generated method stub
		userManageInf.RemoveUser(mesRes.getUsername(),session);
	}
	private void sendCMD(MesResInf mesRes) {
		// TODO Auto-generated method stub
		//直接转发数据t
		String userName = mesRes.getUsername();
		String data =(String)mesRes.getData();
		MesReqInf mesReq=new ProtocolReqString((byte)0x05, userName, data);
		IoSession toSession=CoreMap.getUserMap().get(userName).slaveSession;
		toSession.write(mesReq);
		
	}
	private void sendDesk(MesResInf mesRes) {
		// TODO Auto-generated method stub
		//直接转发数据
		String currentUsername = mesRes.getUsername();
		byte[] data =(byte[])mesRes.getData();
		MesReqInf mesReq=new ProtocolReqByte((byte)0x06, currentUsername, data);
		IoSession toSession=CoreMap.getUserMap().get(currentUsername).masterSession;
		toSession.write(mesReq);
	}
	@Override
	public void processReciveEvent(IoSession session, Object message, SessionManageInf sessionManageInf,
			UserManageInf userManageInf) {
		MesResInf mesRes=(MesResInf)message;
		byte tag=mesRes.getTag();
		switch (tag) {
		case 0x01://login
			loginIn(mesRes,sessionManageInf,session);
			break;
		case 0x02://out
			loginOut(mesRes,sessionManageInf,session);
			break;
		case 0x03://create connect
			createLink(mesRes, userManageInf,session);
			break;
		case 0x04://disconnect
			shutDownLink(mesRes, userManageInf,session);
			break;
		case 0x05://send CMD
			sendCMD(mesRes);
			break;
		case 0x06://send desk data
			sendDesk(mesRes);
			break;
		default:
			//System.out.println("unknow tag："+tag);
		}

		//System.out.println("from:"+session.getRemoteAddress()+ "  body: "  +mesRes.getData());
	}
	
	@Override
	public void processSentEvent(IoSession session, Object message, SessionManageInf sessionManageInf,
			UserManageInf userManageInf) {
		// TODO Auto-generated method stub
		//MesReqInf mesReq=(MesReqInf)message;	
		//System.out.println("to:"+session.getRemoteAddress()+ "  body: "  +mesReq.getData());
	}
	@Override
	public void processCloseEvent(IoSession session, SessionManageInf sessionManageInf,
			UserManageInf userManageInf) {
		System.out.println("close a session,remote ip: "+session.getRemoteAddress());
		String username=session.getAttribute("username").toString();
		userManageInf.RemoveUser(username,session);
		sessionManageInf.removeSession(username, session);

	}
	
}
