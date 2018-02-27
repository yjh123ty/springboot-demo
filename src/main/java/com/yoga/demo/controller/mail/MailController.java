package com.yoga.demo.controller.mail;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yoga.demo.common.CodeConstants;
import com.yoga.demo.common.JsonMsgBean;
import com.yoga.demo.domain.shiro.UserInfo;
import com.yoga.demo.service.UserInfoService;
import com.yoga.demo.utils.result.JsonMsgBeanUtils;

@Controller
@RequestMapping("mail")
public class MailController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
	@Autowired  
    JavaMailSender mailSender;  
	
	@Autowired
	private UserInfoService userinfoService;
	
	@RequestMapping("sendemail")  
	@ResponseBody
    public JsonMsgBean sendEmail()  
    {  
        try  
        {  
        	//true表示需要创建一个multipart message 
            final MimeMessage mimeMessage = this.mailSender.createMimeMessage();  
            final MimeMessage mimeMessage2 = this.mailSender.createMimeMessage();  
            final MimeMessage mimeMessage3 = this.mailSender.createMimeMessage();  
            final MimeMessage mimeMessage4 = this.mailSender.createMimeMessage();  
            final MimeMessage mimeMessage5 = this.mailSender.createMimeMessage();  
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);  
            final MimeMessageHelper message2 = new MimeMessageHelper(mimeMessage2, true);  
            final MimeMessageHelper message3 = new MimeMessageHelper(mimeMessage3, true);  
            final MimeMessageHelper message4 = new MimeMessageHelper(mimeMessage4, true);  
            final MimeMessageHelper message5 = new MimeMessageHelper(mimeMessage5, true);  
            message.setFrom("zp18030466966@sina.com");  
            message.setTo("zp18030466966@sina.com");  
            message.setSubject("被列入失信人通知单");  
            message.setText("【成都友木科技有限公司】周鹏:\n您已被列入失信人名单！请履行未执行标的，详情请查看附件");
            
            message2.setFrom("zp18030466966@sina.com");  
            message2.setTo("zhou@imimz.com");  
            message2.setSubject("被列入失信人通知单");  
            message2.setText("【成都友木科技有限公司】周鹏:\n您已被列入失信人名单！请履行未执行标的，详情请查看附件");
            
            message3.setFrom("zp18030466966@sina.com");  
            message3.setTo("zhoubotong@youmu.tech");  
            message3.setSubject("被列入失信人通知单");  
            message3.setText("【成都友木科技有限公司】周鹏:\n您已被列入失信人名单！请履行未执行标的，详情请查看附件");
            
            message4.setFrom("zp18030466966@sina.com");  
            message4.setTo("1635368272@qq.com");  
            message4.setSubject("被列入失信人通知单");  
            message4.setText("【成都友木科技有限公司】周鹏:\n您已被列入失信人名单！请履行未执行标的，详情请查看附件");
            
            message5.setFrom("zp18030466966@sina.com");  
            message5.setTo("tim@youmu.tech");  
            message5.setSubject("被列入失信人通知单");  
            message5.setText("【成都友木科技有限公司】周鹏:\n您已被列入失信人名单！请履行未执行标的，详情请查看附件");
            
            
            FileSystemResource file = new FileSystemResource(new File("src/main/resources/static/assets/img/shixinren.png"));  
            message.addAttachment("失信人通知.png", file); 
            message2.addAttachment("失信人通知.png", file); 
            message3.addAttachment("失信人通知.png", file); 
            message4.addAttachment("失信人通知.png", file); 
            message5.addAttachment("失信人通知.png", file); 
            
            for (int i = 0; i < 5; i++) {
            	this.mailSender.send(mimeMessage);  
            	this.mailSender.send(mimeMessage2);  
            	this.mailSender.send(mimeMessage3);  
            	this.mailSender.send(mimeMessage4);  
            	this.mailSender.send(mimeMessage5);
            	logger.info("发送邮件成功！" + "第" + i + "次");
			}
        	
            logger.info("发送邮件成功！");
            JsonMsgBean resultMsg = JsonMsgBeanUtils.defaultSeccess();  
            return resultMsg;  
        }  
        catch(Exception ex)  
        {  
        	ex.printStackTrace();
        	JsonMsgBean resultMsg = JsonMsgBeanUtils.fail("邮件发送失败！", new Byte("9"));
            return resultMsg;  
        }  
    }  
	
	@RequestMapping(value = "sendmail", method = RequestMethod.POST)  
	@ResponseBody
    public JsonMsgBean sendEmail(Integer uid)  
    {  
		JsonMsgBean resultMsg;
        try  
        {  
            final MimeMessage mimeMessage = this.mailSender.createMimeMessage();  
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);  
            UserInfo user = userinfoService.selectByPrimaryKey(uid);
            if(StringUtils.isNotBlank(user.getEmail())){
            	message.setTo(user.getEmail());  
            	message.setFrom("zp18030466966@sina.com");  
                message.setSubject("测试邮件title");  
                message.setText("测试邮件");
                this.mailSender.send(mimeMessage);  
                resultMsg = JsonMsgBeanUtils.success("发送成功！", null);
                logger.info("发送邮件成功！");
            }else{
            	resultMsg = JsonMsgBeanUtils.fail("邮件发送失败！", CodeConstants.ERROR_CODE_FAIL_SEND_EMAIL);
            	logger.info("该用户未设置邮箱！");
            }
            return resultMsg;  
        }  
        catch(Exception ex)  
        {  
        	ex.printStackTrace();
        	resultMsg = JsonMsgBeanUtils.fail("邮件发送失败！", CodeConstants.ERROR_CODE_FAIL_SEND_EMAIL);
            return resultMsg;  
        }  
    }  
}
