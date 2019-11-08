package com.Gary.GaryRESTful.validate.code;

import java.time.LocalDateTime;

public class ValidateCode {

	//发送短信code
	private String code;
	
	//当前系统时间
	private LocalDateTime expireTime;

	public ValidateCode(String code,int expireTime)
	{
		this.code = code;
		this.expireTime = LocalDateTime.now().plusSeconds(expireTime);
	}
	
	public ValidateCode(String code,LocalDateTime expireTime)
	{
		this.code = code;
		this.expireTime = expireTime;
	}
	
	//判断当前时间是否存在过期之后
	public boolean isExpired()
	{

		return LocalDateTime.now().isAfter(expireTime);
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public LocalDateTime getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(LocalDateTime expireTime) {
		this.expireTime = expireTime;
	}
	
	
	
}
