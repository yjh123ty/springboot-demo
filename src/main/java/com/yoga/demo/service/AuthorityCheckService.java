package com.yoga.demo.service;

import java.util.List;

/**
 * 权限检查 服务类
 * 
 * @author yoga
 */
public interface AuthorityCheckService {
	
	/**
	 * 检查某个角色是否有对应UI元素的查看数据权限（为freemarker自定义标签auth提供）
	 * @param roleIds
	 * @param key
	 * @param id
	 * @return
	 */
	boolean checkUIDataPermission(List<Integer> roleIds, String key, String id);
	
	/**
	 * 根据权限过滤调不能展示的属性，用于ajax请求过滤
	 * @param fullName
	 * @param roleIds
	 * @param data
	 */
	void filterDataByPermission(String fullName, List<Integer> roleIds, Object data);
	
	/**
     * 根据权限过脱敏指定的属性，用于ajax请求脱敏
     * @param fullName
     * @param roleIds
     * @param data
     */
    void desDataByPermission(String fullName, List<Integer> roleIds, Object data);
}
