package com.chatApp.chatApp.model;

import java.util.Date;

public class ChatMessage {
	private String nickname;
	private String content;
	private Date timestamp;
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	@Override
	public String toString() {
		return "ChatMessage [nickname=" + nickname + ", content=" + content + ", timestamp=" + timestamp + "]";
	}
	public ChatMessage() {}
}
