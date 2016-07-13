package ywcai.ls.desk.protocol;

import java.nio.charset.Charset;


import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

public class MesEncode implements MessageEncoder<MesReqInf>{

	public Charset charset;
	public MesEncode(Charset charset){
		this.charset=charset;
	}
	@Override
	public void encode(IoSession ioSession, MesReqInf msg, ProtocolEncoderOutput out) throws Exception {
		// TODO Auto-generated method stub

		if(msg instanceof MesReqInf)
		{
			MesReqInf req= msg;
			int bufSize=req.getDataLenth()+req.getNameLenth()+9;
			IoBuffer buf=IoBuffer.allocate(bufSize).setAutoExpand(true);
			buf.put(req.getTag());
			buf.putInt(req.getNameLenth());
			buf.putInt(req.getDataLenth());
			buf.putString(req.getUserName(),charset.newEncoder());
			if(req.getTag()==0x06)
			{
				buf.put((byte[])req.getData());
			}
			else
			{
				buf.putString(req.getData().toString(),charset.newEncoder());
			}
			buf.flip();
			out.write(buf);
		}
		else
		{
			System.out.println("传输数据协议不对");
		}

	}

}
