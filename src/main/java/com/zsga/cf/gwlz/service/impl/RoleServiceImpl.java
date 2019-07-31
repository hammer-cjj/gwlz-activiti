package com.zsga.cf.gwlz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zsga.cf.gwlz.mapper.RoleMapper;
import com.zsga.cf.gwlz.pojo.Role;
import com.zsga.cf.gwlz.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
	@Autowired
	private RoleMapper roleMapper;

	@Override
	public List<Role> list() {
		return roleMapper.list();
	}

}
