package com.yoga.demo.utils.wechat;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.yoga.demo.utils.http.HttpUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject; 

/**
 * 微信工具类
 * 
 * @author yoga
 */
public class WeChatUtils {
	
	private static Logger log = LoggerFactory.getLogger(WeChatUtils.class);
	
	public static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	
	public static String CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	
	public static String GET_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
	
	/**
	 * weixin
	 */
	public static String WEIXIN_TOKEN = "weixinTest";
	
	public static AccessToken getAccessToken(){
		AccessToken accessToken = null;  
		
		String url = ACCESS_TOKEN_URL.replace("APPID", "wx2599be431fee2f22").replace("APPSECRET", "7089edffe40bcb4341ba045b665d48f4");
		String response = HttpUtils.get(url);
		JSONObject jsonObject = JSONObject.fromObject(response);
		// 如果请求成功  
	    if (null != jsonObject) {  
	        try {  
	            accessToken = new AccessToken();  
	            accessToken.setToken(jsonObject.getString("access_token"));  
	            accessToken.setExpiresIn(jsonObject.getInt("expires_in"));  
	        } catch (JSONException e) {  
	            accessToken = null;  
	            e.printStackTrace();
	        }  
	    }  
	    return accessToken;  
	}
	
	/**
	 * 创建微信菜单
	 */
	public static void createMenu(){
		//从线程中获取token
		String url = CREATE_URL.replace("ACCESS_TOKEN", SingleAccessToken.getInstance().getAccessToken().getToken());
		
		JSONObject menu = new JSONObject();
		//一级菜单
		JSONArray mainMenu = new JSONArray();
		
		//二级菜单 1
		JSONObject subMenu1 = new JSONObject();
		subMenu1.put("type", "view");
		subMenu1.put("name", "首页");
		subMenu1.put("url", "http://yjh.tunnel.qydev.com");
		//二级菜单 2
		JSONObject subMenu2 = new JSONObject();
		subMenu2.put("type", "view");
		subMenu2.put("name", "注册账号");
		subMenu2.put("url", "http://yjh.tunnel.qydev.com/register/index");
		
		//二级菜单 3
		JSONObject subMenu3 = new JSONObject();
		subMenu3.put("name", "功能菜单");
		JSONArray subArray = new JSONArray();
		
		JSONObject subButton1 = new JSONObject();
		subButton1.put("type", "click");
		subButton1.put("name", "赞一下");
		subButton1.put("key", "V1001_GOOD");
		JSONObject subButton2 = new JSONObject();
		subButton2.put("type", "view");
		subButton2.put("name", "我的城市");
		subButton2.put("url", "http://yjh.tunnel.qydev.com/map/index");
//		JSONObject subButton3 = new JSONObject();
//		subButton2.put("type", "view");
//		subButton2.put("name", "我的商城");
//		subButton2.put("url", "http://yjh.tunnel.qydev.com/product/index");
		
		subArray.add(0, subButton1);
		subArray.add(1, subButton2);
//		subArray.add(2, subButton3);
		subMenu3.put("sub_button", subArray);
		
		mainMenu.add(0, subMenu1);
		mainMenu.add(1, subMenu2);
		mainMenu.add(2, subMenu3);
		menu.put("button", mainMenu);
		//发送请求
		HttpUtils.post(url, menu);
		
	}
	
	public static String getMenu(){
		String url = GET_MENU_URL.replace("ACCESS_TOKEN", SingleAccessToken.getInstance().getAccessToken().getToken());
		String response = HttpUtils.get(url);
		JSONObject fromObject = JSONObject.fromObject(response);
		return fromObject.toString();
	}
	
	/**
	 * sha1加密算法
	 * 
	 * @param key需要加密的字符串
	 * @return 加密后的结果
	 */
	public static String sha1(String key) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			md.update(key.getBytes());
			String pwd = new BigInteger(1, md.digest()).toString(16);
			return pwd;
		} catch (Exception e) {
			e.printStackTrace();
			return key;
		}
	}
	
	/**
	 * 解析request中的xml 并将数据存储到一个Map中返回
	 * 
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			InputStream inputStream = request.getInputStream();
			SAXReader reader = new SAXReader();
			Document document = reader.read(inputStream);
			Element root = document.getRootElement();
			List<Element> elementList = root.elements();
			for (Element e : elementList)
				// 遍历xml将数据写入map
				map.put(e.getName(), e.getText());
			inputStream.close();
			inputStream = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 存储数据的Map转换为对应的Message对象
	 * 
	 * @param map
	 *            存储数据的map
	 * @return 返回对应Message对象
	 */
	public static Message mapToMessage(Map<String, String> map) {
		if (map == null)
			return null;
		String msgType = map.get("MsgType");
		Message message = new Message();
		message.setToUserName(map.get("ToUserName"));
		message.setFromUserName(map.get("FromUserName"));
		message.setCreateTime(new Date(Long.parseLong(map.get("CreateTime"))));
		message.setMsgType(msgType);
		message.setMsgId(map.get("MsgId"));
		if (msgType.equals(Message.TEXT)) {
			message.setContent(map.get("Content"));
		} else if (msgType.equals(Message.IMAGE)) {
			message.setPicUrl(map.get("PicUrl"));
		} else if (msgType.equals(Message.LINK)) {
			message.setTitle(map.get("Title"));
			message.setDescription(map.get("Description"));
			message.setUrl(map.get("Url"));
		} else if (msgType.equals(Message.LOCATION)) {
			message.setLocationX(map.get("Location_X"));
			message.setLocationY(map.get("Location_Y"));
			message.setScale(map.get("Scale"));
			message.setLabel(map.get("Label"));
		} else if (msgType.equals(Message.EVENT)) {
			message.setEvent(map.get("Event"));
			message.setEventKey(map.get("EventKey"));
		}
		return message;
	}
	
	/**
	 * 根据token计算signature验证是否为weixin服务端发送的消息
	 */
	@SuppressWarnings("unused")
	public static boolean checkWeixinReques(HttpServletRequest request){
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		String queryString = request.getQueryString();
		if (signature != null && timestamp != null && nonce != null ) {
			String[] strSet = new String[] { WeChatUtils.WEIXIN_TOKEN, timestamp, nonce };
			java.util.Arrays.sort(strSet);
			String key = "";
			for (String string : strSet) {
				key = key + string;
			}
			String pwd = WeChatUtils.sha1(key);
			if(pwd.equals(signature)){
				log.debug("签名验证通过！");
				return true;
			}else
				return false;
		}else {
			return false;
		}
	}
	
	public static void main(String[] args) {
		createMenu();
		System.err.println(getMenu());
	}
}
