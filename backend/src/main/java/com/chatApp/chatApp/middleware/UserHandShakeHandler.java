package com.chatApp.chatApp.middleware;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;





@Component
public class UserHandShakeHandler extends DefaultHandshakeHandler {
	
	
	@Autowired
	private JWTCLASS jwtClass;
	
	
	@Override
	protected Principal determineUser(ServerHttpRequest request,
	                                 WebSocketHandler wsHandler,
	                                  Map<String, Object> attributes) 

	{
        try {
            // Convert ServerHttpRequest to HttpServletRequest
            if (request instanceof ServletServerHttpRequest) {
                HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
                System.out.println("REACHED FOR THE TOKEN OH YEAH");
                // Extract JWT token from cookies
                String token = CookieClass.extractTokenFromCookies(servletRequest);
                
                if (token == null) {
                    throw new HandshakeFailureException("No token found in cookies.");
                }

                // Extract userId from the token
                Long userId = jwtClass.getUserId(token);

                if (userId == null) {
                    throw new HandshakeFailureException("Invalid token: Unable to extract userId.");
                }
                
                UserPrincipal userPrincipal = new UserPrincipal();
                userPrincipal.setUserId(userId);

                // Return a Principal with userId
                return userPrincipal;
            }

            throw new HandshakeFailureException("Invalid WebSocket request.");
        } catch (Exception e) {
            throw new HandshakeFailureException("Handshake failed: " + e.getMessage(), e);
        }
    }
}
