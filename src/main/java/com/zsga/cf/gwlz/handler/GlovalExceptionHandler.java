package com.zsga.cf.gwlz.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zsga.cf.gwlz.exceptions.UserSessionException;

/**
 * 统一异常处理
 * @author quadcopter
 *
 */
@ControllerAdvice
@Component
public class GlovalExceptionHandler {
	
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
			modelMap.put("errMsg", e.getMessage());
		} else {
			modelMap.put("errMsg", "未知错误!");
			System.out.println(e.getMessage());
		}
		return modelMap;
	}
}
