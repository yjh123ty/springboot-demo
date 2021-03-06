package com.yoga.demo.controller.author;

import javax.servlet.http.HttpSession;

import io.swagger.annotations.Api;
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
@Api(value = "登录接口", description = "")
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

			if (findByUsername == null)
				return JsonMsgBeanUtils.authFail("登录失败!请检查用户名或密码");
			//在shiro会话中设置的userInfo.暂时没用到
			ShiroUtils.setSessionAttribute(Constants.LOGIN_USER.concat("-").concat(String.valueOf(findByUsername.getUid())), findByUsername);
			//前端页面获取的userInfo
			session.setAttribute("loginUser", findByUsername);
		} catch (UnknownAccountException e) {
			e.printStackTrace();
			return JsonMsgBeanUtils.authFail("账号不存在！");
		}catch (IncorrectCredentialsException e) {
			e.printStackTrace();
			return JsonMsgBeanUtils.authFail(e.getMessage());
		}catch (AuthenticationException e) {
			e.printStackTrace();
			return JsonMsgBeanUtils.authFail(e.getMessage());
		}
		
	    return JsonMsgBeanUtils.success("登录成功!", null);
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
