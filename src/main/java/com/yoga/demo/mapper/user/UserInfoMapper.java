package com.yoga.demo.mapper.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yoga.demo.domain.dto.UserInfoSearchDTO;
import com.yoga.demo.domain.shiro.UserInfo;

public interface UserInfoMapper {
	UserInfo findByUsername(String username);

	List<String> findPermissions(Integer uid);

	List<UserInfo> listUsers(UserInfoSearchDTO userSearchDTO);

	int countUsers(UserInfoSearchDTO userSearchDTO);

	UserInfo selectByPrimaryKey(Integer uid);

	void save(UserInfo userInfo);

	void update(UserInfo userInfo);

	void delete(Integer uid);
	
	void saveUserRole(@Param("uid")Integer uid, @Param("roleId")Integer roleId);
	
	/**
	 * 获取用户登录信息
	 * @param username
	 * @return
	 */
	UserInfo findUserInfoByUsername(String username);

	void deleteUserRole(Integer uid);
}
