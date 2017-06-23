package ywcai.ls.desk.manage;


import java.util.HashMap;



import org.apache.mina.core.session.IoSession;

import redis.clients.jedis.Jedis;
import ywcai.ls.desk.core.DeviceInfo;
import ywcai.ls.desk.core.EventNotify;


public class SessionManage implements SessionManageInf {

	HashMap<String,IoSession> sessionMap=CoreMap.getSessionMap();
//	HashMap<String , CurrentUser> userMap=null;
	Jedis jedis,jedis2;
	public SessionManage()
	{
		try
		{
			jedis=new Jedis("127.0.0.1");
			jedis.select(1);
			jedis2=new Jedis("127.0.0.1");
			jedis2.select(2);
		}
		catch(Exception e)
		{
			System.out.println("设备管理模块启动失败：连接Redis失败");
		}
		System.out.println("设备管理模块启动");
	}
	@Override
	public void addSession(String openid,DeviceInfo deviceInfo,IoSession session) {
		// TODO Auto-generated method stub
		SessionAssist sa=new SessionAssist();
		sa.openID=openid;
		sa.deviceInfo=deviceInfo;
		sa.deviceIP=session.getRemoteAddress().toString();
		session.setAttribute("sa", sa);
		sessionMap.put(deviceInfo.deviceID,session);
		jedis.set(deviceInfo.deviceID, "OnLine");
		jedis2.lpush(openid,deviceInfo.deviceID);
		EventNotify eventNotify=new EventNotify();
		eventNotify.notifyOnlineEvent(openid,deviceInfo);
	}
	@Override
	public void removeSession(String openID,DeviceInfo deviceInfo) {
		// TODO Auto-generated method stub
		sessionMap.remove(deviceInfo.deviceID);
		jedis.set(deviceInfo.deviceID, "Free");
		jedis2.lrem(openID, 1, deviceInfo.deviceID);
		EventNotify eventNotify=new EventNotify();
		eventNotify.notifyOfflineEvent(openID,deviceInfo);
	}

	@Override
	public IoSession getSession(DeviceInfo deviceInfo) {
		IoSession session=sessionMap.get(deviceInfo.deviceID);
		return session;
	}
}
