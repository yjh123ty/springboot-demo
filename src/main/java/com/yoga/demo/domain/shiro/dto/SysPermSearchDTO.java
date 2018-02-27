package com.yoga.demo.domain.shiro.dto;

import com.yoga.demo.common.PageCondition;

public class SysPermSearchDTO extends PageCondition{

	private static final long serialVersionUID = -1376590306805966013L;
	
	private Integer id;
	
	private String name;
	

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

	
}
