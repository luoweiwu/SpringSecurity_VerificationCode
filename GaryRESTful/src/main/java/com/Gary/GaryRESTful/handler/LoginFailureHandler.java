package com.Gary.GaryRESTful.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.Gary.GaryRESTful.properties.GarySecurityProperties;
import com.Gary.GaryRESTful.properties.LoginType;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
//springsecurity默认处理器
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler{

	//将我们的authentication转换为json所需要的类
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	//我们自己的配置
	private GarySecurityProperties garySecurityProperties;
	
	@Override
	//登陆不成功产生的错误
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		System.out.println("登陆失败");
		
		if(LoginType.JSON.equals(garySecurityProperties.getLoginType()))
		{
			//设置返回的状态码 500 		SC_INTERNAL_SERVER_ERROR
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			
			response.setContentType("application/json;charset=UTF-8");
			//将我们authentication转换为json通过response对象以application/json写到页面
			response.getWriter().write(objectMapper.writeValueAsString(exception));
			
		}
		else
		{
			//调用父类中的方法，跳转到其它页面
			super.onAuthenticationFailure(request, response, exception);
		}
		
		
		
		
		
	}
	



	
	
}
