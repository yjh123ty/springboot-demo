package com.yoga.demo.service;

import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.yoga.demo.common.Page;
import com.yoga.demo.domain.dto.UserInfoSearchDTO;
import com.yoga.demo.domain.shiro.UserInfo;

public interface UserInfoService {
	UserInfo findByUsername(String username);
	
	UserInfo findUserInfoByUsername(String username);

	Set<String> listUserRoles(Integer userId);

	Set<String> listUserPerms(Integer userId);

	Page<UserInfo> listUsers(UserInfoSearchDTO userSearchDTO);

	UserInfo selectByPrimaryKey(Integer uid);

	void save(UserInfo userInfo, List<Integer> roleIdArrays);

	void update(UserInfo userInfo, List<Integer> roleIdArrays);

	void delete(Integer uid);
	
	void saveUserRole(Integer uid, Integer roleId);

	/**
	 * 上传头像
	 * @param uid
	 * @param file
	 * @return
	 */
	String uploadHeadIcon(Integer uid, MultipartFile file);

}
