package com.example.demo.config.shiro;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;

/**
 * shiro配置类，配置如下bean
 * securityManager ---- shiro核心组件之一
 * ShiroFilterFactoryBean ---- shiro核心组件之一
 * ShiroDialect ---- shiro方言，支持thymeleaf标签
 * DefaultAdvisorAutoProxyCreator ---- 支持shiro注解
 * AuthorizationAttributeSourceAdvisor ---- 开启shiro注解
 * @author: HymanHu
 * @date: 2019年12月3日
 */
@Configuration
public class ShiroConfig {
	@Autowired
	private MyRealm myrealm;

	/**
	 * 配置shiro安全管理器
	 * @return
	 */
	@Bean
	public DefaultWebSecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(myrealm);
		// 增加记住我功能
		securityManager.setRememberMeManager(rememberMeManager());
		return securityManager;
	}
	
	/**
	 * 配置shiro过滤器工厂
	 * -----------------
	 * 拦截权限
	 * anon：匿名访问，无需登录
	 * authc：登录后才能访问
	 * user：登录过能访问
	 * logout：登出
	 * roles：角色过滤器
	 * ------------------
	 * URL匹配风格
	 * ?：匹配一个字符，如 /admin? 将匹配 /admin1，但不匹配 /admin 或 /admin/
	 * *：匹配零个或多个字符串，如 /admin* 将匹配 /admin 或/admin123，但不匹配 /admin/1
	 * **：匹配路径中的零个或多个路径，如 /admin/** 将匹配 /admin/a 或 /admin/a/b
	 * -----------------------
	 * 方法名不能乱写，如果我们定义为别的名称，又没有添加注册过滤器的配置，那么shiro会加载ShiroWebFilterConfiguration过滤器，
	 * 该过滤器会寻找shiroFilterFactoryBean，找不到会抛出异常
	 */
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean() {
		ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
		shiroFilter.setSecurityManager(securityManager());
		shiroFilter.setLoginUrl("/account/login");
		shiroFilter.setSuccessUrl("/account/dashboard");
//		shiroFilter.setUnauthorizedUrl("/error/403");
		
		// 如果登录页面用户名使用的不是userName，则需要添加过滤器，让shiro识别用户名对应的字段
//		Map<String, Filter> filters = new LinkedHashMap<String, Filter>();
//		FormAuthenticationFilter formAuthenticationFilter = new FormAuthenticationFilter();
//		formAuthenticationFilter.setUsernameParam("account");
//		filters.put("authc", formAuthenticationFilter);
//		shiroFilter.setFilters(filters);
		
		Map<String, String> map = new LinkedHashMap<String, String>();
		
		map.put("/static/**", "anon");
		map.put("/js/**", "anon");
		map.put("/css/**", "anon");
		map.put("/img/**", "anon");
		map.put("/account/login", "anon");
		map.put("/account/userNameIsExist", "anon");
		map.put("/account/doLogin", "anon");
		map.put("/account/register", "anon");
		map.put("/account/doRegister", "anon");
		map.put("/test/**", "anon");
		
		// 如果使用“记住我功能”，则采用user规则，如果必须要用户登录，则采用authc规则
		//map.put("/**", "authc");
		map.put("/**", "user");
		
		shiroFilter.setFilterChainDefinitionMap(map);
		return shiroFilter;
	}
	
	/**
	 * 注册shiro方言，让thymeleaf支持shiro标签
	 */
	@Bean
	public ShiroDialect shiroDialect() {
		return new ShiroDialect();
	}
	
	/**
	 * 自动代理类，支持Shiro的注解
	 */
	@Bean
	@DependsOn({"lifecycleBeanPostProcessor"})
	public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
		DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		advisorAutoProxyCreator.setProxyTargetClass(true);
		return advisorAutoProxyCreator;
	}
	
	
	
	 /**
     * 开启Shiro的注解
     */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(){
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
		return authorizationAttributeSourceAdvisor;
	}
	
	/**
	 * 记住我之SimpleCookie
	 */
	@Bean
	public SimpleCookie rememberMeCookie() {
	    //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
	    SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
	    //如果httyOnly设置为true，则客户端不会暴露给客户端脚本代码，使用HttpOnly cookie有助于减少某些类型的跨站点脚本攻击；
	    simpleCookie.setHttpOnly(true);
	    //记住我cookie生效时间,单位是秒
	    simpleCookie.setMaxAge(30 * 24 * 60 * 60);
	    return simpleCookie;
	}
	
	/**
	 * 记住我之cookie管理器
	 */
	@Bean
	public CookieRememberMeManager rememberMeManager() {
	    CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
	    //rememberme cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度（128 256 512 位），通过以下代码可以获取
	    //KeyGenerator keygen = KeyGenerator.getInstance("AES");
	    //SecretKey deskey = keygen.generateKey();
	    byte[] cipherKey = Base64.decode("wGiHplamyXlVB11UXWol8g==");
	    cookieRememberMeManager.setCipherKey(cipherKey);
	    cookieRememberMeManager.setCookie(rememberMeCookie());
	    return cookieRememberMeManager;
	}
	
	
	
}
