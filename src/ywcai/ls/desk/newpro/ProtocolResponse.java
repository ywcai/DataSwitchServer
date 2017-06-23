package ywcai.ls.desk.newpro;


public class ProtocolResponse implements MesInf {
	private byte reqType;
	private int dataLenth;
	private byte[] data;
	private byte[] token;
	public ProtocolResponse(DecodeHelp dHelp) {
		// TODO Auto-generated constructor stub
		this.reqType=dHelp.reqType;
		this.dataLenth=dHelp.dataLenth;
		this.data=dHelp.data;
		this.token=dHelp.token;

	}
	@Override
	public byte getReqFlag() {
		return reqType;
	}

	@Override
	public byte[] getToken() {
		return token;
	}

	@Override
	public int getDataLength() {
		return dataLenth;
	}
	@Override
	public byte[] getData() {
		return data;
	}
}
