package com.Gary.GaryRESTful.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.Gary.GaryRESTful.filter.ValidateCodeFilter;
import com.Gary.GaryRESTful.handler.LoginFailureHandler;
import com.Gary.GaryRESTful.handler.LoginSuccessHandler;
import com.Gary.GaryRESTful.properties.GarySecurityProperties;


//Web应用安全适配器
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	//告诉SpringSecurity密码用什么加密的
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	private LoginSuccessHandler loginSuccessHandler;

	@Autowired
	private LoginFailureHandler loginFailureHandler;
	 
	@Autowired
	private GarySecurityProperties garySecurityProperties;
	
	@Autowired	
	private DataSource dataSource;
	
	//负责操作数据库
	public PersistentTokenRepository  persistentTokenRepository()
	{
		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(dataSource);
		return tokenRepository;
	}
	
	@Autowired	
	public UserDetailsService userDetailService;
	
	
	protected void configure(HttpSecurity http) throws Exception{
		
		//声明我们自己写的过滤器
		ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
		//给过滤器赋值
		validateCodeFilter.setAuthenticationFailureHandler(loginFailureHandler);
		validateCodeFilter.setGarySecurityProperties(garySecurityProperties);
		validateCodeFilter.afterPropertiesSet();
		
		//表单验证(身份认证)
		http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
			.formLogin()
			//自定义登陆页面
			.loginPage("/require")
			//如果URL为loginPage,则用SpringSecurity中自带的过滤器去处理该请求
			.loginProcessingUrl("/loginPage")
			//配置登陆成功调用loginSuccessHandler
			.successHandler(loginSuccessHandler)
			//配置登陆失败调用loginFailureHandler
			.failureHandler(loginFailureHandler)
			//记住我功能
			.and()
			.rememberMe()
			//配置persistentTokenRepository
			.tokenRepository(persistentTokenRepository())
			//配置过期秒数
			.tokenValiditySeconds(garySecurityProperties.getRememberMeSeconds())
			//配置userDetailsService
			.userDetailsService(userDetailService)
			.and()
			//请求授权
			.authorizeRequests()
			//在访问我们的URL时，我们是不需要省份认证，可以立即访问
			.antMatchers("/login.html","/require","/code/image","/code/sms").permitAll()
			//所有请求都被拦截，跳转到(/login请求中)
			.anyRequest()
			//都需要我们身份认证
			.authenticated()
			//SpringSecurity保护机制
			.and().csrf().disable();
	}
	
}
