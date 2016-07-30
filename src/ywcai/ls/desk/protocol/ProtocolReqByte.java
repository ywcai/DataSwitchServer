package ywcai.ls.desk.protocol;

import ywcai.ls.desk.cfg.MyConfig;

public class ProtocolReqByte implements MesReqInf{
	private byte dataType,reqType;
	private int dataLenth;
	private String token;
	private byte[] data;
	public ProtocolReqByte(byte pReqType,String pToken,byte[] pData) {
		// TODO Auto-generated constructor stub
		this.dataType=(byte)MyConfig.PROTOCOL_HEAD_TYPE_IMG;
		this.reqType=pReqType;
		this.dataLenth=pData.length;
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
