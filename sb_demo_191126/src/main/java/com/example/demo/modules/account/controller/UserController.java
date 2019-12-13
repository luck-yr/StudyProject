package com.example.demo.modules.account.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.modules.account.entity.User;
import com.example.demo.modules.account.service.UserService;

@Controller
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserService userService;	
	
	/**
  	  *  插入一个用户user  增
	 * @param user
	 * @return
	 */
	@PostMapping(value = "add" ,consumes = "application/json")
	@ResponseBody
	public User add(@RequestBody User user) {
		User user_db = userService.addUserByUser(user);
		return user_db;
	}
	
	/**
	 * 删除一个用户   删
	 * @param userId
	 */
	@DeleteMapping("delete/{userId}")
	@ResponseBody
	public void delete(@PathVariable int userId) {
		userService.deleteUserByUserId(userId);
	}
	
	/**
	 * 更改用户信息   改
	 * @param userId
	 */
	@PostMapping(value = "update",consumes = "application/json")
	@ResponseBody
	public User update(@RequestBody User user) {
		User user_db = userService.updateUserByUser(user);
		return user_db;
	}
	
	/**
	 * 根据usrId查询用户
	 * @param userId
	 * @return
	 */
	@RequestMapping("select/{userId}")
	@ResponseBody
	public User select(@PathVariable int userId){
		User user_db = userService.selectUserByUserId(userId);
		return user_db;
	}
	
	/**
	 * 全查用户
	 * @return
	 */
	@RequestMapping("select")
	@ResponseBody
	public List<User> select(){
		
		List<User> users = userService.selectUser();
		return users;
	}
}
