package ywcai.ls.desk.core;


import org.apache.mina.core.session.IoSession;

import ywcai.ls.desk.cfg.MyConfig;
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
		session.setAttribute("token", mesRes.getToken());
		session.setAttribute("nickname", mesRes.getData());
		sessionManageInf.addSession(mesRes.getToken(), session);

	}
	private void loginOut(MesResInf mesRes, SessionManageInf sessionManageInf, IoSession session) {
		// TODO Auto-generated method stub
		sessionManageInf.removeSession(mesRes.getToken(), session);
	}


	private void createLink(MesResInf mesRes, UserManageInf userManageInf, IoSession session) {
		String token=mesRes.getToken();
		IoSession master=session;
		//data has the remote session's index
		IoSession slave=CoreMap.getSessionMap().get(mesRes.getToken()).get(Integer.parseInt((String)mesRes.getData()));
		userManageInf.CreateUser(token,master,slave);
	}
	private void shutDownLink(MesResInf mesRes, UserManageInf userManageInf,IoSession session) {
		// TODO Auto-generated method stub
		userManageInf.RemoveUser(mesRes.getToken(),session);
	}
	private void sendCMD(MesResInf mesRes) {
		// TODO Auto-generated method stub
		//直接转发数据t
		String token = mesRes.getToken();
		String data =(String)mesRes.getData();
		MesReqInf mesReq=new ProtocolReqString((byte)0x05, token, data);
		IoSession toSession=CoreMap.getUserMap().get(token).slaveSession;
		synchronized (toSession) {
			toSession.write(mesReq);
		}


	}
	private void sendDesk(MesResInf mesRes) {
		// TODO Auto-generated method stub
		//直接转发数据
		String token = mesRes.getToken();
		byte[] data =(byte[])mesRes.getData();
		MesReqInf mesReq=new ProtocolReqByte((byte)0x06, token, data);
		IoSession toSession=CoreMap.getUserMap().get(token).masterSession;
		synchronized(toSession)
		{
			toSession.write(mesReq);
		}
	}
	@Override
	public void processReciveEvent(IoSession session, Object message, SessionManageInf sessionManageInf,
			UserManageInf userManageInf) {
		MesResInf mesRes=(MesResInf)message;
		byte reqType=mesRes.getReqType();
		switch (reqType) {
		case MyConfig.REQ_TYPE_USER_LOGIN_IN://login
			loginIn(mesRes,sessionManageInf,session);
			break;
		case MyConfig.REQ_TYPE_USER_LOGIN_OUT://out
			loginOut(mesRes,sessionManageInf,session);
			break;
		case MyConfig.REQ_TYPE_DESK_LINK_OPEN://create connect
			createLink(mesRes, userManageInf,session);
			break;
		case MyConfig.REQ_TYPE_DESK_SHOWDOWN://disconnect
			shutDownLink(mesRes, userManageInf,session);
			break;
		case MyConfig.REQ_TYPE_CONTROL_CMD://send CMD
			sendCMD(mesRes);
			break;
		case MyConfig.REQ_TYPE_DESKTOP_SWITCH://send desk data
			sendDesk(mesRes);
			break;
		case MyConfig.REQ_TYPE_CLIENT_LIST_UPDATE://send desk data
			//更新UI，由服务端下发指令，服务端不处理
			break;
		default:
			System.out.println("unknow reqType："+reqType);
		}

		//System.out.println("from:"+session.getRemoteAddress()+ "  body: "  +mesRes.getData());
	}

	@Override
	public void processSentEvent(IoSession session, Object message, SessionManageInf sessionManageInf,
			UserManageInf userManageInf) {
		// TODO Auto-generated method stub
		//MesReqInf mesReq=(MesReqInf)message;	
		//System.out.println("to:"+session.getRemoteAddress()+" lenth "+mesReq.getDataLenth()+"  body: "  +mesReq.getData());
	}
	@Override
	public void processCloseEvent(IoSession session, SessionManageInf sessionManageInf,
			UserManageInf userManageInf) {
		String token="";
		try
		{
			token=session.getAttribute("token").toString();
		}
		catch(Exception e)
		{
			System.out.println("Close Event , reading the close session has an err :"+e.toString());
		}
		userManageInf.RemoveUser(token,session);
		sessionManageInf.removeSession(token, session);
	}
}
