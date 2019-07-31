package com.zsga.cf.gwlz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zsga.cf.gwlz.mapper.UserMapper;
import com.zsga.cf.gwlz.pojo.User;
import com.zsga.cf.gwlz.pojo.UserExt;
import com.zsga.cf.gwlz.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;

	/**
	 * 登陆
	 */
	@Override
	public User login(User user) {
		return userMapper.login(user);
	}

	/**
	 * 查询所有用户
	 */
	@Override
	public List<UserExt> list() {
		return userMapper.list();
	}

	/**
	 * 查询用户名是否已经存在
	 */
	@Override
	public User findUserByUserName(String userName) {
		return userMapper.findUserByUserName(userName);
	}

	/**
	 * 添加用户
	 */
	@Override
	public int addUser(User user) {
		return userMapper.addUser(user);
	}

	/**
	 * 修改用户
	 */
	@Override
	public int updateUser(User user) {
		return userMapper.updateUser(user);
	}

	/**
	 * 删除用户
	 */
	@Override
	public int removeUser(List<String> ids) {
		return userMapper.removeUser(ids);
	}

	/**
	 * 修改密码
	 */
	@Override
	public int modifyPassword(User user) {
		return userMapper.modifyPassword(user);
	}

	/**
	 * 查询任务责任人
	 */
	@Override
	public List<User> findUser(User user) {
		return userMapper.findUser(user);
	}

	/**
	 * 查询任务参与人
	 */
	@Override
	public List<User> findCy(User user) {
		return userMapper.findCy(user);
	}

	/**
	 * 查询搜索栏上我参与页面的任务责任人
	 */
	@Override
	public List<User> findSearchZrUser(User user) {
		return userMapper.findSearchZrUser(user);
	}

	/**
	 * 根据id查询用户
	 */
	@Override
	public User findUserById(int id) {
		return userMapper.findUserById(id);
	}

}
