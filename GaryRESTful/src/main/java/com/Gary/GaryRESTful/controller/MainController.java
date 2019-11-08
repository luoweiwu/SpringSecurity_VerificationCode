package com.Gary.GaryRESTful.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;



//这是SpringBoot测试类

@RunWith(SpringRunner.class)
@SpringBootTest
public class MainController {

		@Autowired
		private WebApplicationContext webApplicationContext;
			
		//SpringMV单元测试独立测试类
		private MockMvc mockMvc;
		
		
		@Before
		public void before()
		{
			//创建独立测试类
			mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		}
		
		
		//@Test
		//查询user
		public void test() throws Exception
		{
			//发起一个Get请求
			String str = mockMvc.perform(MockMvcRequestBuilders.get("/user")
					.param("username", "Gary")
						//json的形式发送一个请求
						.contentType(MediaType.APPLICATION_JSON_UTF8))
						//期望服务器返回什么(期望返回的状态码为200)
						.andExpect(MockMvcResultMatchers.status().isOk())
						//期望服务器返回json中的数组长度为3
						.andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
						.andReturn().getResponse().getContentAsString();
			
			System.out.println("查询简单试图"+str);
		}
		
		//@Test
		public void getInfo() throws Exception
		{
			//发起一个get请求，查看用户详情
			String str = mockMvc.perform(MockMvcRequestBuilders.get("/user/1")
					.contentType(MediaType.APPLICATION_JSON_UTF8))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.jsonPath("$.username").value("Gary"))
					.andReturn().getResponse().getContentAsString();
			
			System.out.println("查询复杂试图"+str);
					
		}
		
		//添加用户
		//@Test
		public void addUser() throws Exception
		{
			mockMvc.perform(MockMvcRequestBuilders.post("/user")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content("{\"username\":\"Gary\"}"))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"));
			
			
		}
		
		//修改用户
		//@Test
		public void updataUser() throws Exception
		{
			mockMvc.perform(MockMvcRequestBuilders.put("/user/1")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content("{\"username\":\"Garyary\",\"id\":\"1\"}"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"));
			
		}
		
		
		//删除用户
		@Test
		public void deleteUser() throws Exception
		{
			mockMvc.perform(MockMvcRequestBuilders.delete("/user/1")
					.contentType(MediaType.APPLICATION_JSON_UTF8))
					.andExpect(MockMvcResultMatchers.status().isOk());
		}
}
