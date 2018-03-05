package com.yoga.demo.controller.author;

import javax.servlet.http.HttpSession;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yoga.demo.common.Constants;
import com.yoga.demo.common.JsonMsgBean;
import com.yoga.demo.common.annotation.WebLog;
import com.yoga.demo.domain.dto.UserLoginDTO;
import com.yoga.demo.domain.shiro.UserInfo;
import com.yoga.demo.service.SysMenuService;
import com.yoga.demo.service.UserInfoService;
import com.yoga.demo.utils.result.JsonMsgBeanUtils;
import com.yoga.demo.utils.shiro.ShiroUtils;


@Controller
public class LoginController {
	@Autowired
	private SysMenuService sysMenuService;
	@Autowired
	private UserInfoService userService;
	
	@RequestMapping(value = "login",method = RequestMethod.GET)
	public ModelAndView toLogin(Model model){
	       return new ModelAndView("login");
	}
	
	@RequestMapping(value = "checkLogin",method = RequestMethod.POST)
	@ResponseBody
	@WebLog(desc="用户登录")
	public JsonMsgBean checkLogin(@RequestBody UserLoginDTO userLogin, HttpSession session){
		//TODO：为了便于开发，暂时关闭验证码校验
//		if (!StringUtils.equalsIgnoreCase(userLogin.getCode(), ShiroUtils.getCaptcha())) {
//			throw new BusinessException("验证码错误!");
//		}
		
	    try {
	    	Subject subject = ShiroUtils.getSubject();
		    UsernamePasswordToken token = new UsernamePasswordToken(userLogin.getLoginName(), userLogin.getPassword());
			subject.login(token);
			//获取用户的菜单
			session.setAttribute("menus", sysMenuService.getUserMenusByLoginUser(userLogin.getLoginName()));
			//获取登录用户信息
			UserInfo findByUsername = userService.findUserInfoByUsername(userLogin.getLoginName());
			//在shiro会话中设置的userInfo.暂时没用到
			ShiroUtils.setSessionAttribute(Constants.LOGIN_USER.concat("-").concat(String.valueOf(findByUsername.getUid())), findByUsername);
			//前端页面获取的userInfo
			session.setAttribute("loginUser", findByUsername);
		} catch (UnknownAccountException e) {
			e.printStackTrace();
			throw new UnknownAccountException("账号不存在！");
		}catch (IncorrectCredentialsException e) {
			e.printStackTrace();
			throw new IncorrectCredentialsException(e.getMessage());
		}catch (AuthenticationException e) {
			e.printStackTrace();
			throw new AuthenticationException(e.getMessage());
		}
		
	    return JsonMsgBeanUtils.defaultSeccess();
	}
	
	@RequestMapping(value = "logout",method = RequestMethod.GET)
	public String logout(HttpSession session){
		session.removeAttribute("loginUserName");
		session.removeAttribute("menus");
		ShiroUtils.logout();
		return "redirect:login.html";
	}
	
	@RequestMapping("/403")
    public String unauthorizedRole(){
        System.out.println("------没有权限-------");
        return "/pages/shiro/403";
    }

}
