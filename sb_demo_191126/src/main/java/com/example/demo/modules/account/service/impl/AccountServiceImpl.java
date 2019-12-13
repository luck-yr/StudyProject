package com.example.demo.modules.account.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.modules.account.dao.AccountDao;
import com.example.demo.modules.account.entity.Resource;
import com.example.demo.modules.account.entity.Role;
import com.example.demo.modules.account.entity.User;
import com.example.demo.modules.account.service.AccountService;
import com.example.demo.modules.common.vo.Result;
import com.example.demo.utils.MD5Util;
@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountDao accountDao;

	@Override
	public List<User> selectAllUsers() {
		return accountDao.selectAllUsers();
	}

	@Override
	public List<Role> selectAllRolls() {
		return accountDao.selectAllRolls();
	}

	@Override
	public List<Resource> selectAllResources() {
		return accountDao.selectAllResources();
	}

	@Override
	public void deleteUserByUserId(int userId) {
		accountDao.deleteUserByUserId(userId);
	}

	@Override
	public Result addUserRoleByUser(User user) {
		
		if(user == null) {
			return new Result(500,"user is null ,please check your user");
		}
		
		//添加前先删除原本存在的UserRole
		accountDao.deleteUserRoleByUserId(user.getUserId());
		if(user.getRoles() != null) {
			for (Role role : user.getRoles()) {
				accountDao.addUserRoleByUser(user.getUserId(),role.getRoleId());
			}
		}
		Result result = new Result(200,"success",user);
		return result;
	}

	@Override
	public List<Role> selectRolesByUserId(int userId) {
		return accountDao.selectRolesByUserId(userId);
	}

	@Override
	public Result editRole(Role role) {
		if(role.getRoleId()>0) {
			accountDao.updateRoleByRole(role.getRoleName(),role.getRoleId());
			return new Result(200,"update success");
		}
		if(role.getRoleId()==0)	{
			accountDao.insertRoleByRole(role.getRoleName());
			return new Result(200,"insert success");
		}
		return new Result(500,"roleId has erro");
	}

	@Override
	public void deleteRoleByRoleId(int roleId) {
		accountDao.deleteRoleByRoleId(roleId);
	}

	@Override
	public void deleteResourceByResourceId(int resourceId) {
		accountDao.deleteResourceByResourceId(resourceId);
		accountDao.deleteRoleResourceByResourceId(resourceId);
	}

	@Override
	public List<Role> selectRolesByResourceId(int resourceId) {
		return accountDao.selectRolesByResourceId(resourceId);
	}

	@Override
	public Result editResource(Resource resource) {
		if(resource.getResourceId() == 0) {
			accountDao.addResourceByResource(resource);
			if(resource.getRoles() == null) {
				return new Result(200, "insert success", resource);
			}else {
				for (Role role : resource.getRoles()) {
					accountDao.addRoleResourceBy(resource.getResourceId(),role.getRoleId());
				}
			}
			return new Result(200, "insert success", resource);
		}
		if(resource.getResourceId()>0) {
			accountDao.updateResourceByResource(resource);
			if(resource.getRoles() == null) {
				return new Result(200, "update success", resource);
			}else {
				accountDao.deleteRoleResourceByResourceId(resource.getResourceId());
				for (Role role : resource.getRoles()) {
					accountDao.addRoleResourceBy(resource.getResourceId(),role.getRoleId());
				}
			}
			return new Result(200, "update success", resource);
		}
		return new Result(500, "insert fail ,please check your resourceId");
	}

	@Override
	public Result doLogin(User user) {
		Subject subject = SecurityUtils.getSubject();
		
		UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(), MD5Util.getMD5(user.getPassword()));
		token.setRememberMe(user.getRememberMe());
		subject.login(token);
		subject.checkRoles();
		return new Result(200, "login success", user);
	}

	@Override
	public User selectUserByUserName(String userName) {
		return accountDao.selectUserByUserName(userName);
	}

	@Override
	public Result doRegister(User user) {
		User user_db = accountDao.selectUserByUserName(user.getUserName());
		if(user_db != null) {
			return new Result(500, "userName is exist");
		}
		
		user.setCreateDate(new Date());
		
		user.setPassword(MD5Util.getMD5(user.getPassword()));
		accountDao.insertUserByUser(user);
		return new Result(200, "register success", user);
	}

	@Override
	public Result userNameIsExist(String userName) {
		User user = accountDao.selectUserByUserName(userName);
		if(user != null) {
			return new Result(500, "userName is exist");
		}
		return new Result(200,"ok");
	}

	@Override
	public List<Resource> selectResoucesByRoleId(int roleId) {
		return accountDao.selectResoucesByRoleId(roleId);
	}
}
