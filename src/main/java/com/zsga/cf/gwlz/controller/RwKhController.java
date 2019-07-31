package com.zsga.cf.gwlz.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zsga.cf.gwlz.pojo.RwKh;
import com.zsga.cf.gwlz.service.RwKhService;

@Controller
@RequestMapping("/rwKh")
public class RwKhController {

	@Autowired
	private RwKhService rwKhService;
	
	/**
	 * 查询任务考核表中的数据,每个人的考核分数
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
		List<RwKh> rwKhList = rwKhService.listRwKh();
		if (null != rwKhList) {
			PageInfo<RwKh> pageInfo = new PageInfo<>(rwKhList);
			modelMap.put("rows", pageInfo.getList());
			modelMap.put("total", pageInfo.getTotal());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
		}
		return modelMap;
	}
	
}
