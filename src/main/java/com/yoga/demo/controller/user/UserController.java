package com.yoga.demo.controller.user;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yoga.demo.common.JsonMsgBean;
import com.yoga.demo.common.Page;
import com.yoga.demo.common.annotation.WebLog;
import com.yoga.demo.domain.dto.UserInfoSearchDTO;
import com.yoga.demo.domain.shiro.SysRole;
import com.yoga.demo.domain.shiro.UserInfo;
import com.yoga.demo.service.AuthorityCheckService;
import com.yoga.demo.service.SysRoleService;
import com.yoga.demo.service.UserInfoService;
import com.yoga.demo.utils.result.JsonMsgBeanUtils;
import com.yoga.demo.utils.shiro.ShiroUtils;

import io.swagger.annotations.Api;

@Controller
@RequestMapping("user")
@Api("用户控制器")
public class UserController {
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
    private AuthorityCheckService authorityCheckService;
	@Autowired
	private SysRoleService sysRoleService;
	
	@RequestMapping(value = "index",method = RequestMethod.GET)
    public String toIndex(Model model){
        return "pages/user/userIndex";	
    }
	
	@RequestMapping(value = "users",method = RequestMethod.GET)
	@ResponseBody
    public Page<UserInfo> listUsers(UserInfoSearchDTO userSearchDTO){
		String fullName = this.getClass().getName().concat(".").concat(Thread.currentThread().getStackTrace()[1].getMethodName());
		Page<UserInfo> page = userInfoService.listUsers(userSearchDTO);
		UserInfo loginUser = ShiroUtils.getLoginUser();
        this.authorityCheckService.desDataByPermission(fullName, loginUser.getRoleIds(), page);
		return page;
    }
	
	@RequestMapping(value = "saveOrUpdate",method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("userInfo:edit")
	@WebLog(desc = "用户编辑")
	public JsonMsgBean saveOrUpdate(UserInfo userInfo, @RequestParam("roleIdArrays[]")List<Integer> roleIdArrays){
		if(userInfo.getUid() != null){
			userInfoService.update(userInfo, roleIdArrays);
			return JsonMsgBeanUtils.defaultSeccess();
		}else{
			userInfoService.save(userInfo, roleIdArrays);
			return JsonMsgBeanUtils.defaultSeccess();
		}
	}
	
	@RequestMapping(value = "edit",method = RequestMethod.GET)
    public ModelAndView toEdit(String uid, Model model){
		ModelAndView mav = new ModelAndView("pages/user/edit");
		List<SysRole> roles = sysRoleService.listAll();
		mav.addObject("roles", roles);
		if(StringUtils.isNotBlank(uid)){
			UserInfo userInfo = userInfoService.selectByPrimaryKey(Integer.parseInt(uid));
			if(userInfo != null){
				mav.addObject("user", userInfo);
			}
		}
        return mav;
    }
	
	/**
	 * 删除用户
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "delete",method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("userInfo:del")
	@WebLog(desc = "用户删除")
	public JsonMsgBean delete(Integer uid){
		userInfoService.delete(uid);
		return JsonMsgBeanUtils.defaultSeccess();
	}
	
}
