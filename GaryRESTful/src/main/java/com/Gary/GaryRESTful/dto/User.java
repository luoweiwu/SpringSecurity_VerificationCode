package com.Gary.GaryRESTful.dto;

import com.fasterxml.jackson.annotation.JsonView;

public class User {

	//简单试图 只有一个username
	public interface UserSimpleView{};
	//复杂试图 有username 和   password 
	public interface UserDetailView extends UserSimpleView{};

	private String username;
	private String password;
	private String id;
	
	@JsonView(UserSimpleView.class)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@JsonView(UserSimpleView.class)
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	@JsonView(UserDetailView.class)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
