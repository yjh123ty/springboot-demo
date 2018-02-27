package com.yoga.demo.controller.author;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.yoga.demo.domain.shiro.UserInfo;
import com.yoga.demo.service.UserInfoService;

@Controller
@RequestMapping(value ="auth")
public class AuthorityController {
	
	@Value("${img_host}")
	private String imgHost;
	
	@Autowired
	private UserInfoService userInfoService;
	
	@RequestMapping(value ="index",method = RequestMethod.GET)
    public String index(Model model, Integer uid){
		model.addAttribute("uid", uid);
        return "pages/user/authority";
    }
	
	@RequestMapping(value ="authSuccess",method = RequestMethod.GET)
    public String toSuccess(Model model){
        return "pages/user/authSuccess";
    }
	
	/**
	 * 认证
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "authorize",method = RequestMethod.POST)
	public String authorize(UserInfo userInfo, @RequestParam("file")MultipartFile file, HttpSession session){
		if(file != null && StringUtils.isNotBlank(file.getOriginalFilename())){
			String image = userInfoService.uploadHeadIcon(userInfo.getUid(), file);
			userInfo.setHeadIcon(imgHost.concat(image));
		}
		userInfo.setState(3);
		userInfoService.update(userInfo, null);
		//更新session中的userInfo
		UserInfo newUser = userInfoService.selectByPrimaryKey(userInfo.getUid());
		session.removeAttribute("loginUser");
		session.setAttribute("loginUser", newUser);
		return "pages/user/authSuccess";
	}
	
}
