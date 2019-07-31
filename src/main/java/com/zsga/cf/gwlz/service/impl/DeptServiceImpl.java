package com.zsga.cf.gwlz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zsga.cf.gwlz.mapper.DeptMapper;
import com.zsga.cf.gwlz.pojo.Dept;
import com.zsga.cf.gwlz.service.DeptService;

@Service
public class DeptServiceImpl implements DeptService {

	@Autowired
	private DeptMapper deptMapper;
	
	/**
	 * 查询所有的部门
	 */
	@Override
	public List<Dept> list() {
		return deptMapper.list();
	}

}
