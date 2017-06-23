package ywcai.ls.desk.core;

import org.json.JSONException;
import org.json.JSONObject;

import ywcai.ls.desk.cfg.MyConfig;


public class MakeJson {

	public JSONObject makeJson(int type,String content)
	{
		JSONObject json=new JSONObject();
		try {
			json.put(MyConfig.json_key_type, type);
			json.put(MyConfig.json_key_content, content);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
		
	}
}
