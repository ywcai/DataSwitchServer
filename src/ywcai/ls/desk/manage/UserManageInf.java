package ywcai.ls.desk.manage;

import org.apache.mina.core.session.IoSession;

public interface UserManageInf {
	public void CreateUser(String Username,IoSession master,IoSession slave);
	public void RemoveUser(String Username,IoSession ioSession);
}
