 package com.example.demo.modules.account.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.modules.account.entity.Resource;
import com.example.demo.modules.account.entity.Role;
import com.example.demo.modules.account.entity.User;
import com.example.demo.modules.account.service.AccountService;
import com.example.demo.modules.common.vo.Result;

@Controller
@RequestMapping("account")
public class AccountController {

	@Autowired
	private AccountService accountService;
	
	/**
	 * @return到登录界面
	 */
	@RequestMapping("login")
	public String login() {
		
		return "baseindex";
	}
	
	/**
	 * 用户登出
	 * @return 重定向到login返回登录界面
	 */
	@RequestMapping("logout")
	public String logout() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return "redirect:login";
	}
	
	/**
	 * 点击登录按钮做登录判断
	 * @return
	 */
	@RequestMapping("doLogin")
	@ResponseBody
	public Result doLogin(@RequestBody User user) {
		return accountService.doLogin(user);
	}
	
	/**
	 * 点击register按钮添加一个新的user
	 * @return
	 */
	@RequestMapping("userNameIsExist")
	@ResponseBody
	public Result userNameIsExist(@RequestBody String userName) {
		return accountService.userNameIsExist(userName);
	}
	
	/**
	 * 点击register按钮添加一个新的user
	 * @return
	 */
	@RequestMapping("doRegister")
	@ResponseBody
	public Result doRegister(@RequestBody User user) {
		return accountService.doRegister(user);
	}
	
	/**
	 * @return跳转到register主页
	 */
	@RequestMapping("register")
	public String register() {
		return "baseindex";
	}
	
	/**
	 * @return跳转到dashboard主页
	 */
	@RequestMapping("dashboard")
	public String dashboard(ModelMap modelmap) {
		   String userName =	(String) SecurityUtils.getSubject().getPrincipal();
		    System.err.println("-----------------------------------------");
		    System.err.println(userName);
		    modelmap.addAttribute("userName", userName);
		return "index";
	}
	
	/**
	 *  全查user返回users给界面
	 *  全查role返回给界面备edit时使用
	 * @return到users界面
	 */
	@RequestMapping("users")
	@RequiresRoles(value={"admin", "manager"}, logical=Logical.OR)
	public String users(ModelMap modelMap) {
		List<User> users = accountService.selectAllUsers();
		List<Role> roles = accountService.selectAllRolls();
		modelMap.addAttribute("users", users);
		modelMap.addAttribute("roles", roles);
		return "index";
	}
	
	/**
	 *  全查roles返回roles给界面
	 * @return到roles界面
	 */
	@RequestMapping("roles")
	@RequiresRoles(value = {"admin","manager"},logical = Logical.OR)
	public String roles(ModelMap modelMap) {
		List<Role> roles = accountService.selectAllRolls();
		modelMap.addAttribute("roles", roles);
		return "index";
	}
	
	/**
	 *  全查resource返回resources给界面
	 * @return到resources界面
	 */
	@RequestMapping("resources")
	@RequiresRoles(value = {"admin","manager"},logical = Logical.OR)
	public String resources(ModelMap modelMap) {
		List<Resource> resources = accountService.selectAllResources();
		List<Role> roles = accountService.selectAllRolls();
		modelMap.addAttribute("roles", roles);
		modelMap.addAttribute("resources", resources);
		return "index";
	}
	
	/**
	 * 根据用户Id删除当前用户
	 * @param userId 从前端得到的用户的userId
	 * @return 重定向到users界面
	 */
	@RequestMapping("deleteUser/{userId}")
	@RequiresPermissions("deleteUser")
	public String deleteUser(@PathVariable int userId) {
		accountService.deleteUserByUserId(userId);
		return "redirect:../users";
	}
	
	/**
	 * @param roleId 从前端传过来的roleId用于指定删除role
	 * @return 重定向到roles界面
	 */
	@RequestMapping("deleteRole/{roleId}")
	@RequiresPermissions("deleteRole")
	public String deleteRole(@PathVariable int roleId) {
		accountService.deleteRoleByRoleId(roleId);
		return "redirect:/account/roles";
	}
	
	/**
	 * @param resourceId 根据前端传回来的id删除指定的resource
	 * @return 重定向到resources界面
	 */
	@RequestMapping("deleteResource/{resourceId}")
	@RequiresPermissions("deleteResource")
	public String deleteResourceByResourceId(@PathVariable int resourceId) {
		accountService.deleteResourceByResourceId(resourceId);
		return "redirect:/account/resources";
	}
	
	/**
	 * 修改user的角色
	 * @param user 将带有useId和roles的user对象传给该方法，然后向中间表中添加元素
	 */
	@RequestMapping(value = "editUser",method = RequestMethod.POST,consumes = "application/json")
	@ResponseBody
	public Result editUser(@RequestBody User user) {
		Result result = accountService.addUserRoleByUser(user);
		return result;
	}
	
	/**
	 * @param userId 通过userId查询roles列表
	 * @return 将roles列表返回给前端进行数据选中展示
	 */
	@RequestMapping("roles/user/{userId}")
	@ResponseBody
	public List<Role> selectRolesByUserId(@PathVariable int userId){
		
		List<Role> roles = accountService.selectRolesByUserId(userId);
		return roles;
	}
	
	/**
	 * @param role 从前端得到的role角色，对其进行增加或修改
	 * @return result给前端ajax data
	 */
	@RequestMapping("editRole")
	@ResponseBody
	public Result editRole(@RequestBody Role role) {
		Result result = accountService.editRole(role);
		return result;
	}
	
	/**
	 * @param resourceId 根据页面传来的resourceId查询所属的roles列表
	 * @return  返回一个Result给前端ajax
	 */
	@RequestMapping("roles/resource/{resourceId}")
	@ResponseBody
	public List<Role> selectRolesByResourceId(@PathVariable int resourceId) {
		List<Role> roles = accountService.selectRolesByResourceId(resourceId);
		return roles;
	}
	
	/**
	 * @param resource 从前端拿到的resource对象
	 * @return 返回一个result给前端ajax解析
	 */
	@RequestMapping("editResource")
	@ResponseBody
	public Result editResource(@RequestBody Resource resource) {
		Result result = accountService.editResource(resource);
		return result;
		
	}
	
	
	@RequestMapping("tubiao")
	public String tubiao() {
		return "index";
	}
	
	@RequestMapping("beijing")
	@ResponseBody
	public HashMap<String, Object> beijing(){
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<Integer> wk = new ArrayList<Integer>();
		wk.add(532);
		wk.add(524);
		wk.add(524);
		wk.add(495);
		wk.add(549);
		wk.add(565);
		wk.add(579);
		wk.add(583);
		wk.add(555);
		wk.add(576);
		wk.add(480);
		List<Integer> lk = new ArrayList<Integer>();
		lk.add(501);
		lk.add(494);
		lk.add(484);
		lk.add(477);
		lk.add(550);
		lk.add(543);
		lk.add(548);
		lk.add(548);
		lk.add(537);
		lk.add(532);
		lk.add(423);
		map.put("wk", wk);
		map.put("lk", lk);
		return map;
	}
}
