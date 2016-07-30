package ywcai.ls.desk.protocol;

import java.nio.charset.Charset;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import ywcai.ls.desk.cfg.MyConfig;

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
			int bufSize=req.getDataLenth()+MyConfig.INT_PACKAGE_HEAD_LEN;
			IoBuffer buf=IoBuffer.allocate(bufSize);
			buf.put((byte)MyConfig.PROTOCOL_HEAD_FLAG);
			buf.put((byte)MyConfig.PROTOCOL_HEAD_HAS_TOKEN);
			buf.put(req.getDataType());
			buf.put(req.getReqType());
			buf.putString(req.getToken(),charset.newEncoder());//16位，不足16位需要补齐
			buf.putInt(req.getDataLenth());
			buf.putInt(MyConfig.PROTOCOL_HEAD_RESERVE);//预留位
			if(req.getDataType()==MyConfig.PROTOCOL_HEAD_TYPE_IMG)
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
