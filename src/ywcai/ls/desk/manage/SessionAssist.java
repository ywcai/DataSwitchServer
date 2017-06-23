package ywcai.ls.desk.manage;

import ywcai.ls.desk.core.DeviceInfo;

public class SessionAssist {
	public String openID;
//	public boolean isCtrl,isConn;
	public String deviceIP;
	public DeviceInfo deviceInfo;
	public long linkID;
//	public boolean isNormalClose;
	@Override
	public String toString() {
		return "SessionAssist [openID=" + openID + ", deviceIP=" + deviceIP
				+ ", deviceInfo=" + deviceInfo + ", linkID=" + linkID + "]";
	}

}
