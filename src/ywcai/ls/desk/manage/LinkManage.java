package ywcai.ls.desk.manage;

import java.util.HashMap;

import redis.clients.jedis.Jedis;
import ywcai.ls.desk.core.EventNotify;

public class LinkManage implements LinkManageInf {
	public HashMap<String , CurrentUser> userMap=CoreMap.getUserMap();
	Jedis jedis,jedis2;
	public LinkManage()
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
			System.out.println("链接管理模块启动失败：连接Redis失败");
		}
		System.out.println("链接管理模块启动");
	}

	@Override
	public void CreateLink(String openID,String masterID, String slaveID) {
		// TODO Auto-generated method stub
		jedis2.lpush(masterID,"master");
		jedis2.lpush(masterID,slaveID);
		jedis2.lpush(slaveID,"slaver");
		jedis2.lpush(slaveID,masterID);
		jedis.set(masterID, "Busy");
		jedis.set(slaveID, "Busy");
		EventNotify eventNotify=new EventNotify();
		eventNotify.notifyCreateLinkEvent(openID, masterID,slaveID);
	}

	@Override
	public void CutLink(String openID,String deviceID) {
		// TODO Auto-generated method stub
		String remoteID="null";
		if(jedis2.exists(deviceID))
		{
			remoteID=jedis2.lpop(deviceID);	
			System.out.println("lpop remoteID: "+remoteID);
			jedis2.del(deviceID);
		}
		if(jedis2.exists(remoteID))
		{
			jedis2.del(remoteID);	
		}
		if(jedis.exists(deviceID))
		{
			jedis.set(deviceID, "OnLine");	
		}
		if(jedis.exists(remoteID))
		{
			jedis.set(remoteID, "OnLine");	
		}
		EventNotify eventNotify=new EventNotify();
		eventNotify.notifyCuteLinkEvent(openID, deviceID, remoteID);
	}
}
