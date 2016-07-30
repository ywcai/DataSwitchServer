package ywcai.ls.desk.protocol;

public interface MesReqInf {
	public byte getReqType();
	public byte getDataType();
	public int getDataLenth();
	public String getToken();
	public Object getData();
}
