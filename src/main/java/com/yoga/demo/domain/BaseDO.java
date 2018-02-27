package com.yoga.demo.domain;

import java.io.Serializable;
import java.util.Date;

public abstract class BaseDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3376975672784113587L;
	
	protected Integer id;
	private Boolean delete;
	private Date createTime;
	private Date lastUpdateTime;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Boolean getDelete() {
		return delete;
	}
	public void setDelete(Boolean delete) {
		this.delete = delete;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	
	

}