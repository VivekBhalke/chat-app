package com.chatApp.chatApp.middleware;

import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.chatApp.chatApp.errors.ApiException;
import com.chatApp.chatApp.errors.ApiResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


@Component
@Scope("prototype")
public class JWTCLASS {
	private final String secretKey = "9fP/Amc2Q1D97H1sZ8tre7EaVM++M4rM39OtpC8x/uA=";
	private final byte[] secret = java.util.Base64.getDecoder().decode(secretKey);
    public String getJWT(long  l) throws ApiException
    {
    	try {
    		String subject = null;
        	subject = Long.toString(l);
        	String jwt = Jwts.builder()
        						.setSubject(subject)
        						.setIssuer("Vivek Bhalke")
        						.setIssuedAt(new Date())
        						.setExpiration(new Date(System.currentTimeMillis() + (3600000 * 24)))
        						.claim("role" ,  "admin")
        						.signWith(Keys.hmacShaKeyFor(secret))
        						.compact();
        	return jwt;
    	}catch(RuntimeException e)
    	{
    		System.out.println(e.toString());
    		ApiResponse<String> response = new ApiResponse<String>();
			response.setHttpStatusCode(HttpStatus.BAD_GATEWAY);
			response.setData(null);
			response.setMessage("CANNOT CONVERT USER ID TO TOKEN");
			ApiException exception = new ApiException();
			exception.setResponse(response);
			throw exception;
    	}
    }
    
    public Long getUserId(String token) throws ApiException
    {
    	try {
    		Jws<Claims> result = Jwts.parser()
    				.setSigningKey(secret)
    				.build()
    				.parseClaimsJws(token);
    	Long userId = Long.parseLong(result.getBody().getSubject());
    	return userId; 
    	}catch(JwtException e)
    	{
    		ApiResponse<String> response = new ApiResponse<String>();
			response.setHttpStatusCode(HttpStatus.BAD_GATEWAY);
			response.setData(null);
			response.setMessage("INVALID TOKEN");
			ApiException exception = new ApiException();
			exception.setResponse(response);
			throw exception;
    	}
    }
}
