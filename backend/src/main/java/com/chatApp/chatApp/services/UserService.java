package com.chatApp.chatApp.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.chatApp.chatApp.dao.UserDAO;
import com.chatApp.chatApp.dto.ChatEntityDTO;
import com.chatApp.chatApp.dto.UserEntityDTO;
import com.chatApp.chatApp.dto.mapper.UserEntityDTOMapper;
import com.chatApp.chatApp.entity.UserEntity;
import com.chatApp.chatApp.errors.ApiException;
import com.chatApp.chatApp.errors.ApiResponse;
import com.chatApp.chatApp.middleware.EmailPasswordValidator;

@Service
public class UserService {
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private UserEntityDTOMapper userEntityDTOMapper;

	public long signup(UserEntityDTO userEntityDTO) throws Exception
	{
		try {
			if(!EmailPasswordValidator.isValidEmail(userEntityDTO.getEmail())) {
				ApiException exception = new ApiException();
				ApiResponse<String> response = new ApiResponse<String>();
				response.setHttpStatusCode(HttpStatus.BAD_REQUEST);
				response.setData(null);
				response.setMessage("INVALID EMAIL FORMAT");
				exception.setResponse(response);
				throw exception;
			}
			if(!EmailPasswordValidator.isValidPassword(userEntityDTO.getPassword()))
			{
				ApiException exception = new ApiException();
				ApiResponse<String> response = new ApiResponse<String>();
				response.setHttpStatusCode(HttpStatus.BAD_REQUEST);
				response.setData(null);
				response.setMessage("INVALID PASSWORD FORMAT");
				exception.setResponse(response);
				throw exception;
			}
			System.out.println("the email and password are correct");
			long userId = userDAO.signup(userEntityDTO);
			return userId;
		}catch(Exception e)
		{
			throw e;
		}
	}
	public long login(UserEntityDTO userEntityDTO) throws Exception {
		try {
			if(!EmailPasswordValidator.isValidEmail(userEntityDTO.getEmail())) {
				ApiException exception = new ApiException();
				ApiResponse<String> response = new ApiResponse<String>();
				response.setHttpStatusCode(HttpStatus.BAD_REQUEST);
				response.setData(null);
				response.setMessage("INVALID EMAIL FORMAT");
				exception.setResponse(response);
				throw exception;
			}
			if(!EmailPasswordValidator.isValidPassword(userEntityDTO.getPassword()))
			{
				ApiException exception = new ApiException();
				ApiResponse<String> response = new ApiResponse<String>();
				response.setHttpStatusCode(HttpStatus.BAD_REQUEST);
				response.setData(null);
				response.setMessage("INVALID PASSWORD FORMAT");
				exception.setResponse(response);
				throw exception;
			}
			System.out.println("the email and password are in correct format");
			return userDAO.login(userEntityDTO);
		}catch(Exception e)
		{
			throw e;
		}
	}
	
	public List<UserEntityDTO> findUser(String username) throws Exception
	{
		try {
			List<UserEntity> users = userDAO.findUser(username);
			 return users.stream()
                     .map(userEntityDTOMapper) // ✅ Use .apply()
                     .collect(Collectors.toList());
		}catch(Exception e)
		{
			throw e;
		}
	}
	
	public UserEntityDTO findUser(Long userId) throws Exception
	{
		try {
			UserEntity user = userDAO.findUser(userId);
			return userEntityDTOMapper.apply(user);
		}catch(Exception e)
		{
			throw e;
		}
	}
	public List<ChatEntityDTO> getUserChats(long userId)
	{
		try {
			return userDAO.getUserChats(userId);
		}catch(Exception e)
		{
			throw e;
		}
	}
}
