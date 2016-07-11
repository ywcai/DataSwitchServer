package ywcai.ls.desk.protocol;

public class ProtocolResString implements MesResInf {
	private byte tag;
	private int usernameLenth;
	private int dataLenth;
	private String username;
	private String data;
	
	@Override
	public byte getTag() {
		return tag;
	}
	@Override
	public void setTag(byte tag) {
		this.tag = tag;
	}

	
	@Override
    public int getUsernameLenth() {
		return usernameLenth;
	}
	@Override
	public void setUsernameLenth(int usernameLenth) {
		this.usernameLenth = usernameLenth;
	}
	@Override
	public int getDataLenth() {
		return dataLenth;
	}
	@Override
	public void setDataLenth(int dataLenth) {
		this.dataLenth = dataLenth;
	}
	@Override
	public String getUsername() {
		return username;
	}
	@Override
	public void setUsername(String username) {
		this.username = username;
	}
	@Override
	public String getData() {
		return data;
	}
	@Override
	public void setData(Object data) {
		this.data =(String)data;
	}
}
