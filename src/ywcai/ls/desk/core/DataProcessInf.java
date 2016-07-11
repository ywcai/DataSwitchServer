package ywcai.ls.desk.core;

import org.apache.mina.core.session.IoSession;

import ywcai.ls.desk.manage.SessionManageInf;
import ywcai.ls.desk.manage.UserManageInf;

public interface DataProcessInf {
	public void processReciveEvent(IoSession session,Object message,SessionManageInf sessionManageInf,UserManageInf userManageInf);
	public void processSentEvent(IoSession session,Object message,SessionManageInf sessionManageInf,UserManageInf userManageInf);
	public void processCloseEvent(IoSession session,SessionManageInf sessionManageInf,UserManageInf userManageInf);

}
