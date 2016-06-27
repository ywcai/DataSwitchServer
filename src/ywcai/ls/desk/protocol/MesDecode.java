package ywcai.ls.desk.protocol;

import java.nio.charset.Charset;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

public class MesDecode implements MessageDecoder {
	public Charset charset;

	public MesDecode(Charset charset) {
		this.charset = charset;
	}

	@Override
	public MessageDecoderResult decodable(IoSession ioSession, IoBuffer ioBuffer) {
		// TODO Auto-generated method stub
		if (ioBuffer.remaining()<9) {
			System.out.println("数据接收不完整");
			return MessageDecoderResult.NEED_DATA;
		}
		byte tag = (byte)ioBuffer.get();
		switch (tag) {
		case 0x01://login
			//System.out.println("login：TAG="+tag);
			break;
		case 0x02://out
			//System.out.println("login out：TAG="+tag);
			break;
		case 0x03://create connect
			//System.out.println("create remote desk：TAG="+tag);
			break;
		case 0x04://disconnect
			//System.out.println("remove remote desk：TAG="+tag);
			break;
		case 0x05://send CMD
			//System.out.println("send CMD：TAG="+tag);
			break;
		case 0x06://send desk data
			//System.out.println("send desk data：TAG="+tag);
			break;
		default:
			System.out.println("unknow tag："+tag);
			return MessageDecoderResult.NOT_OK;
		}
		return MessageDecoderResult.OK;
	}

	@Override
	public MessageDecoderResult decode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput out) throws Exception {
		// TODO Auto-generated method stub
		ioBuffer.position(0);
		byte tag=ioBuffer.get();
		ioBuffer.position(1);
		int namelenth=ioBuffer.getInt(1);
		ioBuffer.position(5);
		int datalenth=ioBuffer.getInt(5);
		ioBuffer.position(9);
		String username=ioBuffer.getString(namelenth,charset.newDecoder());
		ioBuffer.position(9+namelenth);
		String data=ioBuffer.getString(datalenth,charset.newDecoder());
		
		//消费完剩余数据
		ioBuffer.position(ioBuffer.limit());
		
		ProtocolRes protocolRes=new ProtocolRes();
		protocolRes.setTag(tag);
		protocolRes.setUsernameLenth(namelenth);
		protocolRes.setDataLenth(datalenth);
		protocolRes.setUsername(username);
		protocolRes.setData(data);
		out.write(protocolRes);
		return MessageDecoderResult.OK;
	}

	@Override
	public void finishDecode(IoSession ioSession, ProtocolDecoderOutput out) throws Exception {
		// TODO Auto-generated method stub

	}

}
