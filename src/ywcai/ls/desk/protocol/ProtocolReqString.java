package ywcai.ls.desk.protocol;

import java.io.UnsupportedEncodingException;

import ywcai.ls.desk.cfg.MyConfig;

public class ProtocolReqString implements MesReqInf{
	private byte dataType,reqType;
	private int dataLenth;
	private String token;
	private String data;
	public ProtocolReqString(byte pReqType,String pToken,String pData)
	{
		// TODO Auto-generated constructor stub
		this.dataType=(byte)MyConfig.PROTOCOL_HEAD_TYPE_JSON;
		this.reqType=pReqType;	
		try {
			this.dataLenth=pData.getBytes("utf-8").length;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
		    //e.printStackTrace();
			this.dataLenth=0;
		}
		this.token=pToken;
		this.data=pData;
	}
	
	@Override
	public byte getReqType() {
		// TODO Auto-generated method stub
		return reqType;
	}
	@Override
	public byte getDataType() {
		// TODO Auto-generated method stub
		return dataType;
	}
	@Override
	public int getDataLenth() {
		// TODO Auto-generated method stub
		return dataLenth;
	}
	@Override
	public String getToken() {
		// TODO Auto-generated method stub
		return token;
	}
	@Override
	public Object getData() {
		// TODO Auto-generated method stub
		return data;
	}
}
