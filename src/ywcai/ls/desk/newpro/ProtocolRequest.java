package ywcai.ls.desk.newpro;

import ywcai.ls.desk.cfg.MyConfig;




public class ProtocolRequest implements MesInf {
	private int dataLenth;
	private byte[] data,token;
	private byte reqFlag;

	public ProtocolRequest(byte[] pData) {
		// TODO Auto-generated constructor stub
		this.reqFlag= MyConfig.INT_PROTOCOL_HEAD_FLAG;
		this.token=MyConfig.BYTES_PROTOCOL_HEAD_TOKEN;
		this.dataLenth=pData.length;
		this.data=pData;

	}
	@Override
	public int getDataLength() {
		// TODO Auto-generated method stub
		return dataLenth;
	}

	@Override
	public byte getReqFlag() {
		return reqFlag;
	}

	@Override
	public byte[] getToken() {
		return token;
	}

	@Override
	public byte[] getData() {
		// TODO Auto-generated method stub
		return data;
	}
}
