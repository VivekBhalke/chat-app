package com.chatApp.chatApp.dto;

public class UserEntityDTO {
	private String email;
	private String password;
	private String username;
	private long userId;
	public void setUserId(long id)
	{
		userId = id;
	}
	public long getUserId()
	{
		return userId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
}


