package com.yoga.demo.utils.config;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.yoga.demo.domain.shiro.SysPermission;
import com.yoga.demo.service.SysPermissionService;
import com.yoga.demo.service.impl.SysPermissionServiceImpl;
import com.yoga.demo.web.filter.shiro.MyShiroRealm;

@Configuration
public class ShiroConfig {
	/**
	 * ShiroFilterFactoryBean 处理拦截资源文件问题。
	 * 注意：单独一个ShiroFilterFactoryBean配置是会报错的，因为在
	 * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager Filter Chain定义说明
	 * 1、一个URL可以配置多个Filter，使用逗号分隔 
	 * 2、当设置多个过滤器时，全部验证通过，才视为通过
	 * 3、部分过滤器可指定参数，如perms，roles
	 */
	
	@Bean(name = "permissionService")
    public SysPermissionService permissionService() {
        return new SysPermissionServiceImpl();
    }
	
	@Bean
	public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
		System.err.println("ShiroConfiguration.shirFilter()");
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		//拦截器.
		Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();
		// 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
		shiroFilterFactoryBean.setLoginUrl("/login");
		// 登录成功后要跳转的链接
//		shiroFilterFactoryBean.setSuccessUrl("/");
		//未授权界面;
//		shiroFilterFactoryBean.setUnauthorizedUrl("/403");
		
		// 配置不会被拦截的链接 顺序判断
		filterChainDefinitionMap.put("/static/**", "anon");
		filterChainDefinitionMap.put("/index", "anon");
		filterChainDefinitionMap.put("/login", "anon");
		filterChainDefinitionMap.put("/checkLogin", "anon");
		filterChainDefinitionMap.put("/register/index", "anon");
		filterChainDefinitionMap.put("/register/doRegister", "anon");
		filterChainDefinitionMap.put("/captcha/img.html", "anon");
		filterChainDefinitionMap.put("/table/index", "anon");
		filterChainDefinitionMap.put("/table/users", "anon");
//		filterChainDefinitionMap.put("/swagger-resources/*", "anon");
//		filterChainDefinitionMap.put("/v2/api-docs", "anon");
//		filterChainDefinitionMap.put("/webjars/**", "anon");
//		filterChainDefinitionMap.put("/swagger-ui.html", "anon");
		
		//配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
		filterChainDefinitionMap.put("/logout", "logout");
		
		//动态授权的资源（权限MAP中的key只能对应一种情况，要么使用角色过滤，要么使用权限过滤）
		filterChainDefinitionMap.put("/test/userAdd", "perms[userInfo:add]");
		filterChainDefinitionMap.put("/test/userDel", "perms[userInfo:del]");
		
		//读取数据库中的权限资源映射
		SysPermissionService permissionService = permissionService();
		List<SysPermission> listAllPermissions = permissionService.listAllPermissions();
		if(listAllPermissions != null && listAllPermissions.size() > 0){
			for (SysPermission sysPermission : listAllPermissions) {
				String perms = "perms[".concat(sysPermission.getPermission()).concat("]");
				filterChainDefinitionMap.put(sysPermission.getUrl(), perms);
				System.err.println("设置系统权限  ===> " + sysPermission.getUrl() + " : " + perms);
			}
		}
		
//		filterChainDefinitionMap.put("/test/userAdd", "roles[admin]");
//		filterChainDefinitionMap.put("/test/userDel", "roles[admin]");
		
		//<!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
		//<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
		filterChainDefinitionMap.put("/**", "authc");
		
		
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}
	
	/**
     * 保证实现了Shiro内部lifecycle函数的bean执行
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    @ConditionalOnMissingBean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

	@Bean
	@DependsOn("lifecycleBeanPostProcessor")
	@ConditionalOnMissingBean
	public MyShiroRealm myShiroRealm() {
		MyShiroRealm myShiroRealm = new MyShiroRealm();
//		myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
		return myShiroRealm;
	}
	
//	@Bean(name = "defaultAdvisorAutoProxyCreator")
//    @ConditionalOnMissingBean
//    @DependsOn("lifecycleBeanPostProcessor")
//    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
//        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
//        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
//        return defaultAdvisorAutoProxyCreator;
//    }
//
//    @Bean
//    @ConditionalOnMissingBean
//    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(DefaultSecurityManager securityManager) {
//        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
//        aasa.setSecurityManager(securityManager);
//        return new AuthorizationAttributeSourceAdvisor();
//    }
	
	 /**
     * 用户授权信息Cache
     */
    @Bean(name = "shiroCacheManager")
    @ConditionalOnMissingBean
    public CacheManager cacheManager() {
        return new MemoryConstrainedCacheManager();
    }

	@Bean
	public SecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(myShiroRealm());
		securityManager.setCacheManager(cacheManager());
		return securityManager;
	}

	/**
	 * 凭证匹配器 （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了 ）
	 * 未使用
	 * 
	 * @return
	 */
	@Bean
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
//		hashedCredentialsMatcher.setHashAlgorithmName("md5");// 散列算法:这里使用MD5算法;
//		hashedCredentialsMatcher.setHashIterations(2);// 散列的次数，比如散列两次，相当于
														// md5(md5(""));
		return hashedCredentialsMatcher;
	}

//	@Bean(name = "simpleMappingExceptionResolver")
//	public SimpleMappingExceptionResolver createSimpleMappingExceptionResolver() {
//		SimpleMappingExceptionResolver r = new SimpleMappingExceptionResolver();
//		Properties mappings = new Properties();
//		mappings.setProperty("DatabaseException", "databaseError");// 数据库异常处理
//		mappings.setProperty("UnauthorizedException", "/403");
//		r.setExceptionMappings(mappings); // None by default
//		r.setDefaultErrorView("error"); // No default
//		r.setExceptionAttribute("ex"); // Default is "exception"
//		// r.setWarnLogCategory("example.MvcLogger"); // No default
//		return r;
//	}
}
