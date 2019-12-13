package com.example.demo.modules.account.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.modules.account.dao.UserDao;
import com.example.demo.modules.account.entity.User;
import com.example.demo.modules.account.service.UserService;
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userdao;

	@Override
	public User addUserByUser(User user) {
		userdao.addUserByUser(user);
		return user;
	}

	@Override
	public void deleteUserByUserId(int userId) {
		userdao.deleteUserByUserId(userId);
	}

	@Override
	public User updateUserByUser(User user) {
		userdao.updateUserByUser(user);
		return user;
	}

	@Override
	public User selectUserByUserId(int userId) {
		
		return userdao.selectUserByUserId(userId);
	}

	@Override
	public List<User> selectUser() {
		// TODO Auto-generated method stub
		return userdao.selectUsers();
	}
}
