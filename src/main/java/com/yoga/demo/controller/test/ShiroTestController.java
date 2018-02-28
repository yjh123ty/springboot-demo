package com.yoga.demo.controller.test;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yoga.demo.domain.shiro.UserInfo;
import com.yoga.demo.service.UserInfoService;

@Controller
@RequestMapping("/test")
public class ShiroTestController {
	
	@Autowired
	private UserInfoService userInfoService;

    /**
     * 用户查询.
     * @return
     */
    @RequestMapping("/userList")
    @RequiresPermissions("userInfo:view")//权限管理;
    public String userInfo(){
        return "pages/shiro/userInfo";
    }

    /**
     * 用户添加;
     * @return
     */
    @RequestMapping("/userAdd")
//    @RequiresRoles(value={"admin", "vip"}, logical= Logical.OR) //表示当前Subject需要角色admin或vip ， Logical.AND 表示都需要。
//    @RequiresRoles("admin") //表示当前Subject需要角色admin或vip ， Logical.AND 表示都需要。
    @RequiresPermissions("userInfo:add")//权限管理;
    public String userInfoAdd(){
        return "pages/shiro/userInfoAdd";
    }

    /**
     * 用户删除;
     * @return
     */
    @RequestMapping("/userDel")
//    @RequiresRoles("admin")
    @RequiresPermissions("userInfo:del")//权限管理;
    public String userDel(){
        return "pages/shiro/userInfoDel";
    }
    
    @RequestMapping("/findByUsername/{username}")
    public String findByUsername(@PathVariable("username")String username){
    	UserInfo findByUsername = userInfoService.findByUsername(username);
    	return findByUsername.getUsername();
    }
    
}