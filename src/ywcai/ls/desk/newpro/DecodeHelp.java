package ywcai.ls.desk.newpro;

import ywcai.ls.desk.cfg.MyConfig;

public class DecodeHelp
{
	public int dataLenth,needDataLenth,dataPos;
	public byte[] data,head,token;
	public byte reqType;
	public int needHeadLenth,headPos;
	public boolean isHead;

	public void init()
	{
		this.data=null;
		this.head=new byte[MyConfig.INT_SOCKET_HEAD_LEN];
		this.dataLenth=0;
		this.needHeadLenth=MyConfig.INT_SOCKET_HEAD_LEN;
		this.isHead=true;
		this.headPos=0;
		this.dataPos=0;
	}
	public void encodeHead()
	{
		this.reqType=getReqType(head);
		this.dataLenth=getDataLenth(head);
		this.token=getToken(head);
		this.needDataLenth=dataLenth;
		this.needHeadLenth=MyConfig.INT_SOCKET_HEAD_LEN;
		this.data=new byte[dataLenth];
		this.head=new byte[MyConfig.INT_SOCKET_HEAD_LEN];
		this.dataPos=0;
		this.headPos=0;
		
	}
	private byte getReqType(byte[] head)
	{
		return head[0];
	}

	private int  getDataLenth(byte[] head)
	{   
		int value= 0;
		for (int i = 0; i < 4; i++) {
			int shift= (4 - 1 - i) * 8;
			value +=(head[i + MyConfig.INT_SOCKET_LENTH_POS ] & 0x000000FF) << shift;
		}
		return value;
	}
	
	private byte[]  getToken(byte[] head)
	{   
		byte[] temp=new byte[16];
		for(int i=0;i<16;i++)
		{
			temp[i]=head[i+ MyConfig.INT_SOCKET_TOKEN_POS];
		}
		return temp;
	}
}
