package com.zsga.cf.gwlz.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zsga.cf.gwlz.exceptions.UserSessionException;
import com.zsga.cf.gwlz.pojo.RwExt;
import com.zsga.cf.gwlz.pojo.User;
import com.zsga.cf.gwlz.pojo.Yj;
import com.zsga.cf.gwlz.pojo.YjExt;
import com.zsga.cf.gwlz.service.YjService;

@Controller
@RequestMapping("/yj")
public class YjController {
	@Autowired
	private YjService yjService;
	
	/**
	 * 添加意见
	 * @param yj
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveYj")
	@ResponseBody
	public Map<String, Object> saveYj(Yj yj,HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		int resultCode = 0;
		User user = (User) request.getSession().getAttribute("currentUser");
		if (null != user) {
			yj.setUserId(user.getId());
			yj.setPubTime(new Date());
			resultCode = yjService.addYj(yj);
			if (resultCode <= 0) {
				modelMap.put("success", false);
			} else {
				modelMap.put("success", true);
			}
		} else {
			modelMap.put("success", false);
		}
		return modelMap;
	}
	
	/**
	 * 查询意见列表
	 * @param request
	 * @param page
	 * @param rows
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/list")
	public Map<String, Object> list(HttpServletRequest request,String page, String rows) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
		User user = (User) request.getSession().getAttribute("currentUser");
		if (null != user) {
			List<YjExt> yjExtList = yjService.listYj();
			if (null != yjExtList) {
				PageInfo<YjExt> pageInfo = new PageInfo<>(yjExtList);
				modelMap.put("rows", pageInfo.getList());
				modelMap.put("total", pageInfo.getTotal());
			}
			modelMap.put("success", true);
		} else {
			throw new UserSessionException("登陆超时，请重新登陆!");
		}
		return modelMap;
	}
	
	/**
	 * 按照Id查询意见
	 * @param id
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/findYj")
	public String findYj(@RequestParam("id")String id, HttpServletResponse response) throws IOException {
		Yj yj = yjService.findYj(Integer.parseInt(id));
		response.setCharacterEncoding("utf8");
		if (null != yj) {
			PrintWriter out = response.getWriter();
			out.println(yj.getYjContent());
			out.close();
		} 
		return null;
	}
}
