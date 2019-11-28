package com.example.demo.modules.test.vo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/** 
  * @Component ‐‐‐‐ 注册为bean 
  * @PropertySource ‐‐‐‐ 指定其他配置文件位置 
  * @ConfigurationProperties ‐‐‐‐ 指定属性公有前缀 
  */
@Component
@PropertySource("classpath:config/applicationTest.properties")
@ConfigurationProperties(prefix = "com.hqyj")
public class ApplicationConfigTestBean {
	private String name;
	private int age;
	private String description;
	private String random;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRandom() {
		return random;
	}
	public void setRandom(String random) {
		this.random = random;
	}
}
