package ywcai.ls.desk.manage;
import org.apache.mina.core.session.IoSession;

import ywcai.ls.desk.core.DeviceInfo;

public interface SessionManageInf{
public void addSession(String openid,DeviceInfo deviceInfo,IoSession session);
public void removeSession(String openid,DeviceInfo deviceInfo);
public IoSession getSession(DeviceInfo deviceInfo);
}
