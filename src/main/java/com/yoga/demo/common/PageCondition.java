package com.yoga.demo.common;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.yoga.demo.utils.Tools;

public class PageCondition implements Serializable {

    private static final long serialVersionUID = -2869267930670674346L;

    protected Integer pageSize = 10;
    
    protected Integer pageNumber = 1;
    
    protected String orderBy;

    protected String orderSort;
    
    // 驼峰转下划线
    protected boolean camelToUnderline = true;

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
    	if(null != pageNumber)
    		this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    /***
     * ************bootstrap 分页 *************
     */
    public Integer getStart() {
    	return (pageNumber - 1) * pageSize;
    }
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    /***
     * ************bootstrap 分页   END *************
     */
    

    public String getOrderBy() {
    	if(!camelToUnderline)
    		return orderBy;
    	return orderBy == null ? null : Tools.camelToUnderline(orderBy);
    }

    public void setOrderBy(String orderBy) {
        if(StringUtils.isNotEmpty(orderBy)){
        	// 简单的防sql注入
        	orderBy = orderBy.replace(" ", "");
        }
        this.orderBy = orderBy;
    }

    public String getOrderSort() {
        return orderSort;
    }

    public void setOrderSort(String orderSort) {
        if(StringUtils.isNotEmpty(orderSort)){
        	// 防sql注入
        	if(!orderSort.equalsIgnoreCase("asc") && !orderSort.equalsIgnoreCase("desc"))
        		orderSort = "asc";
        }
        this.orderSort = orderSort;
    }

	public boolean isCamelToUnderline() {
		return camelToUnderline;
	}

	public void setCamelToUnderline(boolean camelToUnderline) {
		this.camelToUnderline = camelToUnderline;
	}


	
}

