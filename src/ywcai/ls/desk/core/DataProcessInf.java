package ywcai.ls.desk.core;

import org.apache.mina.core.session.IoSession;

import ywcai.ls.desk.manage.LinkManageInf;
import ywcai.ls.desk.manage.SessionManageInf;

public interface DataProcessInf {
	public void processReciveEvent(IoSession session,Object message,SessionManageInf sessionManageInf,LinkManageInf linkManageInf);
	public void processSentEvent(IoSession session,Object message,SessionManageInf sessionManageInf,LinkManageInf linkManageInf);
	public void processCloseEvent(IoSession session,SessionManageInf sessionManageInf,LinkManageInf linkManageInf);

}
