package com.yoga.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yoga.demo.common.Page;
import com.yoga.demo.domain.shiro.SysRole;
import com.yoga.demo.domain.shiro.dto.SysRoleSearchDTO;
import com.yoga.demo.mapper.role.SysRoleMapper;
import com.yoga.demo.service.SysRoleService;

@Service
public class SysRoleServiceImpl implements SysRoleService{
	
	@Autowired
	private SysRoleMapper sysRoleMapper;

	@Override
	public Page<SysRole> list(SysRoleSearchDTO roleSearchDTO) {
		Page<SysRole> page = new Page<SysRole>(roleSearchDTO.getPageNumber(), roleSearchDTO.getPageSize(), sysRoleMapper.countList(roleSearchDTO));
		if(page.getTotal() > 0) {
            page.setRows(sysRoleMapper.list(roleSearchDTO));
        } else {
            page.setRows(new ArrayList<SysRole>());
        }
        return page;
	}

	@Override
	public void save(SysRole role) {
		sysRoleMapper.insert(role);
	}

	@Override
	public void update(SysRole role) {
		sysRoleMapper.updateByPrimaryKeySelective(role);
	}

	@Override
	public SysRole selectByPrimaryKey(int id) {
		return sysRoleMapper.selectByPrimaryKey(id);
	}

	@Override
	public void delete(Integer id) {
		sysRoleMapper.deleteByPrimaryKey(id);
		sysRoleMapper.deleteRolePermission(id, null);
	}

	@Override
	public List<SysRole> listAll() {
		return sysRoleMapper.listAll();
	}

	@Override
	public void saveOrUpdateRolePerms(Integer roleId, String permIds) {
		sysRoleMapper.deleteRolePermission(roleId, null);
		if(StringUtils.isNotBlank(permIds)){
			String[] permIdsStr = permIds.split(",");
			for (String permId : permIdsStr) {
				sysRoleMapper.saveRolePermission(roleId, Integer.valueOf(permId));
			}
		}
	}

	@Override
	public void saveOrUpdateRoleMenus(Integer roleId, List<Integer> menuIds) {
		sysRoleMapper.deleteRoleMenu(roleId, null);
		if(menuIds != null){
			for (Integer permId : menuIds) {
				sysRoleMapper.saveRoleMenu(roleId, permId);
			}
		}
	}

	@Override
	public SysRole getRoleMenusByPrimaryKey(Integer roleId) {
		return sysRoleMapper.getRoleMenusByPrimaryKey(roleId);
	}

}
