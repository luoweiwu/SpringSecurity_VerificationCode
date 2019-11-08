package com.Gary.GaryRESTful.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.Gary.GaryRESTful.properties.GarySecurityProperties;
import com.Gary.GaryRESTful.properties.LoginType;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
//SavedRequestAwareAuthenticationSuccessHandler为SpringSecurity默认处理机制
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{

	//将我们的authentication转换为json所需要的类
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private GarySecurityProperties garySecurityProperties;
	
	@Override
	//登陆成功之后会调用的函数
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			//封装了我们的认证信息(发起的认证请求(ip，session)，认证成功后的用户信息)
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		System.out.println("登陆成功");
		
		
		if(LoginType.JSON.equals(garySecurityProperties.getLoginType()))
		{
			response.setContentType("application/json;charset=UTF-8");
			
			//将我们authentication转换为json通过response对象以application/json写到页面
			response.getWriter().write(objectMapper.writeValueAsString(authentication));
		}
		else
		{
			//调用父类中的方法，跳转到其它页面
			super.onAuthenticationSuccess(request, response, authentication);
		}
		
	}
	
}
