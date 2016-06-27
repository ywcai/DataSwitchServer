package ywcai.ls.desk.tcpserver;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import ywcai.ls.desk.account.AccountManageInf;
import ywcai.ls.desk.control.ControlServer;

public class AuthenTcpServer  extends IoHandlerAdapter {
	public int PORT=7773;
	private AccountManageInf accountManageInf;
	public void Init(AccountManageInf pAccountManageInf)
	{
		accountManageInf=pAccountManageInf;
		NioSocketAcceptor acceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors()+1);
		acceptor.setHandler(this);
		acceptor.getFilterChain().addFirst
		("codec",
				new ProtocolCodecFilter(
						new TextLineCodecFactory(
								Charset.forName("UTF-8"),
								LineDelimiter.WINDOWS.getValue(),
								LineDelimiter.WINDOWS.getValue()
								)
						)
				);	
		acceptor.getFilterChain().addLast("ThreadPools",new ExecutorFilter(Executors.newCachedThreadPool()));
		try {
		acceptor.bind(new InetSocketAddress(PORT));
		} catch (Exception e) {

		}
		if(acceptor.isActive())
		{
			System.out.println("认证通信模块启动");
		}
		else
		{
			System.out.println("认证网络模块启动失败");
		}
	}
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		super.exceptionCaught(session, cause);
	}
	
	@Override
	public void inputClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.inputClosed(session);
	}
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		accountManageInf.Login(session, message.toString());
	}
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		super.messageSent(session, message);
		
	}
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);
		ControlServer.logger.info("认证模块关闭了sesion: {} ",session );
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
		ControlServer.logger.info("认证模块打开了sesion: {} ",session );
		
	}


}
