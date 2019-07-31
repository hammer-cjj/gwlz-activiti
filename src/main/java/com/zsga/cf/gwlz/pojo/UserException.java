package com.zsga.cf.gwlz.pojo;

/**
 * 统一异常处理：用户登陆session失效
 * @author quadcopter
 *
 */
public class UserException extends RuntimeException {

	private static final long serialVersionUID = 1068022655435870637L;
	
	public UserException() {
		super();
	}
	
	public UserException(String message) {
		super(message);
	}
	
	public UserException(Throwable cause) {
		super(cause);
	}
	
	public UserException(String message, Throwable cause) {
		super(message, cause);
	}
}
