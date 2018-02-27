package com.yoga.demo.utils.shorturl;

import com.alibaba.fastjson.JSONArray;
import com.yoga.demo.utils.http.HttpUtils;

import net.sf.json.JSONObject;


public class ShortUrlUtils {
	
	public static String createUrl = "http://api.t.sina.com.cn/short_url/shorten.json?source=APPKEY&url_long=URL";
	
	//source
	public static final String APP_KEY = "1194890400";
	//请求的长路径,测试用
	public static final String LONG_URL = "http://app.haolaoban168.com/statistic/download/index.htm?source=821150650450532";
			
	public static String getShortUrl(String url){
		String create_url = createUrl.replace("APPKEY", APP_KEY);
		create_url = create_url.replace("URL", url);
		String response = HttpUtils.get(create_url);
		JSONArray array = JSONArray.parseArray(response);
		Object object2 = array.get(0);
		JSONObject fromObject = JSONObject.fromObject(object2);
		Object object = fromObject.get("url_short");
		return object == null ? null : object.toString();
	}
	
    public static void main(String[] args) {
    	System.err.println(getShortUrl(LONG_URL));
    	
    }
}