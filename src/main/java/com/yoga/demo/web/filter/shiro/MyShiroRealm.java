package com.yoga.demo.web.filter.shiro;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.yoga.demo.domain.shiro.UserInfo;
import com.yoga.demo.service.UserInfoService;

public class MyShiroRealm extends AuthorizingRealm{
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private UserInfoService userInfoService;
	
	/**
	 * Authorization 是授权访问控制，用于对用户进行的操作授权
	 * 授权(验证权限时调用)
	 */
	@Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        
		log.info("权限检查-->MyShiroRealm.doGetAuthorizationInfo()");
        
        UserInfo userInfo  = (UserInfo)SecurityUtils.getSubject().getPrincipal();
        Integer userId = userInfo.getUid();
    	//用户角色
  		Set<String> rolesSet = userInfoService.listUserRoles(userId);
  		//用户权限
  		Set<String> permsSet = userInfoService.listUserPerms(userId);
    
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRoles(rolesSet);
        authorizationInfo.addStringPermissions(permsSet);
        
        return authorizationInfo;
    }

	/**
	 * Authentication 是用来验证用户身份
	 * 认证(登录时调用)
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.err.println("MyShiroRealm.doGetAuthenticationInfo() :　认证(登录时调用)");
	    //获取用户的输入的账号.
	    String username = (String)token.getPrincipal();
	    String password = new String((char[]) token.getCredentials());
	    System.out.println(token.getCredentials());
	    //通过username从数据库中查找 User对象，如果找到，没找到.
	    //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
	    UserInfo userInfo = userInfoService.findByUsername(username);
	    System.out.println("----->>userInfo="+userInfo);
	    if(userInfo == null){
	        return null;
	    }
	    
	    //密码错误
        if(!password.equals(userInfo.getPassword())) {
            throw new IncorrectCredentialsException("账号或密码不正确");
        }
        
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(userInfo, password, getName());
	    
//	    SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
//	            userInfo, //用户名
//	            userInfo.getPassword(), //密码
//	            ByteSource.Util.bytes(userInfo.getCredentialsSalt()),//salt=username+salt
//	            getName()  //realm name
//	    );
	    return info;
	}


}
