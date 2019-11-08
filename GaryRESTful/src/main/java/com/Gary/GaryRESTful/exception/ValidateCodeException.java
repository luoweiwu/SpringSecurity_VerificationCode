package com.Gary.GaryRESTful.exception;

import org.springframework.security.core.AuthenticationException;


//AuthenticationException是springsecurity中所有异常的基类
public class ValidateCodeException extends AuthenticationException{

	public ValidateCodeException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

}
