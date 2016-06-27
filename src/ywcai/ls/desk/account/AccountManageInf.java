package ywcai.ls.desk.account;

import org.apache.mina.core.session.IoSession;

public interface AccountManageInf {
	public void Login(IoSession session,String message);
	public void LoginOut(IoSession session,String message);
	public String GetToken(IoSession session,String message);
	public void Response(IoSession session,String message,boolean login);
	public boolean CheckPsw(String pUserName,String pPsw);
	
}
