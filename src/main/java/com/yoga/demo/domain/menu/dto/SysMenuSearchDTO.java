package com.yoga.demo.domain.menu.dto;

import com.yoga.demo.common.PageCondition;

public class SysMenuSearchDTO extends PageCondition{

	private static final long serialVersionUID = 7928498753600484729L;
	
	private String name;
	private String url;

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
	
	
	
}
