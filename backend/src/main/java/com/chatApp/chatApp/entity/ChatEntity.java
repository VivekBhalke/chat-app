package com.chatApp.chatApp.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class ChatEntity {
	@Id
	@Column(name = "chat_id")
	private String chatId; //senderUserId_ReceiverId
	
	@ManyToOne
	@JoinColumn(name="sender" , nullable = false)
	private UserEntity sender; //sender	
	
	@ManyToOne
	@JoinColumn(name="receiver" , nullable = false)
	private UserEntity receiver; //receiver
	
	@OneToMany(mappedBy = "chat" , fetch=FetchType.LAZY)
	private List<MessageEntity> messages;

	public String getChatId() {
		return chatId;
	}

	public void setChatId(String chatId) {
		this.chatId = chatId;
	}

	public UserEntity getSender() {
		return sender;
	}

	public void setSender(UserEntity sender) {
		this.sender = sender;
	}

	public UserEntity getReceiver() {
		return receiver;
	}

	public void setReceiver(UserEntity receiver) {
		this.receiver = receiver;
	}

	public List<MessageEntity> getMessages() {
		return messages;
	}

	public void setMessages(List<MessageEntity> messages) {
		this.messages = messages;
	}

	

	
	
	
 
}
