package com.yoga.demo.service;

import java.util.List;

import com.yoga.demo.common.Page;
import com.yoga.demo.domain.menu.SysMenu;
import com.yoga.demo.domain.menu.dto.SysMenuSearchDTO;
import com.yoga.demo.domain.menu.vo.SysMenuTreeVO;

public interface SysMenuService {

	List<SysMenu> getUserMenusByLoginUser(String loginName);

	List<SysMenu> list(SysMenuSearchDTO sysMenuSearchDTO);

	SysMenu selectByPrimaryKey(Integer id);

	List<SysMenu> getParentMenus();
	
	Page<SysMenu> getSysMenus(SysMenuSearchDTO sysMenuSearchDTO);

	List<SysMenuTreeVO> getMenuTree();

	void updateByPrimaryKeySelective(SysMenu menu);

	void insert(SysMenu menu);

	void deleteByPrimaryKey(Integer id);

}
