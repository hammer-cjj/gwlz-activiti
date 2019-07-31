package com.zsga.cf.gwlz.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zsga.cf.gwlz.pojo.Rw;
import com.zsga.cf.gwlz.pojo.RwKh;
import com.zsga.cf.gwlz.pojo.User;
import com.zsga.cf.gwlz.pojo.UserExt;
import com.zsga.cf.gwlz.service.RwKhService;
import com.zsga.cf.gwlz.service.RwService;
import com.zsga.cf.gwlz.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private RwService rwService;
	@Autowired
	private RwKhService rwKhService;
	
	/**
	 * 用户登陆
	 * @param user
	 * @param request
	 * @return
	 */
	@RequestMapping("/userLogin")
	@ResponseBody
	public Map<String,Object> login(User user, HttpServletRequest request) {
		Map<String,Object> map=new HashMap<String, Object>();
		User resultUser = userService.login(user);
		if (resultUser == null) {
			map.put("success", false);
			map.put("errorMsg", "用户名或密码错误");
		} else {
			map.put("success", true);
			request.getSession().setAttribute("currentUser", resultUser);
//			request.getSession().setMaxInactiveInterval(5);
		}
		return map;
	}
	
	/**
	 * 查询所有用户以及他的角色
	 * @param request
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/userList")
	@ResponseBody
	public Map<String, Object> list(HttpServletRequest request, String page, String rows) {
		Map<String,Object> map=new HashMap<String, Object>();
		PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
		List<UserExt> userList = userService.list();	
		if (null != userList) {
			PageInfo<UserExt> pageInfo = new PageInfo<>(userList);
			map.put("rows", pageInfo.getList());
			map.put("total", pageInfo.getTotal());
		}
		return map;
	}
	
	/**
	 * 查询用户名是否已经存在
	 * @param userName
	 * @return
	 */
	@RequestMapping("/existUserName")
	@ResponseBody
	public Map<String, Object> existUserName(@RequestParam("userName")String userName) {
		Map<String,Object> map=new HashMap<String, Object>();
		if (null != userName) {
			User result = userService.findUserByUserName(userName);
			if (null != result) {
				map.put("exist", true);
			} else {
				map.put("exist", false);
			}
		}
		return map;
	}
	
	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	@RequestMapping("/userSave")
	@ResponseBody
	public Map<String, Object> userSave(User user) {
		Map<String,Object> map=new HashMap<String, Object>();
		int result = userService.addUser(user);
		if (result > 0) {
			map.put("success", true);
		} else {
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 修改用户
	 * @param user
	 * @return
	 */
	@RequestMapping("/userEdit")
	@ResponseBody
	public Map<String, Object> userEdit(User user) {
		Map<String,Object> map=new HashMap<String, Object>();
		int result = userService.updateUser(user);
		if (result > 0) {
			map.put("success", true);
		} else {
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 删除用户
	 * @param request
	 * @return
	 */
	@RequestMapping("/userDelete")
	@ResponseBody
	public Map<String, Object> userDelete(HttpServletRequest request) {
		Map<String,Object> map=new HashMap<String, Object>();
		String id=request.getParameter("ids");
		List<String> list=new ArrayList<String>();
		String[] strs = id.split(",");
		for (String str : strs) {
			list.add(str);
		}
		int result = userService.removeUser(list);
		if (result > 0) {
			map.put("success", true);
		} else {
			map.put("success", false);
		}
		return map;
	}

	/**
	 * 修改密码
	 * @param request
	 * @return
	 */
	@RequestMapping("/modifyPassword")
	@ResponseBody
	public Map<String, Object> modifyPassword(HttpServletRequest request) {
		Map<String,Object> map=new HashMap<String, Object>();
		String id = request.getParameter("id");
		String newPassword = request.getParameter("newPassword");
		User user = new User();
		user.setId(Integer.parseInt(id));
		user.setPassword(newPassword);
		int result = userService.modifyPassword(user);
		if (result > 0) {
			map.put("success", true);
		} else {
			map.put("success", false);
		}
		return map;
	}
	
	/**
	 * 退出登陆
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request) {
		request.getSession().removeAttribute("currentUser");
		return "redirect:/login.jsp";
	}
	
	/**
	 * 查询任务责任人
	 * @return
	 */
	@RequestMapping("/findUser")
	@ResponseBody
	public List<User> findUser(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("currentUser");
		return userService.findUser(user);
	}
	
	/**
	 * 查询搜索栏上我参与页面的任务责任人
	 * @return
	 */
	@RequestMapping("/findSearchZrUser")
	@ResponseBody
	public List<User> findSearchZrUser(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("currentUser");
		return userService.findSearchZrUser(user);
	}
	
	/**
	 * 查询任务参与人
	 * @param request
	 * @return
	 */
	@RequestMapping("/findCy")
	@ResponseBody
	public List<User> findCy(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("currentUser");
		return userService.findCy(user);
	}
}
