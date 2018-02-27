package com.yoga.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yoga.demo.common.Page;
import com.yoga.demo.domain.shiro.SysPermission;
import com.yoga.demo.domain.shiro.dto.SysPermSearchDTO;
import com.yoga.demo.mapper.permission.SysPermissionMapper;
import com.yoga.demo.service.SysPermissionService;

@Service
public class SysPermissionServiceImpl implements SysPermissionService {
	@Autowired
	private SysPermissionMapper sysPermissionMapper;
	
	@Override
	public List<SysPermission> listAllPermissions() {
		return sysPermissionMapper.listAllPermissions();
	}

	@Override
	public Page<SysPermission> list(SysPermSearchDTO permSearchDTO) {
		Page<SysPermission> page = new Page<SysPermission>(permSearchDTO.getPageNumber(), permSearchDTO.getPageSize(), sysPermissionMapper.countList(permSearchDTO));
		if(page.getTotal() > 0) {
			page.setRows(sysPermissionMapper.list(permSearchDTO));
        } else {
            page.setRows(new ArrayList<SysPermission>());
        }
        return page;
	}

	@Override
	public void updateByPrimaryKeySelective(SysPermission perm) {
		sysPermissionMapper.updateByPrimaryKeySelective(perm);
	}

	@Override
	public void insert(SysPermission perm) {
		perm.setAvailable(Boolean.TRUE);
		sysPermissionMapper.insert(perm);
	}

	@Override
	public SysPermission selectByPrimaryKey(Integer id) {
		return sysPermissionMapper.selectByPrimaryKey(id);
	}

	@Override
	public void deleteByPrimaryKey(Integer id, Integer userId) {
		sysPermissionMapper.deleteByPrimaryKey(id, userId);
	}

}
