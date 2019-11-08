package com.Gary.GaryRESTful.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import com.Gary.GaryRESTful.controller.ValidateCodeController;
import com.Gary.GaryRESTful.exception.ValidateCodeException;
import com.Gary.GaryRESTful.properties.GarySecurityProperties;
import com.Gary.GaryRESTful.validate.code.ImageCode;


//继承OncePerRequestFilter，保证这个filter只会执行一次
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean{

	private AuthenticationFailureHandler authenticationFailureHandler;
	
	//操作session域的工具
	private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
	
	private GarySecurityProperties garySecurityProperties;
	
	private Set<String> urls = new HashSet<String>();
	
	//为了处理/user/*的形式 
	private AntPathMatcher antPathMatcher = new AntPathMatcher();
	
	//在garySecurityProperties.code.image.url    /user,/user/*
	//当Bean组装好之后回调用这个函数
	@Override
	public void afterPropertiesSet() throws ServletException {
		super.afterPropertiesSet();
		
		//切割用户配置的url
		String[] configUrls = StringUtils.split(garySecurityProperties.getCode().getImage().getUrl(), ",");
		
		//将数组放入urls中
		for(String configURL : configUrls)
		{
			urls.add(configURL);
		}
		//loginPage一定会用到这个Filter，所以我们必须加上
		urls.add("/loginPage");
			
	}
	
	
	public GarySecurityProperties getGarySecurityProperties() {
		return garySecurityProperties;
	}

	public void setGarySecurityProperties(GarySecurityProperties garySecurityProperties) {
		this.garySecurityProperties = garySecurityProperties;
	}

	//Filter执行
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		//loginPage
		//if(StringUtils.pathEquals("/loginPage", request.getRequestURI()) && StringUtils.startsWithIgnoreCase(request.getMethod(), "post"))
		
		//判断是否有匹配的路径
		boolean action = false;
		
		//循环判断
		for(String url :urls)
		{
			if(antPathMatcher.match(url, request.getRequestURI()))
			{
				action = true;
			}
		}
		
		if(action)
		{
			//filter才会执行
			try
			{
				validate(new ServletWebRequest(request));
			}
			catch(ValidateCodeException e) {
				//判处验证码的异常
				authenticationFailureHandler.onAuthenticationFailure(request,response,e);
				//一旦出现异常，我们就不就不能继续执行(应该放行)，应该return
				return;
			}
			
		}
		
		//放行
		filterChain.doFilter(request, response);
		
	}

	//校验验证码是否正确
	private void validate(ServletWebRequest request) throws ServletRequestBindingException {
		
		// 获得session域中正确的验证码
		ImageCode codeInSession =  (ImageCode) sessionStrategy.getAttribute(request, ValidateCodeController.sessionKey);
		// 获得request域中的用户输入的验证码imageCode
		String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), "imageCode");

		// 判断用户输入的验证码是否为空
		if(StringUtils.isEmpty(codeInRequest))
		{
			throw new ValidateCodeException("验证码不能为空");
		}
		
		// 判断session域中的验证码是否为null
		if(codeInSession == null)
		{
			throw new ValidateCodeException("验证码不存在");
		}
		
		// 判断验证码是否过期
		if(codeInSession.isExpired())
		{
			throw new ValidateCodeException("验证码已过期");
		}
		
		// 校验两个验证码是否匹配
		if(!StringUtils.pathEquals(codeInSession.getCode(), codeInRequest))
		{
			//System.out.println("正确的："+codeInSession.getCode());
			//System.out.println("用户输入的："+codeInRequest);
			throw new ValidateCodeException("验证码不匹配");
		}
		
		// 将验证码从session域中移除
		sessionStrategy.removeAttribute(request, ValidateCodeController.sessionKey);
		
	}

	public AuthenticationFailureHandler getAuthenticationFailureHandler() {
		return authenticationFailureHandler;
	}

	public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
		this.authenticationFailureHandler = authenticationFailureHandler;
	}

	public SessionStrategy getSessionStrategy() {
		return sessionStrategy;
	}

	public void setSessionStrategy(SessionStrategy sessionStrategy) {
		this.sessionStrategy = sessionStrategy;
	}

}
