package ywcai.ls.desk.manage;

import org.apache.mina.core.session.IoSession;

public interface UserManageInf {
	public void CreateUser(String token,IoSession master,IoSession slave);
	public void RemoveUser(SessionAssist sAssist,IoSession ioSession);
}
