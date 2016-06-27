package ywcai.ls.desk.core;

import org.apache.mina.core.session.IoSession;

import ywcai.ls.desk.manage.SessionManageInf;
import ywcai.ls.desk.manage.UserManageInf;
import ywcai.ls.desk.protocol.ProtocolRes;

public interface DataProcessInf {
	public void Process(IoSession session,ProtocolRes protocolRes,SessionManageInf sessionManageInf,UserManageInf userManageInf);


}
