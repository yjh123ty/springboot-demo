package com.yoga.demo.mapper.permission;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yoga.demo.domain.shiro.SysPermission;
import com.yoga.demo.domain.shiro.dto.SysPermSearchDTO;
import com.yoga.demo.mapper.BaseMapper;

public interface SysPermissionMapper extends BaseMapper<SysPermission>{
	List<SysPermission> listAllPermissions();

	int countList(SysPermSearchDTO permSearchDTO);

	List<SysPermission> list(SysPermSearchDTO permSearchDTO);
	
	void deleteByPrimaryKey(@Param("id")Integer id, @Param("userId")Integer userId);

}