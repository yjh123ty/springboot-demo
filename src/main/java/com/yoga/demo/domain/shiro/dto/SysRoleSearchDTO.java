package com.yoga.demo.domain.shiro.dto;

import com.yoga.demo.common.PageCondition;

public class SysRoleSearchDTO extends PageCondition{

	private static final long serialVersionUID = -8968566003550904555L;

	private Integer available;
	
	private String description;

	public Integer getAvailable() {
		return available;
	}

	public void setAvailable(Integer available) {
		this.available = available;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
