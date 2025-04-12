package com.chatApp.chatApp.middleware;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.chatApp.chatApp.errors.ApiException;
import com.chatApp.chatApp.errors.ApiResponse;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class CookieClass {
	public static String extractTokenFromCookies(HttpServletRequest request) throws ApiException {
        // Logic to extract JWT from cookies
		System.out.println("reached here for the token in the cookie class");
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        if(cookies == null)
        {
        	ApiException exception = new ApiException();
            ApiResponse response = new ApiResponse();
    		response.setMessage("NO COOKIES");
    		response.setData(null);
    		response.setHttpStatusCode(HttpStatus.BAD_REQUEST);
    		exception.setResponse(response);
    		throw exception;
        }
        ApiException exception = new ApiException();
        ApiResponse response = new ApiResponse();
		response.setMessage("NO COOKIE WITH PROPER VALUE");
		response.setData(null);
		response.setHttpStatusCode(HttpStatus.BAD_REQUEST);
		exception.setResponse(response);
		throw exception;
        
		
    }
}
