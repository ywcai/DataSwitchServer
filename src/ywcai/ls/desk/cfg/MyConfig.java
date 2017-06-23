package ywcai.ls.desk.cfg;

public class MyConfig {

	//包头属性  0  FALG 1 TOKEN 2 DATA TYPE 3 REQ TPYE 4 TOKEN 20 DATA LENTH 24-27预留
	public static final int INT_PACKAGE_HEAD_LEN=28;
	public static final int PROTOCOL_HEAD_FLAG=0x7E;
	public static final int PROTOCOL_HEAD_HAS_TOKEN=0x01;
	public static final int PROTOCOL_HEAD_NOT_TOKEN=0x00;

	public static final int PROTOCOL_HEAD_TYPE_JSON=0x01;
	public static final int PROTOCOL_HEAD_TYPE_IMG=0x02;

	public static final int REQ_TYPE_USER_LOGIN_IN=0x01;
	public static final int REQ_TYPE_USER_LOGIN_OUT=0x02;
	public static final int REQ_TYPE_DESK_LINK_OPEN=0x03;
	public static final int REQ_TYPE_DESK_SHOWDOWN=0x04;
	public static final int REQ_TYPE_CONTROL_CMD=0x05;
	public static final int REQ_TYPE_DESKTOP_SWITCH=0x06;
	public static final int REQ_TYPE_CLIENT_LIST_UPDATE=0x07;
	//public static final int REQ_TYPE_CLIENT_STATUS_TURNON=0x08;
	//public static final int REQ_TYPE_CLIENT_STATUS_TURNOFF=0x09;

	public static final int PROTOCOL_HEAD_RESERVE=0x7F;//预留位 值

	//协议起始位置，大小
	public static final int PROTOCOL_HEAD_POS_FLAG = 0;
	public static final int PROTOCOL_HEAD_POS_TOKENTYPE = 1;
	public static final int PROTOCOL_HEAD_POS_DATATYPE = 2;
	public static final int PROTOCOL_HEAD_POS_REQTYPE = 3;
	public static final int PROTOCOL_HEAD_POS_TOKEN = 4;
	public static final int PROTOCOL_HEAD_SIZE_TOKEN = 16;
	public static final int PROTOCOL_HEAD_POS_DATALEN = 20;
	public static final int PROTOCOL_HEAD_POS_RESERVE = 24;//预留位
	public static final int PROTOCOL_HEAD_SIZE_RESERVE = 4;//预留位
	


	//socket配置
	public static final int INT_SERVER_PORT=7772;
	public static final int INT_READ_BUFFERSIZE=4096;
	
	
    public static final String STR_LOGIN_RESULT_OK = "login_ok";
    public static final String STR_OUT_RESULT_OK = "login_out_ok";
    public static final String STR_OPEN_DESK_MASTER = "master";
    public static final String STR_OPEN_DESK_SLAVE = "slave";
    public static final String STR_OPEN_DESK_FAIL = "has_a_link"; 
    public static final String STR_OPEN_DESK_FAIL1 = "not_link_self"; 
    public static final String STR_SHUTDOWN_DESK_FAIL = "has_no_link";
    public static final String STR_SHUTDOWN_DESK_OK = "disconnect_ok";
    
    
    public static final  int INT_SOCKET_PORT=7772;
    public static final  int INT_SOCKET_HEAD_LEN=21;
    public static final  int INT_PROTOCOL_HEAD_FLAG=0x01;
    public static final  int INT_SOCKET_TOKEN_POS=1;
    public static final  int INT_SOCKET_LENTH_POS=17;
    public static final  byte[] BYTES_PROTOCOL_HEAD_TOKEN={0x3a,0x3b,0x3c,0x3d,0x4a,0x4b,0x4c,0x4d,0x5a,0x5b,0x5c,0x5d,0x6a,0x6b,0x6c,0x6d};
    public static final  byte byte_data_flag_json=  (byte) 0xcf;
    public static final  byte byte_data_flag_byte=  (byte) 0xdf;
    
    
    public static final String json_key_type="type";
    public static final String json_key_content="content";

    
}
