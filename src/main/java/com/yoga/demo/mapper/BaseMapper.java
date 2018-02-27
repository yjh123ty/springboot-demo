package com.yoga.demo.mapper;

import java.io.Serializable;

public interface BaseMapper<T> {
	void insert(T entity);

	void insertSelective(T entity);
	
	void updateByPrimaryKey(T entity);

	void updateByPrimaryKeySelective(T entity);
	
	void delete(T entity);

	int deleteByPrimaryKey(Serializable id);
	
	T selectByPrimaryKey(Serializable id);
	
}
