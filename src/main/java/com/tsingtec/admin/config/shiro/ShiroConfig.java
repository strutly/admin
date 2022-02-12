package com.tsingtec.admin.config.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.tsingtec.admin.handle.filter.KickoutSessionFilter;
import com.tsingtec.admin.handle.filter.RetryLimitHashedCredentialsMatcher;
import com.tsingtec.admin.handle.filter.ShiroLoginFilter;
import com.tsingtec.admin.handle.filter.ShiroPermissionsFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro的配置类
 * @author lenovo
 *
 */
@Slf4j
@Configuration
@EnableTransactionManagement
public class ShiroConfig {

	@Resource(name="ehCacheManager")
	private EhCacheManager ehCacheManager;

	/**
	 * ShiroFilterFactoryBean 处理拦截资源文件过滤器
	 *	</br>1,配置shiro安全管理器接口securityManage;
	 *	</br>2,shiro 连接约束配置filterChainDefinitions;
	 */
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(
			SecurityManager securityManager) {
		//shiroFilterFactoryBean对象
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		// 配置shiro安全管理器 SecurityManager
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		//添加kickout认证
		HashMap<String, Filter> hashMap = new HashMap<String, Filter>();
		hashMap.put("kickout", kickoutSessionFilter());
		hashMap.put("authc", new ShiroLoginFilter());
		hashMap.put("perms", new ShiroPermissionsFilter());

		LogoutFilter logoutFilter = new LogoutFilter();
		logoutFilter.setRedirectUrl("/login");

		hashMap.put("logout", logoutFilter);

		shiroFilterFactoryBean.setLoginUrl("/login");

		Map<String, String> filterMap = new LinkedHashMap<String, String>();

		filterMap.put("/logout", "logout");
		filterMap.put("/**", "anon");
		filterMap.put("/manager/**", "kickout");
		filterMap.put("/home/**", "kickout");

		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
		shiroFilterFactoryBean.setFilters(hashMap);
		return shiroFilterFactoryBean;
	}

	/**
	 * shiro安全管理器设置realm认证和ehcache缓存管理
	 *
	 * @return
	 */
	@Bean
	public SecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 设置realm.
		securityManager.setRealm(shiroRealm());
		// //注入ehcache缓存管理器;
		securityManager.setCacheManager(ehCacheManager);
		// //注入session管理器;
		securityManager.setSessionManager(sessionManager());
		return securityManager;
	}

	/**
	 * 身份认证realm; (账号密码校验；权限等)
	 *
	 * @return
	 */
	@Bean
	public UserRealm shiroRealm() {
		UserRealm shiroRealm = new UserRealm();
		shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
		return shiroRealm;
	}


	/**
	 * 凭证匹配器
	 */
	@Bean
	public RetryLimitHashedCredentialsMatcher hashedCredentialsMatcher() {
		RetryLimitHashedCredentialsMatcher retryLimitHashedCredentialsMatcher = new RetryLimitHashedCredentialsMatcher(ehCacheManager);
		retryLimitHashedCredentialsMatcher.setHashAlgorithmName("SHA-256");//散列算法:MD2、MD5、SHA-1、SHA-256、SHA-384、SHA-512等。
		retryLimitHashedCredentialsMatcher.setHashIterations(1024);//散列的次数，默认1次， 设置两次相当于 md5(md5(""));
		retryLimitHashedCredentialsMatcher.setLimitCount(5);//账号登录重试次数
		return retryLimitHashedCredentialsMatcher;
	}

	/**
	 * 配置ShiroDialect，用于thymeleaf和shiro标签配合使用
	 */
	@Bean
	public ShiroDialect shiroDialect() {
		return new ShiroDialect();
	}

	/**
	 * EnterpriseCacheSessionDAO shiro sessionDao层的实现；
	 * 提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
	 */
	@Bean
	public SessionDAO enterCacheSessionDAO() {
		EnterpriseCacheSessionDAO enterCacheSessionDAO = new EnterpriseCacheSessionDAO();
		//添加缓存管理器
		//enterCacheSessionDAO.setCacheManager(ehCacheManager);
		//添加ehcache活跃缓存名称（必须和ehcache缓存名称一致）
		enterCacheSessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
		return enterCacheSessionDAO;
	}

	@Bean
	public DefaultWebSessionManager sessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setCacheManager(ehCacheManager);
		sessionManager.setSessionDAO(enterCacheSessionDAO());
		//是否开启删除无效的session对象  默认为true
		sessionManager.setDeleteInvalidSessions(true);
		sessionManager.setSessionIdUrlRewritingEnabled(false);
		//是否开启定时调度器进行检测过期session 默认为true
		sessionManager.setSessionValidationSchedulerEnabled(true);
		//全局会话超时时间（单位毫秒）1个小时
		sessionManager.setGlobalSessionTimeout(60 * 1000 * 60);
		sessionManager.setSessionValidationInterval(60 * 1000 * 10);
		return sessionManager;
	}


	@Bean
	public KickoutSessionFilter kickoutSessionFilter() {
		KickoutSessionFilter kickoutSessionFilter = new KickoutSessionFilter();
		//使用cacheManager获取相应的cache来缓存用户登录的会话；用于保存用户—会话之间的关系的；
		//这里我们还是用之前shiro使用的ehcache实现的cacheManager()缓存管理
		//也可以重新另写一个，重新配置缓存时间之类的自定义缓存属性
		kickoutSessionFilter.setCacheManager(ehCacheManager);
		//用于根据会话ID，获取会话进行踢出操作的；
		kickoutSessionFilter.setSessionManager(sessionManager());
		//是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；踢出顺序。
		kickoutSessionFilter.setKickoutAfter(false);
		//同一个用户最大的会话数，默认1；比如2的意思是同一个用户允许最多同时两个人登录；
		kickoutSessionFilter.setMaxSession(10);
		//被踢出后重定向到的地址；
		kickoutSessionFilter.setKickoutUrl("/login?kickout=1");
		return kickoutSessionFilter;
	}

	/**
	 * 开启Shiro的注解@RequiresRoles,@RequiresPermissions
	 */
	@Bean
	public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		advisorAutoProxyCreator.setProxyTargetClass(true);
		return advisorAutoProxyCreator;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
		return authorizationAttributeSourceAdvisor;
	}
}
