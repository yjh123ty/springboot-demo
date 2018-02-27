package com.yoga.demo.domain.product.dto;

import com.yoga.demo.common.PageCondition;

public class SearchProductDTO extends PageCondition{

	private static final long serialVersionUID = 3745505813767561345L;
	
	private Integer bargainFlag;
	
	private String name;

	public Integer getBargainFlag() {
		return bargainFlag;
	}

	public void setBargainFlag(Integer bargainFlag) {
		this.bargainFlag = bargainFlag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
