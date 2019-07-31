package com.zsga.cf.gwlz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zsga.cf.gwlz.pojo.Role;
import com.zsga.cf.gwlz.service.RoleService;

@Controller
@RequestMapping("/role")
public class RoleController {
	
	@Autowired
	private RoleService reolService;
	
	/**
	 * 查询角色
	 * @return
	 */
	@RequestMapping("/findRole")
	@ResponseBody
	public List<Role> findRole() {
		return reolService.list();
	}
}
