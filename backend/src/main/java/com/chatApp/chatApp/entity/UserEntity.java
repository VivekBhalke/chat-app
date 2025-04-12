package com.chatApp.chatApp.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class UserEntity {
	@Id
	@Column(name="user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userId;
	
	@Column(nullable=false , unique=true)
	private String email;
	
	@Column(nullable=false)
	private String password;
	
	@Column(nullable=false , unique=true)
	private String username;
	
	@OneToMany(mappedBy="sender" , fetch=FetchType.LAZY)
	private List<ChatEntity> chatsAsSender;
	
	@OneToMany(mappedBy="receiver" , fetch=FetchType.LAZY)
	private List<ChatEntity> chatsAsReceiver;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<ChatEntity> getChatsAsSender() {
		return chatsAsSender;
	}

	public void setChatsAsSender(List<ChatEntity> chatsAsSender) {
		this.chatsAsSender = chatsAsSender;
	}

	public List<ChatEntity> getChatsAsReceiver() {
		return chatsAsReceiver;
	}

	public void setChatsAsReceiver(List<ChatEntity> chatsAsReceiver) {
		this.chatsAsReceiver = chatsAsReceiver;
	}
	
}
