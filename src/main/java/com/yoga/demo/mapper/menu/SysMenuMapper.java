package com.yoga.demo.mapper.menu;

import java.util.List;

import com.yoga.demo.domain.menu.SysMenu;
import com.yoga.demo.domain.menu.dto.SysMenuSearchDTO;
import com.yoga.demo.domain.menu.vo.SysMenuTreeVO;
import com.yoga.demo.mapper.BaseMapper;

public interface SysMenuMapper extends BaseMapper<SysMenu>{

	List<SysMenu> getUserMenusByLoginUser(String loginName);

	int countList(SysMenuSearchDTO condition);

	List<SysMenu> list(SysMenuSearchDTO condition);
	
	List<SysMenu> getParentMenus();

	List<SysMenuTreeVO> getMenuTree();

}
