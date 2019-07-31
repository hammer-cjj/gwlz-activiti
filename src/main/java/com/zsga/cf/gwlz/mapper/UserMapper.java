package com.zsga.cf.gwlz.mapper;

import java.util.List;

import com.zsga.cf.gwlz.pojo.User;
import com.zsga.cf.gwlz.pojo.UserExt;

public interface UserMapper {
	//根据用户名和密码登陆
	User login(User user);
	//查询所有用户
	List<UserExt> list();
	//查询用户名是否已经存在
	User findUserByUserName(String userName);
	//添加用户
	int addUser(User user);
	//修改用户
	int updateUser(User user);
	//删除用户
	int removeUser(List<String> ids);
	//更改密码
	int modifyPassword(User user);
	//查询任务责任人
	List<User> findUser(User user);
	//查询任务参与人
	List<User> findCy(User user);
	//查询我参与页面上搜索栏的，任务责任人
	List<User> findSearchZrUser(User user);
	//根据id查询用户
	User findUserById(int id);
}
