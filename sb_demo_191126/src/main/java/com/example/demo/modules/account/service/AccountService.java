package com.example.demo.modules.account.service;

import java.util.List;

import com.example.demo.modules.account.entity.Resource;
import com.example.demo.modules.account.entity.Role;
import com.example.demo.modules.account.entity.User;
import com.example.demo.modules.common.vo.Result;

public interface  AccountService {

	List<User> selectAllUsers();

	List<Role> selectAllRolls();

	List<Resource> selectAllResources();

	void deleteUserByUserId(int userId);

	Result addUserRoleByUser(User user);

	List<Role> selectRolesByUserId(int userId);

	Result editRole(Role role);

	void deleteRoleByRoleId(int roleId);

	void deleteResourceByResourceId(int resourceId);

	List<Role> selectRolesByResourceId(int resourceId);

	Result editResource(Resource resource);

	Result doLogin(User user);

	User selectUserByUserName(String userName);

	Result doRegister(User user);

	Result userNameIsExist(String userName);

	List<Resource> selectResoucesByRoleId(int roleId);

}
