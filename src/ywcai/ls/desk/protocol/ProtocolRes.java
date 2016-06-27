package ywcai.ls.desk.protocol;

public class ProtocolRes {
	private byte tag;
	private int usernameLenth;
	private int dataLenth;
	private String username;
	private String data;
	public byte getTag() {
		return tag;
	}
	public void setTag(byte tag) {
		this.tag = tag;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
    public int getUsernameLenth() {
		return usernameLenth;
	}
	public void setUsernameLenth(int usernameLenth) {
		this.usernameLenth = usernameLenth;
	}
	public int getDataLenth() {
		return dataLenth;
	}
	public void setDataLenth(int dataLenth) {
		this.dataLenth = dataLenth;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
