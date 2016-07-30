package ywcai.ls.desk.protocol;

import java.io.UnsupportedEncodingException;

public class ProtocolResString implements MesResInf {
	private byte dataType,reqType;
	private int dataLenth;
	private String token;
	private String data;
	public ProtocolResString(DecodeHelp dHelp) {
		// TODO Auto-generated constructor stub
		this.dataType=dHelp.dataType;
		this.reqType=dHelp.reqType;
		this.dataLenth=dHelp.dataLenth;
		this.token=dHelp.token;
		try {
			this.data = new String(dHelp.data,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			this.data="data decode err";
		}
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
