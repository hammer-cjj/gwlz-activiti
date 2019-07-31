package com.zsga.cf.gwlz.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.zsga.cf.gwlz.pojo.User;

/**
 * 对未登录用户进行拦截,没在使用
 * @author quadcopter
 *
 */
public class LoginInterceptor implements HandlerInterceptor  {

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		User user = (User) request.getSession().getAttribute("currentUser");
		if (null != user) {
			return true;
		}
		// 若不满足登录验证，则直接跳转到帐号登录页面
		//request.getRequestDispatcher("/error.jsp").forward(request, response);
		return false;
	}

}
