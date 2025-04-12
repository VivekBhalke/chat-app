package com.chatApp.chatApp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.chatApp.chatApp.middleware.UserHandShakeHandler;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{
	
	@Autowired
	private UserHandShakeHandler userHandShakeHandler;
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config)
	{
		config.enableSimpleBroker("/topic", "/queue"); // Enable simple broker for messaging
        config.setApplicationDestinationPrefixes("/app"); // Prefix for messages
        config.setUserDestinationPrefix("/user");
	}
	
	@Override
	public  void registerStompEndpoints(StompEndpointRegistry registry )
	{
		registry
			.addEndpoint("/ws")
			.setAllowedOriginPatterns("*")
			.setHandshakeHandler(userHandShakeHandler);
//    		.withSockJS();
			
	}
}
