package com.chatApp.chatApp.middleware;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvLoader {
	public EnvLoader()
	{
		Dotenv dotenv = Dotenv.load(); // Load .env file from project root
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
	}
}
