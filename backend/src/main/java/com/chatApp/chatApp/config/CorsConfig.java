package com.chatApp.chatApp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

	@Override
    public void addCorsMappings(CorsRegistry registry) {
        // Allow CORS for all endpoints
        registry.addMapping("/**")
                .allowedOrigins("https://chat-app-sigma-pink.vercel.app") // Replace with the allowed origin(s)
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Allowed methods
                .allowedHeaders("*") // Allowed headers
                .allowCredentials(true); // Allow credentials (cookies, etc.)
    }
	
}
