package com.zsga.cf.gwlz.service;

import java.util.List;

import com.zsga.cf.gwlz.pojo.User;
import com.zsga.cf.gwlz.pojo.UserExt;

public interface UserService {
	User login(User user);
	List<UserExt> list();
	User findUserByUserName(String userName);
	int addUser(User user);
	int updateUser(User user);
	int removeUser(List<String> ids);
	int modifyPassword(User user);
	List<User> findUser(User user);
	List<User> findCy(User user);
	List<User> findSearchZrUser(User user);
	User findUserById(int id);
}
