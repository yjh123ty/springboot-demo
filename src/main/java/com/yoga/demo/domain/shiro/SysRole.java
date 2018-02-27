package com.yoga.demo.domain.shiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.yoga.demo.domain.menu.SysMenu;

public class SysRole implements Serializable{
	
	private static final long serialVersionUID = -1810751960822909496L;
	
	private Integer id; // 编号
    private String role; // 角色标识程序中判断使用,如"admin",这个是唯一的:
    private String description; // 角色描述,UI界面显示使用
    private Boolean available = Boolean.TRUE; // 是否可用,如果不可用将不会添加给用户
    
    private List<SysPermission> permissions;//角色 -- 权限关系：多对多关系;
    private List<SysMenu> menus = new ArrayList<>(0); //角色对应多个菜单
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean getAvailable() {
		return available;
	}
	public void setAvailable(Boolean available) {
		this.available = available;
	}
	public List<SysPermission> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<SysPermission> permissions) {
		this.permissions = permissions;
	}
	public List<SysMenu> getMenus() {
		return menus;
	}
	public void setMenus(List<SysMenu> menus) {
		this.menus = menus;
	}
    
    
}
