package ywcai.ls.desk.protocol;

public interface MesResInf {
	public byte getReqType();
	public byte getDataType();
	public int getDataLenth();
	public String getToken();
	public Object getData();
	
//	public void setReqType(byte tag);
//	public void setDataType(byte tag);
//	public void setDataLenth(int dataLenth);
//	public void setToken(String token);
//	public void setData(Object data);

}
