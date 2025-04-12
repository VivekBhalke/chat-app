package com.chatApp.chatApp.dto.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.chatApp.chatApp.dto.UserEntityDTO;
import com.chatApp.chatApp.entity.UserEntity;

@Component
public class UserEntityDTOMapper implements Function<UserEntity , UserEntityDTO> {

	@Override
	public UserEntityDTO apply(UserEntity t) {
		// TODO Auto-generated method stub
		UserEntityDTO userEntityDTO = new UserEntityDTO();
		userEntityDTO.setEmail(t.getEmail());
		userEntityDTO.setUsername(t.getUsername());
		userEntityDTO.setUserId(t.getUserId());
		return userEntityDTO;
	}

}
