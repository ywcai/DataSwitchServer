package ywcai.ls.desk.core;


import java.io.UnsupportedEncodingException;

import org.apache.mina.core.session.IoSession;
import org.json.JSONException;
import org.json.JSONObject;

import redis.clients.jedis.Jedis;
import ywcai.ls.desk.cfg.MyConfig;
import ywcai.ls.desk.cfg.MyUtil;
import ywcai.ls.desk.cfg.ResultCode;
import ywcai.ls.desk.manage.LinkManageInf;
import ywcai.ls.desk.manage.SessionAssist;
import ywcai.ls.desk.manage.SessionManageInf;
import ywcai.ls.desk.newpro.MesInf;

public class DataProcess implements DataProcessInf {

	private  SessionManageInf sessionManageInf;
	private Jedis jedis;
	public DataProcess(SessionManageInf _sessionManageInf)
	{
		sessionManageInf=_sessionManageInf;
		jedis=new Jedis("127.0.0.1",6379);
		jedis.select(1);
		System.out.println("数据处理模块启动");
	}
	//登录成功
	//	private void loginIn(MesResInf mesRes, SessionManageInf sessionManageInf, IoSession session) {
	//		// TODO Auto-generated method stub
	//		SessionAssist sAssist=new SessionAssist();
	//		sAssist.token=mesRes.getToken();
	//		sAssist.isNormalClose=true;
	//		sAssist.isConn=false;
	//		sAssist.nickname=mesRes.getData().toString().split(",")[0];
	//		sAssist.isCtrl=false;
	//		sAssist.remoteIp=session.getRemoteAddress().toString();
	//		sAssist.tempID=session.getId();
	//		sAssist.dreviceType=Integer.parseInt(mesRes.getData().toString().split(",")[1]);
	//		//sAssist.username=tokenHashMap.getkey(token);
	//		session.setAttribute("sa",sAssist);
	//		sessionManageInf.addSession(sAssist, session);
	//
	//	}
	//	private void loginOut(MesResInf mesRes, SessionManageInf sessionManageInf, IoSession session) {
	//		// TODO Auto-generated method stub
	//		SessionAssist sAssist=(SessionAssist)session.getAttribute("sa");
	//		sessionManageInf.removeSession(sAssist, session);
	//	}


	//	private void createLink(MesResInf mesRes, UserManageInf userManageInf, IoSession session) {
	//
	//		String token=mesRes.getToken();
	//		IoSession master=session;
	//		long sessionID=Integer.parseInt((String)mesRes.getData());
	//		boolean flag=false;
	//		for (IoSession ioSession : CoreMap.getSessionMap().get(token)) 
	//		{
	//			if(ioSession.getId()==sessionID)
	//			{
	//				IoSession slave=ioSession;
	//				//System.out.println("Find the slave session is : " + slave);	
	//				userManageInf.CreateUser(token,master,slave);
	//				flag=true;
	//				return ;
	//			}	
	//		}
	//		if(!flag)
	//		{
	//			System.out.println("客户端请求向一个空连接发起远程控制，客服端 ，session is : " + session);	
	//			ControlServer.logger.info("客户端请求向一个空连接发起远程控制，客服端 ，session is : " + session);
	//		}
	//		//data has the remote session's index
	//		//IoSession slave=CoreMap.getSessionMap().get(mesRes.getToken()).get(sessionIndex);
	//		//userManageInf.CreateUser(token,master,slave);
	//	}
	//	private void shutDownLink(MesResInf mesRes, UserManageInf userManageInf,IoSession session) {
	//		// TODO Auto-generated method stub
	//		SessionAssist sAssist=null;
	//		try
	//		{
	//			sAssist=(SessionAssist)session.getAttribute("sa");
	//		}
	//		catch(Exception e)
	//		{
	//			System.out.println("Close Event , reading the close session has an err :"+e.toString());
	//		}
	//		userManageInf.RemoveUser(sAssist,session);
	//	}
	//	private void sendCMD(MesResInf mesRes) 
	//	{
	//		// TODO Auto-generated method stub
	//		//直接转发数据t
	//		String token = mesRes.getToken();
	//		String data =(String)mesRes.getData();
	//		MesReqInf mesReq=new ProtocolReqString((byte)0x05, token, data);
	//		IoSession toSession=CoreMap.getUserMap().get(token).slaveSession;
	//		synchronized (toSession)
	//		{
	//			toSession.write(mesReq);
	//		}
	//	}
	//	private void sendDesk(MesResInf mesRes) {
	//		// TODO Auto-generated method stub
	//		//直接转发数据
	//		String token = mesRes.getToken();
	//		byte[] data =(byte[])mesRes.getData();
	//		MesReqInf mesReq=new ProtocolReqByte((byte)0x06, token, data);
	//		IoSession toSession=CoreMap.getUserMap().get(token).masterSession;
	//		synchronized(toSession)
	//		{
	//			toSession.write(mesReq);
	//		}
	//	}
	@Override
	public void processReciveEvent(IoSession session, Object message, SessionManageInf sessionManageInf,
			LinkManageInf linkManageInf) {
		MesInf mesInf=(MesInf)message;
		if(mesInf.getData()[0]==MyConfig.byte_data_flag_json)
		{
			String s = " ";
			byte[] temp=new byte[mesInf.getData().length-1];
			System.arraycopy(mesInf.getData(),1 , temp, 0, temp.length);
			try {
				s=new String(temp,"utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JSONObject json=new JSONObject();
			try {
				json = new JSONObject(s);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Analysis(session,json,linkManageInf);
			return ;

		}
		if(mesInf.getData()[0]==MyConfig.byte_data_flag_byte)
		{
			Transfer(session,mesInf);
			return ;
		}

		System.out.println("数据位标识错误"+mesInf.getData()[0]);
		//		switch (reqType) {
		////		case MyConfig.REQ_TYPE_USER_LOGIN_IN://login
		////			loginIn(mesRes,sessionManageInf,session);
		////			break;
		////		case MyConfig.REQ_TYPE_USER_LOGIN_OUT://out
		////			loginOut(mesRes,sessionManageInf,session);
		////			break;
		////		case MyConfig.REQ_TYPE_DESK_LINK_OPEN://create connect
		////			createLink(mesRes, userManageInf,session);
		////			break;
		////		case MyConfig.REQ_TYPE_DESK_SHOWDOWN://disconnect
		////			shutDownLink(mesRes, userManageInf,session);
		////			//System.out.println(mesRes.getData().toString());
		////			break;
		////		case MyConfig.REQ_TYPE_CONTROL_CMD://send CMD
		////			sendCMD(mesRes);
		////			break;
		////		case MyConfig.REQ_TYPE_DESKTOP_SWITCH://send desk data
		////			sendDesk(mesRes);
		////			break;
		////		case MyConfig.REQ_TYPE_CLIENT_LIST_UPDATE://send desk data
		////			//更新UI，由服务端下发指令，服务端不处理
		////			break;
		//		default:
		//			System.out.println("unknow reqType："+reqType);
		//		}

	}

	private void Analysis(IoSession session,JSONObject json,LinkManageInf linkManageInf){
		// TODO Auto-generated method stub
		String type="",content="";
		try {
			type=json.getString(MyConfig.json_key_type);
			content=json.getString(MyConfig.json_key_content);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int reponseCode=Integer.parseInt(type);
		switch(reponseCode)
		{
		case ResultCode.json_type_check_token:
			checkToken(session,content);
			break;
		case ResultCode.json_type_request_link:
			createLink(session,content,linkManageInf);
			break;
		case ResultCode.json_type_cut_link:
			cutLink(session,content,linkManageInf);
			break;
		case ResultCode.json_type_control_cmd:
			transerCmd(session,content);
			break;
		}
	}
	private void cutLink(IoSession session, String content,
			LinkManageInf linkManageInf) {
		// TODO Auto-generated method stub
		String manageID=((SessionAssist)session.getAttribute("sa")).deviceInfo.deviceID;
		String openID=((SessionAssist)session.getAttribute("sa")).openID;
		linkManageInf.CutLink(openID, manageID);
	}


	private void createLink(IoSession session,String content,LinkManageInf linkManageInf) {
		// TODO Auto-generated method stub
		//建立连接,content是设备虚拟ID
		String manageID=((SessionAssist)session.getAttribute("sa")).deviceInfo.deviceID;
		String openID=((SessionAssist)session.getAttribute("sa")).openID;
		if(jedis.exists(manageID)&&jedis.exists(content))
		{
			if(jedis.get(manageID).equals("OnLine")&&jedis.get(content).equals("OnLine"))
			{
				linkManageInf.CreateLink(openID,manageID,content);
				return ;
			}
		}
		responseResult(session,ResultCode.json_type_notify_link_fail,"err");
		//返回连接失败信息.
	}
	

	private void transerCmd(IoSession session,String content) {
		// TODO Auto-generated method stub
		//转发指令数据,content是具体操作指令
	}

	private void Transfer(IoSession session,MesInf mes) {
		// TODO Auto-generated method stub
		//直接转发图像数据
		//session.write(mes);
	}

	private void checkToken(IoSession session,String content) {
		// TODO Auto-generated method stub
		//验证TOKEN有效性
		String[] str=new String[5];
		str[0]="openid";
		str[1]="token";
		str[2]="did";
		str[3]="dev";
		str[4]="0";
		try
		{
			str=content.split("\\|");
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		String key=str[0];
		String val=str[1];
		String did=str[2];
		String dev=str[3];
		int deviceType=Integer.parseInt(str[4]);
	    if(!jedis.exists(key)||!jedis.exists(did))
	    {
			System.out.println("无效OpenID: ["+key+"] ，服务器已断开这个连接!");
			session.closeNow();
	    	return ;
	    }
		if(jedis.get(key).equals(val)&&jedis.get(did).equals("Free"))
		{
		    System.out.println("check token successed!");
		    MyUtil.sendJson(session,ResultCode.json_type_validate_success,"");//reponse to client
		    DeviceInfo deviceInfo=new DeviceInfo();
		    deviceInfo.deviceID=did;
		    deviceInfo.deviceName=dev;
		    deviceInfo.deviceType=deviceType;
		    sessionManageInf.addSession(key, deviceInfo,session);
		}
		else
		{
			session.closeNow();
		}
	}





	@Override
	public void processSentEvent(IoSession session, Object message, SessionManageInf sessionManageInf,
			LinkManageInf linkManageInf) {
		// TODO Auto-generated method stub
	}
	@Override
	public void processCloseEvent(IoSession session, SessionManageInf sessionManageInf,
			LinkManageInf linkManageInf) {
		System.out.println("a socket close event !");
		SessionAssist sAssist=null;
		try
		{
			sAssist=(SessionAssist)session.getAttribute("sa");
		}
		catch(Exception e)
		{
			System.out.println("Close a socket that is not legitimated !");
			return ;
		}
		System.out.println("Session closed : "+sAssist.toString());
		linkManageInf.CutLink(sAssist.openID, sAssist.deviceInfo.deviceID);
		sessionManageInf.removeSession(sAssist.openID,sAssist.deviceInfo);
	}
	private void responseResult(IoSession session,int resultCode,String content)
	{
		MyUtil.sendJson(session,resultCode,content);
	}

}
