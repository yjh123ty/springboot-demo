package com.yoga.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yoga.demo.common.Page;
import com.yoga.demo.domain.menu.SysMenu;
import com.yoga.demo.domain.menu.dto.SysMenuSearchDTO;
import com.yoga.demo.domain.menu.vo.SysMenuTreeVO;
import com.yoga.demo.mapper.menu.SysMenuMapper;
import com.yoga.demo.service.SysMenuService;

@Service
public class SysMenuServiceImpl implements SysMenuService{
	
	@Autowired
	private SysMenuMapper sysMenuMapper;

	@Override
	public List<SysMenu> getUserMenusByLoginUser(String loginName) {
		return sysMenuMapper.getUserMenusByLoginUser(loginName);
	}

	@Override
	public List<SysMenu> list(SysMenuSearchDTO condition) {
        return sysMenuMapper.list(condition);
	}

	@Override
	public SysMenu selectByPrimaryKey(Integer menuId) {
		return sysMenuMapper.selectByPrimaryKey(menuId);
	}

	@Override
	public List<SysMenu> getParentMenus() {
		return sysMenuMapper.getParentMenus();
	}

	@Override
	public Page<SysMenu> getSysMenus(SysMenuSearchDTO sysMenuSearchDTO) {
		Page<SysMenu> page = new Page<SysMenu>(sysMenuSearchDTO.getPageNumber(), sysMenuSearchDTO.getPageSize(), sysMenuMapper.countList(sysMenuSearchDTO));
		if(page.getTotal() > 0) {
            page.setRows(sysMenuMapper.list(sysMenuSearchDTO));
        } else {
            page.setRows(new ArrayList<SysMenu>());
        }
        return page;
	}

	@Override
	public List<SysMenuTreeVO> getMenuTree() {
		return sysMenuMapper.getMenuTree();
	}

	@Override
	public void updateByPrimaryKeySelective(SysMenu menu) {
		sysMenuMapper.updateByPrimaryKeySelective(menu);
	}

	@Override
	public void insert(SysMenu menu) {
		sysMenuMapper.insert(menu);
	}

	@Override
	public void deleteByPrimaryKey(Integer id) {
		sysMenuMapper.deleteByPrimaryKey(id);
	}

}
