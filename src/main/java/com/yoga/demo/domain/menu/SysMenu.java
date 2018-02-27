package com.yoga.demo.domain.menu;

import java.io.Serializable;
import java.util.List;

public class SysMenu implements Serializable{
	private static final long serialVersionUID = -269733914989270196L;
	
	private Integer id;
	private String name; 
	private String url; 
	private String urlIcon; 
	private Integer parentId;
	/** 排序 */
	private Integer order;
	
	/**上级菜单名 */
	private String parentName; 
	private List<SysMenu> children;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getUrlIcon() {
		return urlIcon;
	}
	public void setUrlIcon(String urlIcon) {
		this.urlIcon = urlIcon;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public List<SysMenu> getChildren() {
		return children;
	}
	public void setChildren(List<SysMenu> children) {
		this.children = children;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
	
	
}
