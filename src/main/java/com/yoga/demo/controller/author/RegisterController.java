package com.yoga.demo.controller.author;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yoga.demo.common.CodeConstants;
import com.yoga.demo.common.JsonMsgBean;
import com.yoga.demo.domain.dto.UserLoginDTO;
import com.yoga.demo.domain.shiro.UserInfo;
import com.yoga.demo.service.UserInfoService;
import com.yoga.demo.utils.result.JsonMsgBeanUtils;

@Controller
@RequestMapping("register")
public class RegisterController {
	
	@Autowired
	private UserInfoService userService;	
	
	@RequestMapping(value="index", method=RequestMethod.GET)
	public ModelAndView toRegister() {
		ModelAndView mav = new ModelAndView("register");
		return mav;
	}
	
	@RequestMapping(value="doRegister", method=RequestMethod.POST)
	@ResponseBody
	public JsonMsgBean doRegister(UserLoginDTO userLogin) {
		UserInfo user = userService.findByUsername(userLogin.getLoginName());
		if(user != null){
			//重复的用户名
			return JsonMsgBeanUtils.fail("该用户名已被注册", CodeConstants.ERROR_CODE_ILLEGAL_REGISTER);
		}
		UserInfo userInfo = new UserInfo(userLogin.getLoginName(), userLogin.getPassword(), UserInfo.UserState.UNCKECK, Boolean.FALSE);
		userService.save(userInfo, null);
		userService.saveUserRole(userInfo.getUid(), 2);
		return JsonMsgBeanUtils.defaultSeccess();
	}
	

}
