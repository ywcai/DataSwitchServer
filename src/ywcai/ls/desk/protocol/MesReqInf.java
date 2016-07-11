package ywcai.ls.desk.protocol;

public interface MesReqInf {
	public byte getTag();
	public int getNameLenth();
	public int getDataLenth();
	public String getUserName();
	public Object getData();
}
