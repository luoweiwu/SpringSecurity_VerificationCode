package com.Gary.GaryRESTful.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.Gary.GaryRESTful.domain.User;
import com.Gary.GaryRESTful.repository.UserRepository;


//用SprinSecurity默认的登陆系统
//UserService要实现UserDetailsService接口
@Component
public class UserService implements UserDetailsService{

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	//spring security默认处理登陆(username为输入的username)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		//System.out.println(username);
		User user = userRepository.findUserByUsername(username);
		//用户名，密码，权限
		if(user == null)
		{
			throw new UsernameNotFoundException(username);
		}
		//User实现UserDetails接口
		return new User(user.getUsername(),passwordEncoder.encode(user.getPassword()),true,true,true,true,AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
	}

	
	
}
