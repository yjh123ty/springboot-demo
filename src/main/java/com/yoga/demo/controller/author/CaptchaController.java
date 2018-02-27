package com.yoga.demo.controller.author;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.bingoohuang.patchca.color.SingleColorFactory;
import com.github.bingoohuang.patchca.custom.ConfigurableCaptchaService;
import com.github.bingoohuang.patchca.filter.predefined.CurvesRippleFilterFactory;
import com.github.bingoohuang.patchca.service.Captcha;
import com.github.bingoohuang.patchca.utils.encoder.EncoderHelper;
import com.github.bingoohuang.patchca.word.RandomWordFactory;
import com.yoga.demo.common.Constants;
import com.yoga.demo.utils.shiro.ShiroUtils;

/**
 * 图形验证码
 * @author yoga
 *
 */
@Controller
@RequestMapping("captcha")
public class CaptchaController {
	private static ConfigurableCaptchaService captchaService = new ConfigurableCaptchaService();
	
	static {
		captchaService.setColorFactory(new SingleColorFactory(new Color(25, 60, 170)));
		captchaService.setFilterFactory(new CurvesRippleFilterFactory());
		RandomWordFactory wordFactory = new RandomWordFactory();
		wordFactory.setCharacters("23456789abcdefghigkmnpqrstuvwxyzABCDEFGHIGKLMNPQRSTUVWXYZ");
		wordFactory.setMaxLength(4);
		wordFactory.setMinLength(4);
		captchaService.setWordFactory(wordFactory);
	}

	/**
	 * 生成二维码放在shiro的session下
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="img", method=RequestMethod.GET)
	@ResponseBody
	public void captcha(HttpServletResponse response) throws IOException {
		 response.setContentType("image/png");
		 response.setHeader("Cache-Control", "no-cache, no-store");
		 response.setHeader("Pragma", "no-cache");
		 long current = System.currentTimeMillis();
		 response.setDateHeader("Last-Modified", current);
		 response.setDateHeader("Date", current);
		 response.setDateHeader("Expires", current);
		 String token = EncoderHelper.getChallangeAndWriteImage(captchaService, "png", response.getOutputStream());
		 ShiroUtils.setSessionAttribute(Constants.CAPTCHA_TOKEN, token);
	}
	
	@RequestMapping(value="string", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, String> captchaString(HttpSession session) throws IOException {
		 Captcha captcha = captchaService.getCaptcha();
		 String token = captcha.getChallenge();
		 String imgBase64 = base64BuffedImage(captcha.getImage());
		 session.setAttribute(Constants.CAPTCHA_TOKEN, token);
		 
		 Map<String, String> resultMap = new HashMap<String, String>();
		 resultMap.put("imgBase64", imgBase64);
		 return resultMap;
	}

	private String base64BuffedImage(BufferedImage bufferedImage) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "png", baos);  
        byte[] binaryData = baos.toByteArray();
		return Base64.encodeBase64String(binaryData);
	}
}
