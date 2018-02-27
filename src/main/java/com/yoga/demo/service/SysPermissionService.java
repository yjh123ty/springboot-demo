package com.yoga.demo.service;

import java.util.List;

import com.yoga.demo.common.Page;
import com.yoga.demo.domain.shiro.SysPermission;
import com.yoga.demo.domain.shiro.dto.SysPermSearchDTO;

public interface SysPermissionService{
	List<SysPermission> listAllPermissions();

	Page<SysPermission> list(SysPermSearchDTO permSearchDTO);

	void updateByPrimaryKeySelective(SysPermission perm);

	void insert(SysPermission perm);

	SysPermission selectByPrimaryKey(Integer id);

	void deleteByPrimaryKey(Integer id, Integer userId);
}
