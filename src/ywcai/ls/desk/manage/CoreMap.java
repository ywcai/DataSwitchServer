package ywcai.ls.desk.manage;

import java.util.HashMap;
import java.util.List;

import org.apache.mina.core.session.IoSession;

public class CoreMap {
	private static HashMap<String,CurrentUser> userMap=null;
	private static HashMap<String,String> tokenMap=null;
	private static HashMap<String,List<IoSession>> sessionMap=null;
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
	public static synchronized  HashMap<String,String> getTokenMap()
	{
		if(tokenMap==null)
		{
			tokenMap=new HashMap<String,String>();
		}
		return tokenMap;
	}
	public static synchronized  HashMap<String,List<IoSession>> getSessionMap()
	{
		if(sessionMap==null)
		{
			sessionMap=new HashMap<String,List<IoSession>>();
		}
		return sessionMap;
	}

	
}
