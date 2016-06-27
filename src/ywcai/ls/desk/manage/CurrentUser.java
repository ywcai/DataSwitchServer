package ywcai.ls.desk.manage;

import java.util.Date;

import org.apache.mina.core.session.IoSession;

public class CurrentUser {
	public long onLineStart=0;
	public String userName;
	public IoSession masterSession,slaveSession;
	public long getOnlineTime()
	{
		long onLineTime=new Date().getTime();
		onLineTime=onLineTime-onLineStart;
		return onLineTime;
	}
}
