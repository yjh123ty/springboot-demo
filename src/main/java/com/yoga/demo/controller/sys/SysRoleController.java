package com.yoga.demo.controller.sys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.yoga.demo.domain.menu.SysMenu;
import com.yoga.demo.domain.shiro.SysPermission;
import com.yoga.demo.domain.shiro.SysRole;
import com.yoga.demo.domain.shiro.dto.SysRoleSearchDTO;
import com.yoga.demo.service.SysRoleService;
import com.yoga.demo.utils.result.JsonMsgBeanUtils;

import io.swagger.annotations.Api;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("sysRole")
@Api("角色")
public class SysRoleController {
	
	@Autowired
	private SysRoleService sysRoleService;
	
	@RequestMapping(value = "index",method = RequestMethod.GET)
    public String toIndex(Model model){
        return "pages/sys/role/index";	
    }
	
	@RequestMapping(value = "list",method = RequestMethod.GET)
	@ResponseBody
    public Page<SysRole> listRoles(SysRoleSearchDTO roleSearchDTO){
		Page<SysRole> page = sysRoleService.list(roleSearchDTO);
		return page;
    }
	
	@RequestMapping(value = "editPerms", method = RequestMethod.GET)
	public ModelAndView toEditPerms(@RequestParam("id")Integer id, Model model){
		ModelAndView mav = new ModelAndView();
		if(null != id){
			
			String curIds = "";
			SysRole role = sysRoleService.selectByPrimaryKey(id);
			if(role != null){
				List<SysPermission> permissions = role.getPermissions();
				if(permissions != null && permissions.size() != 0){
					for (SysPermission sysPermission : permissions) {
						//已存储的权限（用于回显）
						curIds = curIds.concat(String.valueOf(sysPermission.getId())).concat(",");
					}
					System.err.println("curIds : " + curIds);
					mav.addObject("curIds", curIds);
				}
				mav.addObject("id", id);
			}
		}
		mav.setViewName("pages/sys/role/dialog/editPerms");
		return mav;	
	}
	
	@RequestMapping(value = "saveOrUpdate",method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("perm:edit")
	public JsonMsgBean saveOrUpdate(SysRole role){
		if(role.getId() != null){
			sysRoleService.update(role);
		}else{
			sysRoleService.save(role);
		}
		return JsonMsgBeanUtils.defaultSeccess();
	}
	
	@RequestMapping(value = "saveOrUpdateRolePerms",method = RequestMethod.POST)
	@ResponseBody
	public JsonMsgBean saveOrUpdateRolePerms(Integer roleId, String permIds){
		if(roleId != null){
			sysRoleService.saveOrUpdateRolePerms(roleId, permIds);
		}
		return JsonMsgBeanUtils.defaultSeccess();
	}
	
	@RequestMapping(value = "saveOrUpdateRoleMenus",method = RequestMethod.POST)
	@ResponseBody
	public JsonMsgBean saveOrUpdateRoleMenus(Integer roleId, @RequestParam(value = "menuIds[]", required = false)List<Integer> menuIds){
		if(roleId != null){
			sysRoleService.saveOrUpdateRoleMenus(roleId, menuIds);
		}
		return JsonMsgBeanUtils.defaultSeccess();
	}
	
	@RequestMapping(value = "edit",method = RequestMethod.GET)
    public ModelAndView toEdit(Integer id, Model model){
		ModelAndView mav = new ModelAndView("pages/sys/role/edit");
		//使用map来存储 当前已选择的权限 
		if(null != id){
			Map<String, String> currentPermissions = new HashMap<String, String>();
			SysRole role = sysRoleService.selectByPrimaryKey(id);
			if(role != null){
				List<SysPermission> permissions = role.getPermissions();
				if(permissions != null && permissions.size() != 0){
					for (SysPermission sysPermission : permissions) {
						//已存储的权限（用于回显）
						currentPermissions.put(String.valueOf(sysPermission.getId()), sysPermission.getName());
						System.err.println(String.valueOf(sysPermission.getId()) + " : " + sysPermission.getName());
					}
					JSONObject currentJson = JSONObject.fromObject(currentPermissions);
					String currentPerms = currentJson.toString();
					mav.addObject("currentPerms", currentPerms);
				}
				mav.addObject("role", role);
			}
		}
        return mav;
    }
	
	/**
	 * 删除
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "delete",method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("perm:del")
	@WebLog(desc="删除角色")
//	@RequiresPermissions("role:del")
	public JsonMsgBean delete(Integer id){
		sysRoleService.delete(id);
		return JsonMsgBeanUtils.defaultSeccess();
	}
	
	/**
	 * 选择菜单
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "editMenus", method = RequestMethod.GET)
	public ModelAndView toEditMenus(@RequestParam("id")Integer id, Model model){
		ModelAndView mav = new ModelAndView();
		if(null != id){
			String curIds = "";
			SysRole role = sysRoleService.getRoleMenusByPrimaryKey(id);
			if(role != null){
				List<SysMenu> menus = role.getMenus();
				if(menus != null && menus.size() != 0){
					for (SysMenu sysMenu : menus) {
						//已存储的权限（用于回显）
						curIds = curIds.concat(sysMenu.getId().toString()).concat(",");
					}
					System.err.println("curIds : " + curIds);
					mav.addObject("curIds", curIds);
				}
				mav.addObject("id", id);
			}
		}
		mav.setViewName("pages/sys/role/dialog/editMenus");
		return mav;	
	}
}
