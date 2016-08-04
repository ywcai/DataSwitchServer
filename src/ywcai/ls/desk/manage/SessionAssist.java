package ywcai.ls.desk.manage;

public class SessionAssist {
	public String token;
	public boolean isCtrl,isConn;
	public String remoteIp;
	public String nickname;
	public int dreviceType;
	public long tempID;
	public boolean isNormalClose;
	@Override
	public String toString() {
		return "SessionAssist [token=" + token + ", isCtrl=" + isCtrl + ", isConn=" + isConn + ", remoteIp=" + remoteIp
				+ ", nickname=" + nickname + ", dreviceType=" + dreviceType + ", tempID=" + tempID + ", isNormalClose="
				+ isNormalClose + "]";
	}
	
}
