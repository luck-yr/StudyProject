package com.example.demo.modules.account.service;

import java.util.List;

import com.example.demo.modules.account.entity.User;

public interface UserService {

	User addUserByUser(User user);

	void deleteUserByUserId(int userId);

	User updateUserByUser(User user);

	User selectUserByUserId(int userId);

	List<User> selectUser();

}
