package com.example.demo.modules.test.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.modules.test.entity.City;
import com.example.demo.modules.test.entity.Country;
import com.example.demo.modules.test.service.TestService;
import com.example.demo.modules.test.vo.ApplicationConfigTestBean;

@Controller
@RequestMapping(value = "test")
public class TestController {
	private final static Logger LOGGER = LoggerFactory.getLogger(TestController.class);

	@Value("${com.hqyj.name}")
	private String name;

	@Value("${com.hqyj.age}")
	private int age;

	@Value("${com.hqyj.description}")
	private String description;

	@Value("${com.hqyj.random}")
	private String random;

	@Autowired
	private ApplicationConfigTestBean configTestBean;

	@Autowired
	private TestService testService;
	
	/**
	 * 根据country name 查询国家信息
	 */
	@RequestMapping("/country")
	@ResponseBody
	public Country getCountryByCountryName(@RequestParam String countryName) {
		return testService.getCountryByCountryName(countryName);
	}
	
	/**
	 * 根据国家id查询所有城市
	 * @PathVariable --- 获取url路径上的参数
	 */
	@RequestMapping(value = "/cities/{countryId}")
	@ResponseBody
	public List<City> getCitiesByCountryId(@PathVariable int countryId) {
		return testService.getCitiesByCountryId(countryId);
	}
	
	/**
	 * 根据国家id查询国家信息
	 */
	@RequestMapping("/country/{countryId}")
	@ResponseBody
	public Country getcountryByCountryId(@PathVariable int countryId) {
		return testService.getcountryByCountryId(countryId);
	}
	
	//测试post接口访问
	@PostMapping(value = "post")
	@ResponseBody
	public String post() {
		return "this is a post interface";
	}
	
	// 全局配置文件部署配置
	@RequestMapping(value = "config")
	@ResponseBody
	public String config() {
		StringBuffer strb = new StringBuffer();
		strb.append(name).append("###name###").append("<br>").append(age).append("###age###").append("<br>")
				.append(description).append("###description###").append("<br>").append(random).append("###random###")
				.append("<br>");
		String str = strb.toString();
		return str;
	}

	// 日志文件测试
	@RequestMapping(value = "log")
	@ResponseBody
	public String log() {
		LOGGER.trace("This is trace log");
		LOGGER.debug("This is debug log");
		LOGGER.info("This is info log");
		LOGGER.warn("This is warn log");
		LOGGER.error("This is error log");

		return "This is  log test";
	}

	// 其他配置文件部署配置
	@RequestMapping(value = "other")
	@ResponseBody
	public String other() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(configTestBean.getName()).append("###name###").append("<br>")
				.append(configTestBean.getAge()).append("###age###").append("<br>")
				.append(configTestBean.getDescription()).append("###description###").append("<br>")
				.append(configTestBean.getRandom()).append("###random###").append("<br>");
		String str = stringBuffer.toString();
		return str;
	}

	// spring boot工程创建测试
	@RequestMapping(value = "open")
	@ResponseBody
	public String test() {
		return "Asuccess spring boot";
	}
}
