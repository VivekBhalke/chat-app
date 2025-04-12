package com.chatApp.chatApp.dto.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.chatApp.chatApp.dto.ChatEntityDTO;
import com.chatApp.chatApp.entity.ChatEntity;

@Component
public class ChatEntityDTOMapper  implements Function<ChatEntity, ChatEntityDTO>{

	@Override
	public ChatEntityDTO apply(ChatEntity t) {
		ChatEntityDTO chatEntityDTO = new ChatEntityDTO();
		chatEntityDTO.setChatId(t.getChatId());
		
		return null;
	}
}
