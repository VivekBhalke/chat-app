package com.chatApp.chatApp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chatApp.chatApp.dao.ChatDAO;
import com.chatApp.chatApp.dto.MessageEntityDTO;
import com.chatApp.chatApp.dto.mapper.MessageEntityDTOMapper;
import com.chatApp.chatApp.entity.MessageEntity;

@Service
public class ChatService {
	@Autowired
	private ChatDAO chatDAO;
	
	@Autowired
	private MessageEntityDTOMapper messageEntityDTOMapper;
	
	
	public void addChatAndMessage(long senderId , long receiverId , String message) throws Exception
	{
		chatDAO.addChatAndMessage(senderId, receiverId, message);
	}
	public List<MessageEntityDTO> getChatMessages(long senderId , long receiverId) throws Exception
	{
		List<MessageEntity> messageEntities = chatDAO.getChatMessages(senderId, receiverId) ;
		return messageEntities
				.stream()
				.map(messageEntityDTOMapper)
				.toList();
	}
}
