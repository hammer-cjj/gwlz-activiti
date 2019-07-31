package com.zsga.cf.gwlz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zsga.cf.gwlz.pojo.Dept;
import com.zsga.cf.gwlz.service.DeptService;

@Controller
@RequestMapping("/dept")
public class DeptController {
	@Autowired
	private DeptService deptService;
	
	@RequestMapping("/findDept")
	@ResponseBody
	public List<Dept> findDept() {
		return deptService.list();
	}
}
