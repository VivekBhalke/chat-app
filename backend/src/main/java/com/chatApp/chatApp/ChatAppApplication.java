package com.chatApp.chatApp;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

import com.chatApp.chatApp.middleware.EnvLoader;

@SpringBootApplication
public class ChatAppApplication 
{
	public static void main(String[] args) 
	{
//		new EnvLoader();
		SpringApplication.run(ChatAppApplication.class, args);
	}

}
