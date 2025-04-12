package com.chatApp.chatApp.middleware;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class HashedPassword {
	@Autowired
	private  PasswordEncoder passwordEncoder;
	public  String hashPassword(String password)
	{
		return passwordEncoder.encode(password);
	}
	public  Boolean verify(String password , String hashPassword)
	{
		return passwordEncoder.matches(password , hashPassword);
	}
	private HashedPassword() {}
}

