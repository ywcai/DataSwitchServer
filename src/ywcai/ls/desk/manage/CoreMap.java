package ywcai.ls.desk.manage;

import java.util.HashMap;

import org.apache.mina.core.session.IoSession;

public class CoreMap {
	private static HashMap<String,CurrentUser> userMap=null;
	private static HashMap<String,IoSession> sessionMap=null;
	private CoreMap()
	{
		;
	}
	public static synchronized  HashMap<String,CurrentUser> getUserMap()
	{
		if(userMap==null)
		{
			userMap=new HashMap<String,CurrentUser>();
		}
		return userMap;
	}
//	public static synchronized  HashMap<String,String> getTokenMap()
//	{
//		if(tokenMap==null)
//		{
//			tokenMap=new HashMap<String,String>();
//		}
//		return tokenMap;
//	}
	public static synchronized  HashMap<String,IoSession> getSessionMap()
	{
		if(sessionMap==null)
		{
			sessionMap=new HashMap<String,IoSession>();
		}
		return sessionMap;
	}

	
}
