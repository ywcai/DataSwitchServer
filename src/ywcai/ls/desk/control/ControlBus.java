package ywcai.ls.desk.control;



import java.util.HashMap;

import ywcai.ls.desk.account.AccountManage;
import ywcai.ls.desk.account.AccountManageInf;
import ywcai.ls.desk.core.DataProcess;
import ywcai.ls.desk.core.DataProcessInf;
import ywcai.ls.desk.manage.CoreMap;
import ywcai.ls.desk.manage.CurrentUser;
import ywcai.ls.desk.manage.SessionManage;
import ywcai.ls.desk.manage.SessionManageInf;
import ywcai.ls.desk.manage.UserManage;
import ywcai.ls.desk.manage.UserManageInf;
import ywcai.ls.desk.tcpserver.AuthenTcpServer;
import ywcai.ls.desk.tcpserver.WorkTcpServer;
import ywcai.ls.desk.ui.InfoFind;

public class ControlBus {
	
	public void Init()
	{
		HashMap<String, CurrentUser> userMap=CoreMap.getUserMap();
		userMap.clear();
		//需要一个静态的HashMap，查找来自己不同用户对象信息
		
		//进行账号验证，需要单独的网络模块进行处理.//这不考虑实现
		AccountManageInf accountManageInf=new AccountManage();
		
		AuthenTcpServer authenTcpServer =new AuthenTcpServer();
		authenTcpServer.Init(accountManageInf);
		
		UserManageInf userManageInf=new UserManage();
		//如果创建用户成功，则对在线的session进行管理。也可在核心数据处理时进行初始化，则在dataProcess实例里实例化。
		SessionManageInf sessionManageInf=new SessionManage(userManageInf);

		
		//核心数据处理模块
		DataProcessInf dataProcessInf=new DataProcess();
		
		//网络初始化，网络模块需要 session管理和数据处理模块的引用
		WorkTcpServer workTcpServer=new WorkTcpServer();
		workTcpServer.Init(sessionManageInf,userManageInf,dataProcessInf);
	
		//信息查询，需要hash表;信息查询必须在最后面，因为是阻塞了输入流
		InfoFind infoFind=new InfoFind();
		infoFind.Init();	
	}
	
}
