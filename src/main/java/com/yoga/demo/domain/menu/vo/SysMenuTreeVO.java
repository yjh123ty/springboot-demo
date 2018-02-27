package com.yoga.demo.domain.menu.vo;

import java.io.Serializable;
import java.util.List;

public class SysMenuTreeVO implements Serializable{

	private static final long serialVersionUID = -6973677619296055945L;
	
	private Integer nodeId;
	private String text; 
	private Integer parentId;
	private List<SysMenuTreeVO> nodes;
	
	
	public Integer getNodeId() {
		return nodeId;
	}
	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public List<SysMenuTreeVO> getNodes() {
		return nodes;
	}
	public void setNodes(List<SysMenuTreeVO> nodes) {
		this.nodes = nodes;
	}
	
	
}
