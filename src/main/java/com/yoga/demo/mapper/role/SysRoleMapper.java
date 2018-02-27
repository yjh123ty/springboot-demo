package com.yoga.demo.mapper.role;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yoga.demo.domain.shiro.SysRole;
import com.yoga.demo.domain.shiro.dto.SysRoleSearchDTO;
import com.yoga.demo.mapper.BaseMapper;

public interface SysRoleMapper extends BaseMapper<SysRole>{

	List<String> listUserRoles(Integer userId);

	int countList(SysRoleSearchDTO roleSearchDTO);

	List<SysRole> list(SysRoleSearchDTO roleSearchDTO);

	void deleteRolePermission(@Param("roleId")Integer roleId, @Param("permissionId")Integer permissionId);
	
	void deleteRoleMenu(@Param("roleId")Integer roleId, @Param("menuId")Integer menuId);

	void saveRolePermission(@Param("roleId")Integer roleId, @Param("permissionId")Integer permissionId);

	List<SysRole> listAll();

	void saveRoleMenu(@Param("roleId")Integer roleId, @Param("menuId")Integer menuId);

	SysRole getRoleMenusByPrimaryKey(Integer roleId);
	
}
