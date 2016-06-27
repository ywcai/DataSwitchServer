package ywcai.ls.desk.ui;

import java.util.Scanner;

public class InfoFind implements InfoInf {
	public Scanner scanner;

	public void Init()
	{
		System.out.println("信息查询模块启动");
		while(true)
		{
			System.out.print("ControlServer>");
			scanner=new Scanner(System.in);
			String cmd=scanner.nextLine();
			ExecuteCmd(cmd);
		}
	}

	@Override
	public void ExecuteCmd(String cmd) {
		// TODO Auto-generated method stub
		switch(cmd)
		{
		case "UsersInfo":
			SelectUsersInfo();
			break;
		case "UsersNum":
			SelectUsersNum();
			break;
		case "UsersID":
			SelectUsersID();
			break;
		case "UsersIP":
			SelectUsersIP();
			break;
		case "":
			break;
		default :
			System.out.println("Info:");
			System.out.println("无效命令");
			break;
		}
	}



	@Override
	public void SelectUsersInfo() {
		// TODO Auto-generated method stub
		System.out.println("Info:");
		System.out.println("AllInfoLists");
	}

	@Override
	public void SelectUsersNum() {
		// TODO Auto-generated method stub
		System.out.println("Info:");
		System.out.println("UsersNumLists");
	}

	@Override
	public void SelectUsersID() {
		// TODO Auto-generated method stub
		System.out.println("Info:");
		System.out.println("UsersIDLists");

	}

	@Override
	public void SelectUsersIP() {
		// TODO Auto-generated method stub
		System.out.println("Info:");
		System.out.println("UsersIPLists");
	}

	@Override
	public void SelectUserInfo(String username) {
		// TODO Auto-generated method stub
		System.out.println("Info:"+username);
		System.out.println("UserInfoLists");
	}

	@Override
	public void SelectUserInfo(int userid) {
		// TODO Auto-generated method stub
		System.out.println("Info:"+userid);
		System.out.println("UserInfoLists");
	}

	@Override
	public void SelectUserIP(String username) {
		// TODO Auto-generated method stub
		System.out.println("Info:"+username);
		System.out.println("UserIP");
	}

	@Override
	public void SelectUserIP(int userid) {
		// TODO Auto-generated method stub
		System.out.println("Info:"+userid);
		System.out.println("UserIP");
	}

	@Override
	public void SelectUserOnlineTime(String username) {
		// TODO Auto-generated method stub
		System.out.println("Info:"+username);
		System.out.println("OnlineTime");
	}

	@Override
	public void SelectUserOnlineTime(int userid) {
		// TODO Auto-generated method stub
		System.out.println("Info:"+userid);
		System.out.println("UserIP");
	}

}
