package ywcai.ls.desk.cfg;

import java.io.UnsupportedEncodingException;

import org.apache.mina.core.session.IoSession;
import org.json.JSONObject;

import ywcai.ls.desk.core.MakeJson;
import ywcai.ls.desk.newpro.ProtocolRequest;

public class MyUtil {

	public static void sendJson(IoSession session,int type,String content)
	{
		MakeJson mj=new MakeJson();
		JSONObject json=mj.makeJson(type, content);
		String str=json.toString();
		System.out.println("to session: " + session.toString() +" Send json: "+str);
		byte[] temp=null;
		try {
			temp = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] payload=new byte[temp.length+1];
		payload[0]=(byte) MyConfig.byte_data_flag_json;
		System.arraycopy(temp, 0, payload, 1, temp.length);
		ProtocolRequest pq=new ProtocolRequest(payload);
		session.write(pq);
	}
}
