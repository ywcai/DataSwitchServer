package ywcai.ls.desk.ui;

public interface InfoInf {
	
	public void ExecuteCmd(String cmd);
	
	public void SelectUsersInfo();
	public void SelectUsersNum();
	public void SelectUsersID();//id+name
	public void SelectUsersIP();//login +remote 


	public void SelectUserInfo(String username);
	public void SelectUserInfo(int userid);
	public void SelectUserIP(String username);
	public void SelectUserIP(int userid);
	public void SelectUserOnlineTime(String username);
	public void SelectUserOnlineTime(int userid);
}
