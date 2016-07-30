package ywcai.ls.desk.protocol;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import ywcai.ls.desk.cfg.MyConfig;

public class DecodeHelp
{
	public int dataLenth,needDataLenth,dataPos;
	public byte[] data,head;
	public byte reqType,dataType;
	public int needHeadLenth,headPos;
	public int isNeedToke;
	public String token;
	public boolean isHead;

	public void init()
	{
		this.data=null;
		this.head=new byte[MyConfig.INT_PACKAGE_HEAD_LEN];
		this.dataLenth=0;
		this.needHeadLenth=MyConfig.INT_PACKAGE_HEAD_LEN;
		this.isHead=true;
		this.headPos=0;
		this.dataPos=0;
	}
	public void encodeHead()
	{
		this.isNeedToke=getHasToken(head);
		this.dataType=getDataType(head);
		this.reqType=getReqType(head);
		this.dataLenth=getDataLenth(head);
		this.token=getToken(head);
		
		this.needDataLenth=dataLenth;
		this.needHeadLenth=MyConfig.INT_PACKAGE_HEAD_LEN;
		this.data=new byte[dataLenth];
		this.head=new byte[MyConfig.INT_PACKAGE_HEAD_LEN];
		this.dataPos=0;
		this.headPos=0;
		
	}
	
	
	private byte getHasToken(byte[] head)
	{
		return head[1];
	}
	
	private byte getDataType(byte[] head)
	{
		return head[2];
	}
	
	private byte getReqType(byte[] head)
	{
		return head[3];
	}

	private int  getDataLenth(byte[] head)
	{   
		int value= 0;
		for (int i = 0; i < 4; i++) {
			int shift= (4 - 1 - i) * 8;
			value +=(head[i + 20] & 0x000000FF) << shift;
		}
		return value;
	}
	
	private String  getToken(byte[] head)
	{   
		byte[] temp=new byte[16];
		for(int i=0;i<16;i++)
		{
			temp[i]=head[i+4];
		}
		String str="";
		try {
			str = new String(temp,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			str="token decode err";
		}
		return str;
	}
	
	@Override
	public String toString() {
		return "DecodeHelp [dataLenth=" + dataLenth + ", needDataLenth=" + needDataLenth + ", dataPos=" + dataPos
				+ ", data=" + Arrays.toString(data) + ", head=" + Arrays.toString(head) + ", reqType=" + reqType
				+ ", dataType=" + dataType + ", needHeadLenth=" + needHeadLenth + ", headPos=" + headPos + ", needToke="
				+ isNeedToke + ", token=" + token + ", isHead=" + isHead + "]";
	}
	
	
	
	
	
}
