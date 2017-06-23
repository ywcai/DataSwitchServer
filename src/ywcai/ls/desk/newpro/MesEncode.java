package ywcai.ls.desk.newpro;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import ywcai.ls.desk.cfg.MyConfig;



public class MesEncode implements MessageEncoder<MesInf>{
	@Override
	public void encode(IoSession ioSession, MesInf msg, ProtocolEncoderOutput out) throws Exception {
		// TODO Auto-generated method stub
		if(msg instanceof MesInf)
		{
			MesInf req= msg;
			int bufSize=req.getDataLength()+ MyConfig.INT_SOCKET_HEAD_LEN;
			IoBuffer buf=IoBuffer.allocate(bufSize);
			buf.put(req.getReqFlag());
			buf.put(req.getToken());
			buf.putInt(req.getDataLength());
			buf.put(req.getData());
			buf.flip();
			out.write(buf);
		}
	}

}
