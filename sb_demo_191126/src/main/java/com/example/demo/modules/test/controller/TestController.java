package com.example.demo.modules.test.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.modules.test.entity.City;
import com.example.demo.modules.test.entity.Country;
import com.example.demo.modules.test.service.TestService;
import com.example.demo.modules.test.vo.ApplicationConfigTestBean;
import com.github.pagehelper.PageInfo;

/**
 * @author Administrator
 *
 */
/**
 * @author Administrator
 *
 */
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
	 * 下载文件
	 * 响应头信息
	 * 'Content-Type': 'application/octet-stream',
	 * 'Content-Disposition': 'attachment;filename=req_get_download.js'
	 * @return ResponseEntity ---- spring专门包装响应信息的类
	 */
	@RequestMapping("/download")
	@ResponseBody
	public ResponseEntity<Resource> download( String fileName) {
		try {
			Resource resource = new UrlResource(
					Paths.get("e:/fortest"+File.separator+fileName).toUri());
			if(resource.exists() && resource.isReadable()) {
				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + 
								resource.getFilename() + "\"")
						.body(resource);
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	  *       上传多个文件
	 */
	@RequestMapping("uploadBatchFile")
	public String uploadBatchFile(@RequestParam MultipartFile[] files, RedirectAttributes redirectAttributes) {
		boolean isempty =true;
		for (MultipartFile file : files) {
			if(file.isEmpty()) {
				continue;
			}
			String fileName = file.getOriginalFilename();
			String destFileName = "e:/fortest" + File.separator + fileName;
			File destFile = new File(destFileName);
			try {
				file.transferTo(destFile);
				isempty=false;
			} catch (IllegalStateException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("message2", "upload failed");
			}
			
		}
		if(isempty) {
			redirectAttributes.addFlashAttribute("message2", "please select file");
			return "redirect:/test/index";
		}
		redirectAttributes.addFlashAttribute("message2", "upload success");
		return "redirect:/test/index";
	}
	
	/**
	 * 上传单个文件，虽然是form表单，但file是以参数的形式传递的，采用requestParam注解接收MultipartFile
	 */
	@RequestMapping("upload")
	public String upload(@RequestParam MultipartFile file,RedirectAttributes redirectAttributes) {
		if(file.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "please select file");
			return "redirect:/test/index";
		}
		String filename = file.getOriginalFilename();
		String destFileName = "e:/fortest" + File.separator +filename;
		File destFile = new File(destFileName);
		try {
			file.transferTo(destFile);
			redirectAttributes.addFlashAttribute("message", "upload success");
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("message", "upload failed");
		}
		return "redirect:/test/index";
	}
	
	@RequestMapping("index")
	public String testPage(ModelMap modelMap) {
		
		modelMap.addAttribute("thtext", "This is th:text test");
		modelMap.addAttribute("checked",true);
		modelMap.addAttribute("currentNumber", "99");
		modelMap.addAttribute("thymeleafTitle", "");
		modelMap.addAttribute("changeType", "checkbox");
		modelMap.addAttribute("baiduUrl", "http://www.baidu.com");
		modelMap.addAttribute("country", getcountryByCountryId(522));
		modelMap.addAttribute("city", getCitiesByCountryId(522).get(1));
		modelMap.addAttribute("updateCityUri", "/test/city");
		modelMap.addAttribute("cities", getCitiesByCountryId(522));
		
//		modelMap.addAttribute("template", "test/index");
		return "index";
	}
	
	/**
	 * 过滤器测试
	 * @param req
	 * @param value
	 * @return
	 */
	@RequestMapping(value = "filter")
	@ResponseBody
	public String filer(HttpServletRequest req,@RequestParam(name = "key",defaultValue = "111") String value) {
		String value2 = req.getParameter("key");
		return "this is a spring boot."+value+"---------"+value2;
	}
	
	/**
	 * 删除城市
	 */
	@DeleteMapping(value="city/{cityId}")
	@ResponseBody
	public void deleteCity(@PathVariable int cityId) {
		testService.deleteCity(cityId);
	}
	
	/**
	 * 更改城市
	 * 接受form表单数据 ---- application/x-www-form-urlencoded || @ModelAttribute
	 */
	@PostMapping(value = "city",consumes = "application/x-www-form-urlencoded")
	@ResponseBody
	public City updateCityByCity(City city) {
		return testService.updateCityByCity( city);
	}
	
	/**
	 * 插入城市
	 * 接受json数据 ---- @RequestBody || application/json
	 */
	@PostMapping(value = "city",consumes = "application/json")
	@ResponseBody
	public City insertCityByCity(@RequestBody City city) {
		return testService.insertCityByCity(city);
	}
	
	/**
	 * 分页查询城市
	 */
	@RequestMapping(value = "pageCities/{currentPage}/{pageSize}")
	@ResponseBody
	public PageInfo<City> getCitiesByPage(@PathVariable int currentPage,@PathVariable int pageSize){
		return testService.getCitiesByPage(currentPage,pageSize);
	}
	
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
