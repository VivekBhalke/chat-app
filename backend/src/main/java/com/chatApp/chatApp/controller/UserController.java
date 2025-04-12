package com.chatApp.chatApp.controller;
import java.util.List;

import org.hibernate.annotations.ParamDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chatApp.chatApp.dto.ChatEntityDTO;
import com.chatApp.chatApp.dto.MessageEntityDTO;
import com.chatApp.chatApp.dto.UserEntityDTO;
import com.chatApp.chatApp.errors.ApiException;
import com.chatApp.chatApp.errors.ApiResponse;
import com.chatApp.chatApp.middleware.CookieClass;
import com.chatApp.chatApp.middleware.JWTCLASS;
import com.chatApp.chatApp.services.ChatService;
import com.chatApp.chatApp.services.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ChatService chatService;
	
	@Autowired
	private JWTCLASS jwtClass;
	
	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<UserEntityDTO>> singup(@RequestBody UserEntityDTO userEntityDTO , HttpServletResponse servletResponse)
	{
		try {
			System.out.println("reached here");
			Long userId = userService.signup(userEntityDTO);
			userEntityDTO.setUserId(userId);
			userEntityDTO.setPassword(null);
			String token = jwtClass.getJWT(userId);
			Cookie cookie = new Cookie("jwt", token);
			cookie.setPath("/");                    
			cookie.setMaxAge(60 * 60);  // 1 hour  
			cookie.setHttpOnly(true);  // Prevent JavaScript access for security  
			cookie.setSecure(true);  // Required for HTTPS  
			//cookie.setDomain("chat-app-spring-boot-7.onrender.com");  // Set your domain  
			cookie.setAttribute("SameSite", "None");  // Needed for cross-site requests  
	        
	        servletResponse.addCookie(cookie);
			ApiResponse<UserEntityDTO> response = new ApiResponse<UserEntityDTO>();
			response.setHttpStatusCode(HttpStatus.OK);
			response.setMessage("USER CREATED SUCCESSFULLY");
			response.setData(userEntityDTO);
			return ResponseEntity.ok(response);
		}
		catch(Exception e)
		{
			if(e instanceof ApiException)
			{
				ApiException exception = (ApiException)e;
				return ResponseEntity.status(exception.getResponse().getHttpStatusCode()).body(exception.getResponse());
			}
		}
		System.out.println("hi from the backend");
		ApiResponse response = new ApiResponse();
		response.setMessage("INTERNAL SERVER ERROR");
		response.setData(null);
		response.setHttpStatusCode(HttpStatus.BAD_GATEWAY);
		return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
	}
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<UserEntityDTO>> login(@RequestBody UserEntityDTO userEntityDTO , HttpServletResponse servletResponse)
	{
		try {
			System.out.println("reached login controller");
			long userId = userService.login(userEntityDTO);
			System.out.println("this is the user Id :" + userId);
			String token = jwtClass.getJWT(userId);
			Cookie cookie = new Cookie("jwt", token);
			cookie.setPath("/");                    
			cookie.setMaxAge(60 * 60);  // 1 hour  
			cookie.setHttpOnly(true);  // Prevent JavaScript access for security  
			cookie.setSecure(true);  // Required for HTTPS  
			//cookie.setDomain("/");
			//cookie.setDomain("chat-app-spring-boot-7.onrender.com");  // Set your domain  
			cookie.setAttribute("SameSite", "None");  // Needed for cross-site requests              // 1 hour expiration
	        servletResponse.addCookie(cookie);
			ApiResponse<UserEntityDTO> response = new ApiResponse<>();
			response.setHttpStatusCode(HttpStatus.OK);
			response.setMessage("USER LOGGED IN  SUCCESSFULLY");
			response.setData(userEntityDTO);
			return ResponseEntity.ok(response);
		}
		catch(Exception e)
		{
			if(e instanceof ApiException)
			{
				ApiException exception = (ApiException)e;
				return ResponseEntity.status(exception.getResponse().getHttpStatusCode()).body(exception.getResponse());
			}
		}
		System.out.println("hi from the backend");
		ApiResponse response = new ApiResponse();
		response.setMessage("INTERNAL SERVER ERROR");
		response.setData(null);
		response.setHttpStatusCode(HttpStatus.BAD_GATEWAY);
		return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
	}
	@GetMapping("/searchUser")
	public ResponseEntity<ApiResponse<List<UserEntityDTO>>> findUser(@RequestParam String username , HttpServletRequest request)
	{
		try {
			String token = CookieClass.extractTokenFromCookies(request);
			Long userId = jwtClass.getUserId(token);
			List<UserEntityDTO> users = userService.findUser(username);
			ApiResponse response = new ApiResponse();
			response.setMessage("USER FOUND");
			response.setData(users);
			response.setHttpStatusCode(HttpStatus.ACCEPTED);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
		}catch(Exception e)
		{
			if(e instanceof ApiException)
			{
				ApiException exception = (ApiException)e;
				return ResponseEntity.status(exception.getResponse().getHttpStatusCode()).body(exception.getResponse());
			}
		}
		ApiResponse response = new ApiResponse();
		response.setMessage("INTERNAL SERVER ERROR");
		response.setData(null);
		response.setHttpStatusCode(HttpStatus.BAD_GATEWAY);
		return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
		
	}
	
	@GetMapping("/me")
	public ResponseEntity<ApiResponse<UserEntityDTO>> verifyCookie(HttpServletRequest request)
	{
		try {
			String token = CookieClass.extractTokenFromCookies(request);
			Long userId = jwtClass.getUserId(token);
			System.out.println("userId " + userId);
			UserEntityDTO userEntityDTO = userService.findUser(userId);
			ApiResponse response = new ApiResponse();
			response.setMessage("USER IS VERIFIED");
			response.setData(userEntityDTO);
			response.setHttpStatusCode(HttpStatus.ACCEPTED);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
		}catch(Exception e)
		{
			if(e instanceof ApiException)
			{
				ApiException exception = (ApiException)e;
				return ResponseEntity.status(exception.getResponse().getHttpStatusCode()).body(exception.getResponse());
			}
		}
		ApiResponse response = new ApiResponse();
		response.setMessage("INTERNAL SERVER ERROR");
		response.setData(null);
		response.setHttpStatusCode(HttpStatus.BAD_GATEWAY);
		return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
	}
	
	@GetMapping("/validateToken")
	public ResponseEntity<ApiResponse<String>> verifyCookieToken(HttpServletRequest request)
	{
		try {
			String token = CookieClass.extractTokenFromCookies(request);
			Long userId = jwtClass.getUserId(token);
			ApiResponse response = new ApiResponse();
			response.setMessage("USER IS VERIFIED");
			response.setData(userId.toString());
			response.setHttpStatusCode(HttpStatus.ACCEPTED);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
		}catch(Exception e)
		{
			if(e instanceof ApiException)
			{
				ApiException exception = (ApiException)e;
				return ResponseEntity.status(exception.getResponse().getHttpStatusCode()).body(exception.getResponse());
			}
		}
		ApiResponse response = new ApiResponse();
		response.setMessage("INTERNAL SERVER ERROR");
		response.setData(null);
		response.setHttpStatusCode(HttpStatus.BAD_GATEWAY);
		return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
	}
	@GetMapping("/getChats")
	public ResponseEntity<ApiResponse<List<ChatEntityDTO>>> getChats(HttpServletRequest request)
	{
		try {
			String token = CookieClass.extractTokenFromCookies(request);
			Long userId = jwtClass.getUserId(token);
			var chats = userService.getUserChats(userId);
			ApiResponse response = new ApiResponse();
			response.setMessage("THESE ARE THE CHATS");
			response.setData(chats);
			response.setHttpStatusCode(HttpStatus.ACCEPTED);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
		}catch(Exception e)
		{
			if(e instanceof ApiException)
			{
				ApiException exception = (ApiException)e;
				return ResponseEntity.status(exception.getResponse().getHttpStatusCode()).body(exception.getResponse());
			}
		}
		ApiResponse response = new ApiResponse();
		response.setMessage("INTERNAL SERVER ERROR");
		response.setData(null);
		response.setHttpStatusCode(HttpStatus.BAD_GATEWAY);
		return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
	}
	
	@GetMapping("/getMessages")
	public ResponseEntity<ApiResponse<List<MessageEntityDTO>>> getMessages(HttpServletRequest request , @RequestParam Long receiverId)
	{
		try {
			String token = CookieClass.extractTokenFromCookies(request);
			Long userId = jwtClass.getUserId(token);
			var messages = chatService.getChatMessages(userId, receiverId);
			ApiResponse response = new ApiResponse();
			response.setMessage("THESE ARE THE MESSAGES");
			response.setData(messages);
			response.setHttpStatusCode(HttpStatus.ACCEPTED);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
		}catch(Exception e)
		{
			if(e instanceof ApiException)
			{
				ApiException exception = (ApiException)e;
				return ResponseEntity.status(exception.getResponse().getHttpStatusCode()).body(exception.getResponse());
			}
		}
		ApiResponse response = new ApiResponse();
		response.setMessage("INTERNAL SERVER ERROR");
		response.setData(null);
		response.setHttpStatusCode(HttpStatus.BAD_GATEWAY);
		return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
	}
	
	
}
