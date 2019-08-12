package com.zsga.cf.gwlz.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;



/**
 * 拦截器来限定文件上传大小
 * @author quadcopter
 *
 */
public class FileUploadInterceptor extends HandlerInterceptorAdapter {
	private long maxSize;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 判断是否文件上传
		if (request != null && ServletFileUpload.isMultipartContent(request)) {
			ServletRequestContext ctx = new ServletRequestContext(request);
			// 获取上传文件尺寸大小
			long requestSize = ctx.contentLength();
			if (requestSize > maxSize) {
				// 当上传文件大小超过指定大小限制后，模拟抛出MaxUploadSizeExceededException异常
				throw new MaxUploadSizeExceededException(maxSize);
			}
		}
		return true;
	}

	public void setMaxSize(long maxSize) {
		this.maxSize = maxSize;
	}
}
