package ywcai.ls.desk.protocol;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import ywcai.ls.desk.cfg.MyConfig;

public class MesDecode implements MessageDecoder  {
	public MesDecode() {
	}
	
	@Override
	public MessageDecoderResult decodable(IoSession ioSession, IoBuffer ioBuffer) {
		// TODO Auto-generated method stub	
		DecodeHelp dHelp=new DecodeHelp();
		dHelp.init();
		ioSession.setAttribute("dp",dHelp);
		if (ioBuffer.remaining()<MyConfig.INT_PACKAGE_HEAD_LEN) 
		{
			System.out.println("need more data");
			return MessageDecoderResult.NEED_DATA;
		}	
		byte tag = (byte)ioBuffer.get();
		//协助处理断包和粘包的变量
		if(tag==MyConfig.PROTOCOL_HEAD_FLAG) 
		{
			return MessageDecoderResult.OK;
		}
		System.out.println("decodable unknow tag："+tag+" from session : "+ioSession.getRemoteAddress().toString());
		return MessageDecoderResult.NOT_OK;
	}

	@Override
	public MessageDecoderResult decode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput out) throws Exception {
		// TODO Auto-generated method stub

		DecodeHelp dHelp=(DecodeHelp)ioSession.getAttribute("dp");
	
		while(ioBuffer.hasRemaining())
		{
			//如果是新包或粘包，则要解析包头。
			if(dHelp.isHead)
			{

				if( ioBuffer.remaining()<(dHelp.needHeadLenth) )
				{
					//System.out.println("处理包头不齐情况:"+dHelp.toString()+" remain:"+ioBuffer.remaining());	
					//处理这些小字节数据
					dHelp.needHeadLenth=dHelp.needHeadLenth-ioBuffer.remaining();		
					ioBuffer.get(dHelp.head,dHelp.headPos,ioBuffer.remaining());			
					dHelp.headPos=MyConfig.INT_PACKAGE_HEAD_LEN-dHelp.needHeadLenth;
					//System.out.println("处理包头不齐情况:"+dHelp.toString()+" remain:"+ioBuffer.remaining());	
					return MessageDecoderResult.NEED_DATA;
				}
				/*
				 * rebuild package head
				*/
				//System.out.println("解析包头:"+dHelp.toString()+" remain:"+ioBuffer.remaining());	
				ioBuffer.get(dHelp.head,dHelp.headPos,dHelp.needHeadLenth);
				dHelp.encodeHead();
			}
			

			if(dHelp.needDataLenth>ioBuffer.remaining())
			{
				dHelp.needDataLenth=dHelp.needDataLenth-ioBuffer.remaining();
				ioBuffer.get(dHelp.data,dHelp.dataPos,ioBuffer.remaining());
				dHelp.dataPos=dHelp.dataLenth-dHelp.needDataLenth;	
				dHelp.isHead=false;

				return MessageDecoderResult.NEED_DATA;
			}	

			if(dHelp.needDataLenth==ioBuffer.remaining())
			{
				ioBuffer.get(dHelp.data,dHelp.dataLenth-dHelp.needDataLenth,dHelp.needDataLenth);
				putMessage(dHelp,out);
				//返回OK后会自动重置DecodeHelp所有参数
				//System.out.println("完整包 ：remining: "+ioBuffer.remaining()+" postion :"+ioBuffer.position()+" cap :"+ioBuffer.capacity()+" lim :"+ioBuffer.limit()) ;	
				return MessageDecoderResult.OK;
			}

			if(dHelp.needDataLenth<ioBuffer.remaining())
			{
				ioBuffer.get(dHelp.data,dHelp.dataLenth-dHelp.needDataLenth,dHelp.needDataLenth);
				putMessage(dHelp,out);
				dHelp.isHead=true;
			}
		}
		return MessageDecoderResult.OK;
	}

	private void putMessage(DecodeHelp dHelp,ProtocolDecoderOutput out)
	{		
		//解码
		MesResInf mesRes;
		
		if(dHelp.dataType==MyConfig.PROTOCOL_HEAD_TYPE_IMG)
		{
			mesRes=new ProtocolResByte(dHelp);	
		}
		else
		{
			mesRes=new ProtocolResString(dHelp);
		}
		out.write(mesRes);
	}

	@Override
	public void finishDecode(IoSession ioSession, ProtocolDecoderOutput out) throws Exception {
		// TODO Auto-generated method stub

	}



}
