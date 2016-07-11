package ywcai.ls.desk.manage;

import java.util.Date;


import org.apache.mina.core.session.IoSession;


public class CurrentUser {
	public Date onLineStart;
	public String userName;
	public IoSession masterSession,slaveSession;
	public CurrentUser(String pUsername,IoSession master,IoSession slave)
	{
		this.onLineStart=new Date();
		this.userName=pUsername;
		this.masterSession=master;
		this.slaveSession=slave;
	}
	public String getOnlineTime()
	{
		Date nowTime=new Date();
		long onlineTime=nowTime.getTime()-onLineStart.getTime();
		int day=(int)onlineTime/(24*60*60*1000);
		int hour=(int)(onlineTime/(60*60*1000)-day*24);
		int min=(int)((onlineTime/(60*1000))-day*24*60-hour*60);
		int sec=(int)(onlineTime/1000-day*24*60*60-hour*60*60-min*60);
		String online=day+"天"+hour+"小时"+min+"分"+sec+"秒";
		return online;
	}
}
