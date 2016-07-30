package ywcai.ls.desk.protocol;

public class ProtocolResByte implements MesResInf {
	private byte dataType,reqType;
	private int dataLenth;
	private String token;
	private byte[] data;
	public ProtocolResByte(DecodeHelp dHelp) {
		// TODO Auto-generated constructor stub
		this.dataType=dHelp.dataType;
		this.reqType=dHelp.reqType;
		this.dataLenth=dHelp.dataLenth;
		this.token=dHelp.token;
		this.data=dHelp.data;
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
