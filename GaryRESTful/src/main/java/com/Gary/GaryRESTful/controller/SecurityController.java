package com.Gary.GaryRESTful.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

	//拿到转发跳转到之前的请求
	private RequestCache requestCache = new HttpSessionRequestCache();
	
	//可以用来做重定向
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@RequestMapping("/require")
	//返回的状态码(401)
	@ResponseStatus(code=HttpStatus.UNAUTHORIZED)
	public String require(HttpServletRequest request , HttpServletResponse response) throws IOException
	{
		//拿到了之前的请求
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		if(savedRequest != null)
		{
			//url就是引发跳转之前我们的请求
			String url = savedRequest.getRedirectUrl();
			//判断之前的请求是否以html结尾
			if(StringUtils.endsWithIgnoreCase(url, ".html"))
			{
				//如果是，重定向到登陆页面
				redirectStrategy.sendRedirect(request, response, "/login.html");
			
			}

		}

		//如果不是，我们就让他身份认证
		return new String("需要身份认证");
	}
	

}
