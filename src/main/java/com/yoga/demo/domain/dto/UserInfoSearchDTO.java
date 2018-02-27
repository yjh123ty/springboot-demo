package com.yoga.demo.domain.dto;

import com.yoga.demo.common.PageCondition;

public class UserInfoSearchDTO extends PageCondition{
	private static final long serialVersionUID = 9162632783543491483L;
	private String username;
	private String name;
	private Integer state;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	
	
}
