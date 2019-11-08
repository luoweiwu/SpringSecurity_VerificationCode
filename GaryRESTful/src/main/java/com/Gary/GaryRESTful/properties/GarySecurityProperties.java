package com.Gary.GaryRESTful.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gary.security")
public class GarySecurityProperties {
	
	//LoginType登陆的方式，默认为JSON(restful设计风格)
	private LoginType loginType = LoginType.JSON;

	private ValidateCodeProperties code = new ValidateCodeProperties();
	
	private int rememberMeSeconds = 60*60;
	
	public int getRememberMeSeconds() {
		return rememberMeSeconds;
	}

	public void setRememberMeSeconds(int rememberMeSeconds) {
		this.rememberMeSeconds = rememberMeSeconds;
	}

	public ValidateCodeProperties getCode() {
		return code;
	}

	public void setCode(ValidateCodeProperties code) {
		this.code = code;
	}

	public LoginType getLoginType() {
		return loginType;
	}

	public void setLoginType(LoginType loginType) {
		this.loginType = loginType;
	}
	
	
	
}
