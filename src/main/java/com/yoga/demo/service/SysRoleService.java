package com.yoga.demo.service;

import java.util.List;

import com.yoga.demo.common.Page;
import com.yoga.demo.domain.shiro.SysRole;
import com.yoga.demo.domain.shiro.dto.SysRoleSearchDTO;

public interface SysRoleService {

	Page<SysRole> list(SysRoleSearchDTO roleSearchDTO);

	void save(SysRole role);

	void update(SysRole role);

	SysRole selectByPrimaryKey(int roleId);

	void delete(Integer uid);

	List<SysRole> listAll();

	void saveOrUpdateRolePerms(Integer roleId, String permIds);

	void saveOrUpdateRoleMenus(Integer roleId, List<Integer> menuIds);

	SysRole getRoleMenusByPrimaryKey(Integer roleId);

}
