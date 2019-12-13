package com.example.demo.config.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.filter.ParameterFilter;
import com.example.demo.interceptor.UriInterceptor;

/**
 * web mvc相关配置类
 * @author yr
 */
@Configuration
@AutoConfigureAfter({WebMvcAutoConfiguration.class})
public class WebMvcConfig implements WebMvcConfigurer {

	@Autowired
	private UriInterceptor uriInterceptor;
	
	/**
	 * 添加拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(uriInterceptor).addPathPatterns("/**");
	}

	/**
	 * 注册参数过滤器
	 * @return
	 */
	@Bean
	public FilterRegistrationBean<ParameterFilter> parameterFilter(){
		FilterRegistrationBean<ParameterFilter> filterRegist = new FilterRegistrationBean<>();
		filterRegist.setFilter(new ParameterFilter());
		return filterRegist;
	}
}
