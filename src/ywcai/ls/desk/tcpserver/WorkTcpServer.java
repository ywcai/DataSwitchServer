package ywcai.ls.desk.tcpserver;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import ywcai.ls.desk.cfg.MyConfig;
import ywcai.ls.desk.core.DataProcessInf;
import ywcai.ls.desk.manage.SessionManageInf;
import ywcai.ls.desk.manage.UserManageInf;
import ywcai.ls.desk.protocol.CodeFactory;
import ywcai.ls.desk.protocol.MesEncode;

public class WorkTcpServer  extends IoHandlerAdapter {
	private int PORT=MyConfig.INT_SERVER_PORT;
	private int bufferSize=MyConfig.INT_READ_BUFFERSIZE;
	private DataProcessInf dataProcessInf;
	private SessionManageInf sessionManageInf;
	private UserManageInf userManageInf;
	public void Init(SessionManageInf sessionManage, UserManageInf userManage, DataProcessInf dataProcess)
	{
		this.sessionManageInf=sessionManage;
		this.userManageInf=userManage;
		this.dataProcessInf=dataProcess;
		NioSocketAcceptor acceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors()+1);
		acceptor.setHandler(this);
		acceptor.getSessionConfig().setReceiveBufferSize(bufferSize);
		acceptor.getFilterChain().addFirst
		("codec",new ProtocolCodecFilter(new CodeFactory(new MesEncode(Charset.forName("utf-8")))));
		
		acceptor.getFilterChain().addLast("ThreadPools",new ExecutorFilter(Executors.newCachedThreadPool()));
		try
		{
			acceptor.bind(new InetSocketAddress(PORT));
		} 
		catch (Exception e) 
		{

		}
		if(acceptor.isActive())
		{
			System.out.println("核心通信模块启动");
		}
		else
		{
			System.out.println("核心通信模块启动失败");
		}
	}
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		super.exceptionCaught(session, cause);
		//System.out.println("session :"+session+", cause : "+cause.toString());
		dataProcessInf.processCloseEvent(session, sessionManageInf, userManageInf);
		//System.out.println("session :"+session+", cause : "+cause.getMessage());
	}
	@Override
	public void inputClosed(IoSession session) throws Exception {
		super.inputClosed(session);
	}
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		super.messageReceived(session, message);
		dataProcessInf.processReciveEvent(session, message,sessionManageInf,userManageInf);
	}
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		super.messageSent(session, message);
		dataProcessInf.processSentEvent(session, message,sessionManageInf,userManageInf);
	}
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);		
		dataProcessInf.processCloseEvent(session, sessionManageInf, userManageInf);
	}
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);

	}
	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		super.sessionIdle(session, status);
	}
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		super.sessionOpened(session);
		System.out.println("session opened ! ip : "+session.getRemoteAddress());
	}
}
