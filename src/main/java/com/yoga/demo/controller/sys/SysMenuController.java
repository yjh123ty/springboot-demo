package com.yoga.demo.controller.sys;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yoga.demo.common.JsonMsgBean;
import com.yoga.demo.domain.menu.SysMenu;
import com.yoga.demo.domain.menu.dto.SysMenuSearchDTO;
import com.yoga.demo.domain.menu.vo.SysMenuTreeVO;
import com.yoga.demo.service.SysMenuService;
import com.yoga.demo.utils.result.JsonMsgBeanUtils;

@Controller
@RequestMapping("sysMenu")
public class SysMenuController {
	@Autowired
	private SysMenuService sysMenuService;
	
	@RequestMapping(value = "index",method = RequestMethod.GET)
    public String toIndex(Model model){
        return "pages/sys/menu/index";	
    }
	
	@RequestMapping(value = "list",method = RequestMethod.GET)
	@ResponseBody
    public List<SysMenu> listMenus(SysMenuSearchDTO sysMenuSearchDTO){
		return sysMenuService.list(sysMenuSearchDTO);
    }
	
	@RequestMapping(value = "edit",method = RequestMethod.GET)
    public ModelAndView toEdit(Integer id, Model model){
		ModelAndView mav = new ModelAndView("pages/sys/menu/edit");
		List<SysMenu> parentMenus = sysMenuService.getParentMenus();
		mav.addObject("parentMenus", parentMenus);
		if(null != id){
			SysMenu menu = sysMenuService.selectByPrimaryKey(id);
			if(menu != null){
				mav.addObject("menu", menu);
			}
		}
        return mav;
    }
	
	@RequestMapping(value = "saveOrUpdate",method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("menu:edit")
	public JsonMsgBean saveOrUpdate(SysMenu menu){
		if(menu.getId() != null){
			sysMenuService.updateByPrimaryKeySelective(menu);
		}else{
			sysMenuService.insert(menu);
		}
		return JsonMsgBeanUtils.defaultSeccess();
	}
	
	@RequestMapping(value = "delete",method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("menu:del")
	public JsonMsgBean delete(Integer id){
		sysMenuService.deleteByPrimaryKey(id);
		return JsonMsgBeanUtils.defaultSeccess();
	}
	
	@RequestMapping(value = "getMenuTree",method = RequestMethod.GET)
	@ResponseBody
    public List<SysMenuTreeVO> getMenuTree(){
		return sysMenuService.getMenuTree();
    }
}
