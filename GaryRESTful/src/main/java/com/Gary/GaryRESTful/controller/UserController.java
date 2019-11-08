package com.Gary.GaryRESTful.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Gary.GaryRESTful.dto.User;
import com.fasterxml.jackson.annotation.JsonView;

//表示这个Controller提供R二十天API
@RestController
@RequestMapping("/user")
public class UserController {

	
	//@RequestMapping(value="/user",method = RequestMethod.GET)
	/*
	 * default value 	默认
	 * name 			请求的名字
	 * required			是否是必须的,true
	 * value			别名
	 * 
	 * */
	@GetMapping
	@JsonView(User.UserSimpleView.class)
	public List<User> query(@RequestParam(name="username",required=false) String username)
	{
		System.out.println(username);
		//满足期望服务器返回json中的数组长度为3
		List<User> list = new ArrayList<>();
		list.add(new User());
		list.add(new User());
		list.add(new User());
		return list;
		
	}
	

	//@RequestMapping(value="/user/{id}",method= RequestMethod.GET)
	//将@PathVariable路径中的片段映射到java代码中	
	@GetMapping("/{id}")
	@JsonView(User.UserDetailView.class)
	public User getInfo(@PathVariable String id)
	{
		User user = new User();
		user.setUsername("Gary");
		return user;
	}
	
	//添加用户
	//@RequestMapping(value="/user",method= RequestMethod.POST)
	@PostMapping
	public User addUser(@RequestBody User user)
	{
		//传输json格式的时候，一定要记得加上@RequestBody注解
		//输出 null
		System.out.println(user.getPassword());
		//输出 Gary
		System.out.println(user.getUsername());
		
		user.setId("1");
		return user;
	}
	
	//修改用户资料
	//@RequestMapping(value="/user/{id}",method = RequestMethod.PUT)
	@PutMapping("/{id}")
	public User updataUser(@RequestBody User user)
	{
		System.out.println(user.getId());
		System.out.println(user.getUsername());
		System.out.println(user.getPassword());
		
		return user;
	}
	
	
	//删除
	//@RequestMapping(value="/user/{id}",method = RequestMethod.DELETE)
	@DeleteMapping("/{id}")
	public User deleteUser(@PathVariable String id)
	{
		//输出删除的用户    可以查看JUnit中的状态吗
		System.out.println(id);
		return null;
	}
}
