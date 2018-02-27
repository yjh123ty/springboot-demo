package com.yoga.demo.controller.wechat;


import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yoga.demo.utils.wechat.Message;
import com.yoga.demo.utils.wechat.MessageUtils;
import com.yoga.demo.utils.wechat.WeChatUtils;
import com.yoga.demo.utils.wechat.reply.TextMessage;

@Controller
public class WeChatController {
	
	//接收微信公众号接收的消息，处理后再做相应的回复
	@RequestMapping(value="/index",method=RequestMethod.POST)
	@ResponseBody
	public String replyMessage(HttpServletRequest request){
		//仅处理微信服务端发的请求
		if (WeChatUtils.checkWeixinReques(request)) {
			Map<String, String> requestMap = WeChatUtils.parseXml(request);
			Message message = WeChatUtils.mapToMessage(requestMap);
			//weixinService.addMessage(message);//保存接受消息到数据库
			String type = message.getMsgType();
			if (type.equals(Message.TEXT)) {
				//自动消息回复
				TextMessage textMessage = new TextMessage();  
                textMessage.setToUserName(message.getFromUserName());  
                textMessage.setFromUserName(message.getToUserName());  
                textMessage.setCreateTime(new Date().getTime());  
                textMessage.setMsgType("text");  
                textMessage.setContent("欢迎来到我的微信测试号，您可以在菜单中选择您想了解的东东~~");
                return MessageUtils.textMessageToXml(textMessage); 
			}
			else if (type.equals(Message.EVENT)) {
				if(StringUtils.isNotBlank(message.getEventKey()) && message.getEventKey().equals("V1001_GOOD")){//若为 赞一下
					//处理点击事件
					TextMessage textMessage = new TextMessage();  
	                textMessage.setToUserName(message.getFromUserName());  
	                textMessage.setFromUserName(message.getToUserName());  
	                textMessage.setCreateTime(new Date().getTime());  
	                textMessage.setMsgType("text");  
	                textMessage.setContent("谢谢您的赞，已经收到！");
	                return MessageUtils.textMessageToXml(textMessage);  
				}else if(message.getEvent().equals("subscribe")){
					//处理订阅事件
					TextMessage textMessage = new TextMessage();  
	                textMessage.setToUserName(message.getFromUserName());  
	                textMessage.setFromUserName(message.getToUserName());  
	                textMessage.setCreateTime(new Date().getTime());  
	                textMessage.setMsgType("text");  
	                textMessage.setContent("感谢关注【丶余子酱】！\n 更多精彩，玩转微信！");
	                return MessageUtils.textMessageToXml(textMessage);
				}
			}
		}
		return "error";
	}
	
	
	//微信公众平台验证url是否有效使用的接口
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public void initWeixinURL(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String echostr = request.getParameter("echostr");
		if (WeChatUtils.checkWeixinReques(request)) {
			System.out.println("接入成功");
			response.getWriter().write(echostr);
		}else{
			//严格个操作应该抛异常
			System.out.println("接入失败");
			response.getWriter().write(echostr);
		}
	}
	
}
