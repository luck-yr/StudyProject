package com.example.demo.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Configuration ‐‐‐‐ 配置类 
 * @AutoConfigureAfter ‐‐‐‐ 控制配置类的加载顺序 
 */
@Configuration
@AutoConfigureAfter({WebMvcAutoConfiguration.class})
public class WebServerConfig {

	@Value("${server.http.port}")
	private int httpPort;
	
	/**
	 * http连接器
	 */
	//@Bean ---- 生成bean放入Spring容器，告诉Spring 这个方法将会返回一个对象，这个对象要注册为Spring应用上下文中的bean；
	@Bean
	public Connector connector() {
		Connector connector = new Connector();
		connector.setScheme("http");
		connector.setPort(httpPort);
		return connector;
	}
	
	/**
	 * 1、重新注册ServletWebServerFactory bean
	 * 2、以TomcatServletWebServerFactory实现，重写postProcessContext方法
	 * 3、设置安全收集器SecurityCollection，匹配所有URL
	 * 4、设置安全约束SecurityConstraint，加入约束类型为CONFIDENTIAL(秘密的)
	 * 5、注入上下文
	 * 6、如果需要同时支持 http 和 https，还需要添加 http 连接器
	 */
	@Bean
	public ServletWebServerFactory ServletWebServerFactory() {
		TomcatServletWebServerFactory tomcatServletWebServerFactory = new TomcatServletWebServerFactory() {

			@Override
			protected void postProcessContext(Context context) {
				SecurityCollection collection = new SecurityCollection();
				collection.addPattern("/**");
				
				SecurityConstraint constraint = new SecurityConstraint();
				constraint.addCollection(collection);
				constraint.setUserConstraint("CONFIDENTIAL");
				
				context.addConstraint(constraint);
			}
			
		};
		tomcatServletWebServerFactory.addAdditionalTomcatConnectors(connector());
		return tomcatServletWebServerFactory;
	}
	
}
