package com.yoga.demo.controller.user;

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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
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
	 * @param uid
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
	
	/**
     * 导出数据
     * @param response
     */
    @RequestMapping(value="users/download")
    @ResponseBody
    public void downloadUsers(HttpServletResponse response, HttpServletRequest request, UserInfoSearchDTO searchDTO) {
        OutputStream out = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String title = "用户数据_".concat(sdf.format(new Date())).concat(".xlsx");
            response.reset();
            out = response.getOutputStream();
            if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {  
                response.setHeader("Content-Disposition", "attachment; filename=".concat(URLEncoder.encode(title, "UTF-8")));
            } else {  
                /*new String(title.getBytes("gb2312"), "ISO8859-1")*/
                response.setHeader("Content-Disposition", "attachment; filename=".concat(new String(title.getBytes("UTF-8"), "ISO8859-1")));
            }
            Map<String, String> headerMap = new LinkedHashMap<String, String>();
            headerMap.put("username", "账号");
            headerMap.put("name", "用户名");
            headerMap.put("password", "密码");
            //导出数据
            userInfoService.exportUsers(null, headerMap, searchDTO, out);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @RequestMapping(value="test")
    @ResponseBody
    public String testThreadResponse(String syncflag){
    	userInfoService.updateByThread(syncflag);
    	return "调用接口完成 　时间： " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
	
}
