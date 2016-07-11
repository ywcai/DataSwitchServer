package ywcai.ls.desk.protocol;

import java.nio.charset.Charset;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

public class MesDecode implements MessageDecoder  {
	public Charset charset;
	private String username="";
	private int namelenth=0;
	public MesDecode() {
		this.charset = Charset.forName("utf-8");
	}
	@Override
	public MessageDecoderResult decodable(IoSession ioSession, IoBuffer ioBuffer) {
		// TODO Auto-generated method stub

		if (ioBuffer.remaining()<9) 
		{
			System.out.println("数据接收不完整");
			return MessageDecoderResult.NEED_DATA;
		}	

		byte tag = (byte)ioBuffer.get();
		
		//协助处理断包和粘包的变量
		DecodeHelp dHelp=new DecodeHelp();
		dHelp.data=null;
		dHelp.dataLenth=0;
		dHelp.dataPending=0;
		dHelp.canDecode=true;
		dHelp.tag=tag;
		ioSession.setAttribute("dp",dHelp);
		
		if(tag==0x06) 
		{
			//System.out.println("decode normal tag："+tag+"  the remining: "+ioBuffer.remaining()+" limit: "+ioBuffer.limit()+" cap: "+ioBuffer.capacity()+" pos:"+ioBuffer.position());
			return MessageDecoderResult.OK;
		}
		if(tag==0x01||tag==0x02||tag==0x03||tag==0x04||tag==0x05||tag==0x07) 
		{
			//System.out.println("decode normal tag："+tag+"  the remining: "+ioBuffer.remaining()+" limit: "+ioBuffer.limit()+" cap: "+ioBuffer.capacity()+" pos:"+ioBuffer.position());
			return MessageDecoderResult.OK;
		}
		System.out.println("decodable unknow tag："+tag+"  the remining: "+ioBuffer.remaining()+" limit: "+ioBuffer.limit()+" cap: "+ioBuffer.capacity()+" pos:"+ioBuffer.position());
		return MessageDecoderResult.NOT_OK;
	}

	@Override
	public MessageDecoderResult decode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput out) throws Exception {
		// TODO Auto-generated method stub
		DecodeHelp dHelp=(DecodeHelp)ioSession.getAttribute("dp");
		
		
//		byte[] data=(byte[])ioSession.getAttribute("data");
//		boolean newPackflag=(boolean)ioSession.getAttribute("newPackage");
//		int datalenth=(int)ioSession.getAttribute("dataLenth");
//		int dataPending=(int)ioSession.getAttribute("dataPending");

		while(ioBuffer.hasRemaining())
		{
			//System.out.println("hasRemaining :"+ioBuffer.remaining()+"id: "+ioSession.getId());
			//如果是新包或粘包，则要解析包头。新包上面已经预处理，起始不存在这个问题
			if(dHelp.canDecode)
			{
				int nowPos=ioBuffer.position();
				dHelp.tag=ioBuffer.get();				
				//如果新包或粘包的标识位不是服务器协商允许的标识，直接返回notok，丢弃数据；
				if(dHelp.tag>0x07||dHelp.tag<0x01)
				{
					return MessageDecoderResult.NOT_OK;
				}
				ioBuffer.position(nowPos+1);
				namelenth=ioBuffer.getInt();
				ioBuffer.position(nowPos+5);
				dHelp.dataLenth=ioBuffer.getInt();
				dHelp.dataPending=dHelp.dataLenth;
				//ioSession.setAttribute("dataLenth",datalenth);
				
				ioBuffer.position(nowPos+9);
				username=ioBuffer.getString(namelenth,charset.newDecoder());
				ioBuffer.position(nowPos+9+namelenth);
						
				byte[] temp=new byte[dHelp.dataLenth];
				dHelp.data=temp;				
			}

			//如果需要接收的数据大于现在已有的数据，说明存在断包，需要继续接收数据，首先对本次数据进行缓存。
			if(dHelp.dataPending>ioBuffer.remaining())
			{
				int getLenth=ioBuffer.remaining();
				ioBuffer.get(dHelp.data,dHelp.dataLenth-dHelp.dataPending,ioBuffer.remaining());

				dHelp.dataPending=dHelp.dataPending-getLenth;

				dHelp.canDecode=false;

				//System.out.println("处理断包 ：remining: "+ioBuffer.remaining()+" datapending : "+dataPending+" postion :"+ioBuffer.position()+" datalenth :"+datalenth+" cap :"+ioBuffer.capacity()+" lim :"+ioBuffer.limit()) ;
				return MessageDecoderResult.NEED_DATA;
			}	
			//如果需要接收的数据小于或者等于已接收到的数据，说明能组装成正常包。
			else
			{
				int getLenth=dHelp.dataPending;
				ioBuffer.get(dHelp.data,dHelp.dataLenth-dHelp.dataPending,getLenth);
				dHelp.dataPending=dHelp.dataPending-getLenth;
				putMessage(dHelp.tag,namelenth,dHelp.dataLenth,username,dHelp.data,out);
				dHelp.canDecode=true;
			}
			//System.out.println("输出：tag："+tag+" remining: "+ioBuffer.remaining()+" limit: "+ioBuffer.limit()+" cap: "+ioBuffer.capacity()+" pos:"+ioBuffer.position());
		}	
		return MessageDecoderResult.OK;
	}

	private void putMessage(byte tag,int namelenth,int datalenth,String username,byte[] data,ProtocolDecoderOutput out)
	{		
		//解码
		MesResInf mesRes;
		if(tag==0x06)
		{
			mesRes=new ProtocolResByte();
			mesRes.setTag(tag);
			mesRes.setUsernameLenth(namelenth);
			mesRes.setDataLenth(datalenth);
			mesRes.setUsername(username);
			mesRes.setData(data);
			System.out.println("recived img data  lenth is  : "+data.length);
		}
		else
		{
			String mesString=new String(data,charset);
			mesRes=new ProtocolResString();
			mesRes.setTag(tag);
			mesRes.setUsernameLenth(namelenth);
			mesRes.setDataLenth(datalenth);
			mesRes.setUsername(username);
			mesRes.setData(mesString);
			System.out.println("recived String is  : "+mesString);
		}
		out.write(mesRes);
	
	}

	@Override
	public void finishDecode(IoSession ioSession, ProtocolDecoderOutput out) throws Exception {
		// TODO Auto-generated method stub

	}



}
