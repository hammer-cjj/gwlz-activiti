package com.zsga.cf.gwlz.exceptions;

/**
 * 登陆session失效异常
 * @author quadcopter
 *
 */
public class UserSessionException extends RuntimeException {

	private static final long serialVersionUID = 800649670956979061L;
	
	public UserSessionException(String msg) {
		super(msg);
	}

}
