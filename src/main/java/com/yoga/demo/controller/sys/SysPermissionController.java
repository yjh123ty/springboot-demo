package com.yoga.demo.controller.sys;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yoga.demo.common.JsonMsgBean;
import com.yoga.demo.common.Page;
import com.yoga.demo.domain.shiro.SysPermission;
import com.yoga.demo.domain.shiro.dto.SysPermSearchDTO;
import com.yoga.demo.service.SysPermissionService;
import com.yoga.demo.utils.result.JsonMsgBeanUtils;
import com.yoga.demo.utils.shiro.ShiroUtils;

@Controller
@RequestMapping("sysPerm")
public class SysPermissionController {
	@Autowired
	private SysPermissionService permissionService;
	
	@RequestMapping(value = "index",method = RequestMethod.GET)
    public String toIndex(Model model){
        return "pages/sys/perm/index";	
    }
	
	
	@RequestMapping(value = "list",method = RequestMethod.GET)
	@ResponseBody
    public Page<SysPermission> list(SysPermSearchDTO permSearchDTO){
		Page<SysPermission> page = permissionService.list(permSearchDTO);
		return page;
    }
	
	@RequestMapping(value = "edit",method = RequestMethod.GET)
    public ModelAndView toEdit(Integer id, Model model){
		ModelAndView mav = new ModelAndView("pages/sys/perm/edit");
		if(null != id){
			SysPermission perm = permissionService.selectByPrimaryKey(id);
			if(perm != null){
				mav.addObject("perm", perm);
			}
		}
        return mav;
    }
	
	@RequestMapping(value = "saveOrUpdate",method = RequestMethod.POST)
	@ResponseBody
	public JsonMsgBean saveOrUpdate(SysPermission perm){
 		Integer userId = ShiroUtils.getUserId();
		perm.setLastUpdateUserId(userId);
		if(perm.getId() != null){
			permissionService.updateByPrimaryKeySelective(perm);
		}else{
			permissionService.insert(perm);
		}
		return JsonMsgBeanUtils.defaultSeccess();
	}
	
	/**
	 * 删除
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "delete",method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("perm:del")
	public JsonMsgBean delete(Integer id){
		permissionService.deleteByPrimaryKey(id, ShiroUtils.getUserId());
		return JsonMsgBeanUtils.defaultSeccess();
	}
	
}
