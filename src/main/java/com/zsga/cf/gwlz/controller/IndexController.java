package com.zsga.cf.gwlz.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面跳转
 * @author quadcopter
 *
 */
@Controller
public class IndexController {
	
	/**
	 * 跳转到即将超期页面
	 * @return
	 */
	/*
	@RequestMapping("/rwManageCq")
	public String rwManageCq() {
		return "rwManageCq";
	}
	*/
	
	/**
	 * 跳转到任务考核页面
	 * @return
	 */
	@RequestMapping("/rwKaohe")
	public String rwKaohe() {
		return "rwKaohe";
	}
	
	/**
	 * 跳转到意见列表页面
	 * @return
	 */
	@RequestMapping("/listYj")
	public String listYj() {
		return "listYj";
	}
	
	/**
	 * 跳转到写意见页面
	 * @return
	 */
	@RequestMapping("/writeYj")
	public String write() {
		return "writeYj";
	}
	
	/**
	 * 跳转到用户管理页面
	 * @return
	 */
	@RequestMapping("/userManage")
	public String userManager() {
		return "userManage";
	}
	
	/**
	 * 跳转到任务管理页面
	 * @return
	 */
	@RequestMapping("/rwManage")
	public String rwManage() {
		return "rwManage";
	}
	
	/**
	 * 跳转到我分配的任务界面
	 * @return
	 */
	@RequestMapping("/rwManageFp")
	public String rwManageFp(HttpServletRequest request) {
		return "rwManageFp";
	}
	
	/**
	 * 跳转到我负责的任务界面
	 * @return
	 */
	@RequestMapping("/rwManageZr")
	public String rwManageZr(HttpServletRequest request) {
		return "rwManageZr";
	}
	
	/**
	 * 跳转到我负责的参与界面
	 * @return
	 */
	@RequestMapping("/rwManageCy")
	public String rwManageCy(HttpServletRequest request) {
		return "rwManageCy";
	}
	
	/**
	 * 错误跳转 text/html
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/sessionError")
	public String sessionError(HttpServletRequest request,HttpServletResponse response) {
		
		PrintWriter out;
		try {
			out = response.getWriter();
			out.println("<html>");
			out.println("<script>");
			out.println("window.top.location='" + request.getContextPath() + "/login.jsp'");
			out.println("</script>");
			out.println("</html>");
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;
	}
}
