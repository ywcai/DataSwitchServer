package ywcai.ls.desk.core;

import java.util.HashMap;
import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.json.JSONException;
import org.json.JSONObject;

import redis.clients.jedis.Jedis;
import ywcai.ls.desk.cfg.MyUtil;
import ywcai.ls.desk.cfg.ResultCode;
import ywcai.ls.desk.manage.CoreMap;
import ywcai.ls.desk.manage.SessionAssist;

public class EventNotify {

	HashMap<String,IoSession> sessionMap=CoreMap.getSessionMap();
	public void notifyOnlineEvent(String openID,DeviceInfo deviceInfo)
	{

		Jedis jedis2=new Jedis("127.0.0.1");
		jedis2.select(2);
		List<String> list=jedis2.lrange(openID, 0, -1);
		System.out.println("10: "+list.size());
		JSONObject content=new JSONObject();
		try {
			content.put("deviceID", deviceInfo.deviceID);
			content.put("deviceName", deviceInfo.deviceName);
			content.put("deviceType", deviceInfo.deviceType);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(String deviceID:list)
		{
			if(sessionMap.containsKey(deviceID))
			{
				IoSession session=sessionMap.get(deviceID);
				SessionAssist sa=(SessionAssist) session.getAttribute("sa");
				if(!sa.deviceInfo.deviceID.equals(deviceInfo.deviceID))
				{
					MyUtil.sendJson(sessionMap.get(deviceID),ResultCode.json_type_notify_turn_on,content.toString());
				}
			}
			else
			{
				jedis2.lrem(openID, 10, deviceID);
			}
			//loop notify client
		}
	}
	public void notifyOfflineEvent(String openID,DeviceInfo deviceInfo)
	{
		Jedis jedis2=new Jedis("127.0.0.1");
		jedis2.select(2);
		List<String> list=jedis2.lrange(openID, 0, -1);
		for(String deviceID:list)
		{
			//loop notify client
			if(sessionMap.containsKey(deviceID))
			{
				if(sessionMap.get(deviceID).isConnected())
				{
					System.out.println("to deivceID : "+deviceID+" session: "+sessionMap.get(deviceID).toString()+" content : "+deviceInfo.deviceID);
					MyUtil.sendJson(sessionMap.get(deviceID),ResultCode.json_type_notify_turn_off,deviceInfo.deviceID);
					System.out.println("notify turn off success");
				}
				else
				{
					jedis2.lrem(openID, 10, deviceID);
				}
			}
			else
			{
				jedis2.lrem(openID, 10, deviceID);
			}
		}	
	}
	public void notifyCreateLinkEvent(String openID,String masteID,String slaveId)
	{
		Jedis jedis2=new Jedis("127.0.0.1");
		jedis2.select(2);
		List<String> list=jedis2.lrange(openID, 0, -1);
		for(String deviceID:list)
		{
			//loop notify client
			if(sessionMap.containsKey(deviceID))
			{
				IoSession session=sessionMap.get(deviceID);
				if(session.isConnected()&&(!session.isClosing()))
				{
					MyUtil.sendJson(session,ResultCode.json_type_notify_link_up,masteID);
					MyUtil.sendJson(session,ResultCode.json_type_notify_link_up,slaveId);
				}
				else
				{
					jedis2.lrem(openID, 10, deviceID);
				}
			}
			else
			{
				jedis2.lrem(openID, 10, deviceID);
			}
		}	
	}
	public void notifyCuteLinkEvent(String openID,String one,String two)
	{
		Jedis jedis2=new Jedis("127.0.0.1");
		jedis2.select(2);
		List<String> list=jedis2.lrange(openID, 0, -1);
		for(String deviceID:list)
		{
			//loop notify client
			if(sessionMap.containsKey(deviceID))
			{
				if(sessionMap.get(deviceID).isConnected())
				{
					MyUtil.sendJson(sessionMap.get(deviceID),ResultCode.json_type_notify_link_down,one);
					MyUtil.sendJson(sessionMap.get(deviceID),ResultCode.json_type_notify_link_down,two);
				}
				else
				{
					jedis2.lrem(openID, 10, deviceID);
				}
			}
			else
			{
				jedis2.lrem(openID, 10, deviceID);
			}
		}	
	}

}
