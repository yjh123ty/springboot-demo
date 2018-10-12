package com.yoga.demo.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.classification.InterfaceAudience.Private;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Objects;
import com.yoga.demo.common.Page;
import com.yoga.demo.common.exception.BusinessException;
import com.yoga.demo.domain.dto.UserInfoSearchDTO;
import com.yoga.demo.domain.shiro.UserInfo;
import com.yoga.demo.mapper.role.SysRoleMapper;
import com.yoga.demo.mapper.user.UserInfoMapper;
import com.yoga.demo.service.UserInfoService;
import com.yoga.demo.utils.Tools;
import com.yoga.demo.utils.poi.PageExcelUtil;
import com.yoga.demo.utils.upload.ImageUploadUtils;

@Service
public class UserInfoServiceImpl implements UserInfoService{
	
	private ExecutorService executors = Executors.newCachedThreadPool();
	
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

	@Override
	public void exportUsers(String title, Map<String, String> headerMap, UserInfoSearchDTO searchDTO,
			OutputStream out) throws IOException {
		searchDTO.setPageSize(PageExcelUtil.SUGGEST_EACH_PAGE_SIZE);
		List<UserInfo> lists = null;
		//最大允许页数
		int maxPage = PageExcelUtil.SUGGEST_MAX_PAGE_SIZE / PageExcelUtil.SUGGEST_EACH_PAGE_SIZE;
        int page = 1;
        
        PageExcelUtil pageExcelUtil = new PageExcelUtil(title, headerMap, UserInfo.class, "yyyy-MM-dd HH:mm:ss");
        
        do {
        	searchDTO.setPageNumber(page);
        	lists = userInfoMapper.listUsers(searchDTO);
        	for (UserInfo item : lists) {
        		//TODO: 格式处理，状态
			}
        	//excel分页
        	if(CollectionUtils.isNotEmpty(lists) || page == 1){
        		pageExcelUtil.appendExcel(page, lists);
        		page++;
        		if(lists.size() < PageExcelUtil.SUGGEST_EACH_PAGE_SIZE) {
                    break;
                }
        	}
        }
        while(CollectionUtils.isNotEmpty(lists) || page <= maxPage);
        
        pageExcelUtil.printStream(out);
		
	}
	
	
	//测试事务处理的方法: 异步执行
	//   在线程池中异步处理的数据依然是未更新之前的数据信息：
	//   例如虽然在线程池中先提交了删除请求，在线程池中后续提交更新请求，读取得到的用户状态仍然是未删除。
	//   这就证明在同一个service方法下，事务要么全部提交，要么全部回滚。
	@Override
	public void updateByThread(String syncflag){
		
		List<Future<String>> fList = new ArrayList<>();
		
		
		UserInfo userInfo = userInfoMapper.selectByPrimaryKey(20);
		System.err.println("UserId为 20 的用户：" + userInfo);
		
		if (Objects.equal(syncflag, "async")) {
			//异步执行
			executors.submit(new TaskHandler(1));
			System.err.println("提交 更新 任务到线程池... 时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			
			executors.submit(new TaskHandler(2));
			System.err.println("提交 删除 任务到线程池... 时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			
		}else if (Objects.equal(syncflag, "sync")) {
			delete(20);
			
			UserInfo newUser = new UserInfo();
			newUser.setUid(20);
			newUser.setMobile("1802222111");
			update(newUser, null);
		}
		
		System.err.println("完成。。。");
		
		
	}

	@Override
	public List<UserInfo> listAllUser() {
		return userInfoMapper.listAllUsers();
	}


	public class TaskHandler implements Callable{
		private int operate;
		
		public TaskHandler(Integer operate){
			this.operate = operate;
		}


		@Override
		public String call() throws Exception {
			
			UserInfo userInfo = userInfoMapper.selectByPrimaryKey(20);
			
			if(Objects.equal(operate, 1)){
				Thread.sleep(6000);
				System.err.println("userInfo.getIsDelete() : " + userInfo.getIsDelete());
				if (!userInfo.getIsDelete()) {
					UserInfo newUser = new UserInfo();
					newUser.setUid(20);
					newUser.setMobile("1802222111");
					update(newUser, null);
				}else
					throw new RuntimeException("未找到该用户..");
				
			}else if(Objects.equal(operate, 2)){
				Thread.sleep(2000);
				delete(20);
				System.err.println("删除用户完成  时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				System.err.println("用户状态是否被删除： " + userInfo.getIsDelete());
				
			}
			
			System.err.println("任务处理完成 ： " + operate + " 时间： " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			return "FINISH";
		}
		
	}

}
