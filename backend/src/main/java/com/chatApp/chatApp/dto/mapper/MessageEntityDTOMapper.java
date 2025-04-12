package com.chatApp.chatApp.dto.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.chatApp.chatApp.dto.MessageEntityDTO;
import com.chatApp.chatApp.entity.MessageEntity;

@Component
public class MessageEntityDTOMapper implements Function<MessageEntity , MessageEntityDTO> {

	@Override
	public MessageEntityDTO apply(MessageEntity t) {
		var messageEntityDTO = new MessageEntityDTO();
		messageEntityDTO.setSenderId(t.getSender().getUserId());
		messageEntityDTO.setMessage(t.getMessage());
		messageEntityDTO.setCreatedAt(t.getCreatedAt());
		return messageEntityDTO;
	}

}
