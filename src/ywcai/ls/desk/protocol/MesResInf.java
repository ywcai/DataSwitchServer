package ywcai.ls.desk.protocol;

public interface MesResInf {
	public byte getTag();
	public void setTag(byte tag);
    public int getUsernameLenth();
	public void setUsernameLenth(int usernameLenth);
	public int getDataLenth() ;
	public void setDataLenth(int dataLenth) ;
	public String getUsername();
	public void setUsername(String username);
	public Object getData();
	public void setData(Object data);
}
