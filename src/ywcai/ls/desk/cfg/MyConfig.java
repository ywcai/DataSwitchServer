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
}
