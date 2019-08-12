package com.zsga.cf.gwlz.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.zsga.cf.gwlz.exceptions.UserSessionException;

/**
 * 统一异常处理
 * @author quadcopter
 *
 */
@ControllerAdvice
@Component
public class GlovalExceptionHandler {
	
	/**
	 * 上传附件超过50M
	 * @param e
	 * @param request
	 * @return
	 */
	/*
	@ExceptionHandler(value=MaxUploadSizeExceededException.class)
	@ResponseBody
	public Map<String, Object> errorPage(MaxUploadSizeExceededException e, HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("success", false);
		modelMap.put("errMsg", "附件上传最大容量为50M");
		return modelMap;
	}
	*/
	
	@ExceptionHandler(value=Exception.class)
	@ResponseBody
	public Map<String, Object> handle(Exception e) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("success", false);
		//session超时
		if (e instanceof UserSessionException) { 
			//针对datagrid返回错误处理
			modelMap.put("errFlag", true);
			modelMap.put("rows", new ArrayList());
			modelMap.put("total", 0);
			modelMap.put("errMsg", "登陆超时，请重新登陆!");
		} else if (e instanceof MaxUploadSizeExceededException) {
			modelMap.put("errMsg", "附件上传超过最大值50M");
		} else {
			modelMap.put("errMsg", "未知错误!");
			System.out.println(e.getMessage());
		}
		return modelMap;
	}
}
