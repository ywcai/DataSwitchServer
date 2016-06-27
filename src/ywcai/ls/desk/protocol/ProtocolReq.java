package ywcai.ls.desk.protocol;

public class ProtocolReq implements MesReqInf{

	private String username,data;
	private byte tag;
	public ProtocolReq(byte ptag,String pUsername,String pData) {
		// TODO Auto-generated constructor stub
		this.tag=ptag;
		this.username=pUsername;
		this.data=pData;
	}
	@Override
	public byte getTag() {
		// TODO Auto-generated method stub
		return tag;
	}

	@Override
	public String getData() {
		// TODO Auto-generated method stub
		return data;
	}
	@Override
	public int getNameLenth() {
		// TODO Auto-generated method stub
		return username==null?0:username.getBytes().length;
	}
	@Override
	public int getDataLenth() {
		// TODO Auto-generated method stub
		return data==null?0:data.getBytes().length;
	}
	@Override
	public String getUserName() {
		// TODO Auto-generated method stub
		return username;
	}
}
