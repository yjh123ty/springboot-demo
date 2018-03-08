package com.yoga.demo.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.yoga.demo.common.Page;
import com.yoga.demo.common.exception.BusinessException;
import com.yoga.demo.domain.dto.UserInfoSearchDTO;
import com.yoga.demo.domain.shiro.UserInfo;
import com.yoga.demo.mapper.role.SysRoleMapper;
import com.yoga.demo.mapper.user.UserInfoMapper;
import com.yoga.demo.service.UserInfoService;
import com.yoga.demo.utils.Tools;
import com.yoga.demo.utils.upload.ImageUploadUtils;

@Service
public class UserInfoServiceImpl implements UserInfoService{
	
	@Autowired
	private UserInfoMapper userInfoMapper;
	
	@Autowired
	private SysRoleMapper sysRoleMapper;
	
	@Override
	public UserInfo findByUsername(String username) {
		UserInfo findByUsername = userInfoMapper.findByUsername(username);
		return findByUsername;
	}

	@Override
	public Set<String> listUserRoles(Integer userId) {
		List<String> roles = sysRoleMapper.listUserRoles(userId);
		Set<String> rolesSet = new HashSet<>();
		for(String role : roles) {
			if(StringUtils.isNotBlank(role)) {
				rolesSet.addAll(Arrays.asList(role.trim().split(",")));
			}
		}
		return rolesSet;
	}

	@Override
	public Set<String> listUserPerms(Integer userId) {
		List<String> perms = userInfoMapper.findPermissions(userId);
		Set<String> permsSet = new HashSet<>();
		for(String perm : perms) {
			if(StringUtils.isNotBlank(perm)) {
				permsSet.addAll(Arrays.asList(perm.trim().split(",")));
			}
		}
		return permsSet;
	}

	@Override
	public Page<UserInfo> listUsers(UserInfoSearchDTO userSearchDTO) {
		Page<UserInfo> page = new Page<UserInfo>(userSearchDTO.getPageNumber(), userSearchDTO.getPageSize(), userInfoMapper.countUsers(userSearchDTO));
        if(page.getTotal() > 0) {
            page.setRows(userInfoMapper.listUsers(userSearchDTO));
        } else {
            page.setRows(new ArrayList<UserInfo>());
        }
        return page;
	}

	@Override
	public UserInfo selectByPrimaryKey(Integer uid) {
		return userInfoMapper.selectByPrimaryKey(uid);
	}

	@Override
	public void save(UserInfo userInfo, List<Integer> roleIdArrays) {
		if(userInfo.getState() == null){
			//用户初始化，需要认证
			userInfo.setState(UserInfo.UserState.UNCKECK);
		}
		userInfoMapper.save(userInfo);
		
		//根据用户id生成对应的关系
		if(roleIdArrays != null && roleIdArrays.size() > 0){
			for (Integer roleId : roleIdArrays) {
				userInfoMapper.saveUserRole(userInfo.getUid(), roleId);
			}
		}
		
	}

	@Override
	public void update(UserInfo userInfo, List<Integer> roleIdArrays) {
		//先根据用户id , 删除用户角色表中的相关数据，再重新生成对应的关系
		if(roleIdArrays != null && roleIdArrays.size() > 0){
			userInfoMapper.deleteUserRole(userInfo.getUid());
			for (Integer roleId : roleIdArrays) {
				userInfoMapper.saveUserRole(userInfo.getUid(), roleId);
			}
		}
		if(StringUtils.isNotBlank(userInfo.getMobile()) && Tools.isMobile(userInfo.getMobile())){
			throw new BusinessException("错误的手机格式！");
		}
		if(StringUtils.isNotBlank(userInfo.getEmail()) && Tools.isEmail(userInfo.getEmail())){
			throw new BusinessException("错误的邮箱格式！");
		}
		userInfoMapper.update(userInfo);
	}

	@Override
	public void delete(Integer uid) {
		userInfoMapper.delete(uid);
	}

	@Override
	public UserInfo findUserInfoByUsername(String username) {
		return userInfoMapper.findUserInfoByUsername(username);
	}

	@Override
	public void saveUserRole(Integer uid, Integer roleId) {
		userInfoMapper.saveUserRole(uid, roleId);
	}

	@Override
	public String uploadHeadIcon(Integer uid, MultipartFile file) {
		String uploadImgUrl = "";
		if(uid != null && file != null){
			String folderName = "headIcon";
            String dirPath = folderName.concat("/").concat(String.valueOf(uid)).concat("/").concat(UUID.randomUUID().toString().replaceAll("-", StringUtils.EMPTY));
            System.err.println(dirPath);
            uploadImgUrl = ImageUploadUtils.uploadImgDefault(file, dirPath);
		}
		return uploadImgUrl;
	}

}
