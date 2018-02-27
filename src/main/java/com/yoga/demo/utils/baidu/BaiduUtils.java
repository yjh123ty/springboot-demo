package com.yoga.demo.utils.baidu;

import com.yoga.demo.domain.address.vo.AddressInfo;
import com.yoga.demo.utils.http.HttpUtils;

import net.sf.json.JSONObject;

public class BaiduUtils {
	
	public static String BAIDU_LOCATION_IP_URL = "http://api.map.baidu.com/location/ip?";
	public static String BAIDU_AK = "rzINumfC12VuDm3d2r4CBTDNACLWYVZz";
	
	public static AddressInfo getLocationByIp(String ip){
		StringBuilder sb = new StringBuilder(BAIDU_LOCATION_IP_URL);
		sb.append("ip=").append(ip)
		.append("&ak=").append(BAIDU_AK)
		.append("&coor=").append("bd09ll");
		//发送请求
		String response = HttpUtils.get(sb.toString());
		JSONObject jsonObject = JSONObject.fromObject(response);
		System.err.println(jsonObject);
		Object content = jsonObject.get("content");
		JSONObject contentJson = JSONObject.fromObject(content);
		
		AddressInfo info = null;
		if((Integer)jsonObject.get("status") == 0){
			//封装对象
			info = new AddressInfo();
			info.setAddress(contentJson.getString("address"));
			Object pointInfo = contentJson.get("point");
			JSONObject point = JSONObject.fromObject(pointInfo);
			info.setPointX(point.getString("x"));
			info.setPointY(point.getString("y"));
		}
		return info;
	}
	
	public static void main(String[] args) {
		getLocationByIp("115.239.210.237");
    }
}
