package ywcai.ls.desk.newpro;


import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import ywcai.ls.desk.cfg.MyConfig;


public class MesDecode implements MessageDecoder  {

	@Override
	public MessageDecoderResult decodable(IoSession ioSession, IoBuffer ioBuffer) {
		// TODO Auto-generated method stub	
		DecodeHelp dHelp=new DecodeHelp();
		dHelp.init();
		ioSession.setAttribute("dp",dHelp);
		if (ioBuffer.remaining()< MyConfig.INT_SOCKET_HEAD_LEN)
		{
			return MessageDecoderResult.NEED_DATA;
		}	
		byte tag = ioBuffer.get();

		if(tag==MyConfig.INT_PROTOCOL_HEAD_FLAG)
		{
			return MessageDecoderResult.OK;
		}
		return MessageDecoderResult.NOT_OK;
	}

	@Override
	public MessageDecoderResult decode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput out) throws Exception {
		// TODO Auto-generated method stub
		DecodeHelp dHelp=(DecodeHelp)ioSession.getAttribute("dp");

		while(ioBuffer.hasRemaining())
		{
			if(dHelp.isHead)
			{

				if( ioBuffer.remaining()<(dHelp.needHeadLenth) )
				{
					dHelp.needHeadLenth=dHelp.needHeadLenth-ioBuffer.remaining();		
					ioBuffer.get(dHelp.head,dHelp.headPos,ioBuffer.remaining());			
					dHelp.headPos=MyConfig.INT_SOCKET_HEAD_LEN-dHelp.needHeadLenth;
					return MessageDecoderResult.NEED_DATA;
				}
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
		MesInf mes = new ProtocolResponse(dHelp);
		out.write(mes);
	}

	@Override
	public void finishDecode(IoSession ioSession, ProtocolDecoderOutput out) throws Exception {
		// TODO Auto-generated method stub

	}
}
