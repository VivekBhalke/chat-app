package com.chatApp.chatApp.controller;

import java.security.Principal;



import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.lang.*;
import com.chatApp.chatApp.services.ChatService;



class ChatMessage{
	private String senderUserId;
	private String senderUsername;
	private String message;
	
	public String getSenderUsername() {
		return senderUsername;
	}
	public void setSenderUsername(String senderUsername) {
		this.senderUsername = senderUsername;
	}
	public String getSenderUserId() {
		return senderUserId;
	}
	public void setSenderUserId(String senderUserId) {
		this.senderUserId = senderUserId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ChatMessage(String senderUserId, String message , String senderUsername) {
		super();
		this.senderUserId = senderUserId;
		this.message = message;
		this.senderUsername = senderUsername;
	}
	public ChatMessage() {}
	
}


@Controller
public class WebController {
	
	private final SimpMessagingTemplate messagingTemplate;
	
	public WebController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
	
	@Autowired
	private ChatService chatService;
	
	@MessageMapping("/chat") // when a client sends a message to /app/chat then it would come here
	@SendTo("/topic/messages")
	public String sendPrivateMessage(String message)
	{
		System.out.println("THIS IS THE MESSAGE : " + message);
		return message;
	}
	@MessageMapping("/chat/{recipientUsername}") // Receiving messages to send to specific user
    public void sendMessage(@Payload ChatMessage message,
                            @org.springframework.messaging.handler.annotation.DestinationVariable String recipientUsername,
                            Principal sender) throws Exception{
        
        String senderUsername = sender.getName(); // Get authenticated user from Principal
        System.out.println("User " + senderUsername  + " is sending a message to " + recipientUsername + message.getMessage());
        //senderUsername is the senderId
        //recipientUsername is the receiverId
        // Send the message to the recipient's personal queue
        try {
        	messagingTemplate.convertAndSendToUser(recipientUsername, "/queue/" + senderUsername +"/" + recipientUsername + "/messages", new ChatMessage(senderUsername , message.getMessage() , message.getSenderUsername()));
            messagingTemplate.convertAndSendToUser(recipientUsername , "/queue/" + recipientUsername + "/messages" , new ChatMessage(senderUsername , message.getMessage() , message.getSenderUsername()));
            chatService.addChatAndMessage(Long.parseLong(senderUsername), Long.parseLong(recipientUsername) , message.getMessage());
        } catch ( NumberFormatException e) {
            throw e;
        } catch(Exception e)
        {
        	System.out.println(e);
        }
        
	}
}

/*
 * 1. make UserHandShakeHandler extends DefaultHandShakeHandler
 * 			override the determine user method 
 * 			which returns the principal object 
 * 			in the principle object store the userId of the user from the token from the cookie
 * 			new UserPrincipal(randomId) //where random id is a string
 * 2. go to websocketconfig and to the registerStompEndpoints
 * 			registry.addEndpoint()
 * 						.setHandShakeHandler(new UserHandShakeHandler())
 * 						.withShockJS();
 * 3. go to the websocketcontroller
 * 			use the convertAndSendToUser method 
 * 			to send message to a particular user 
 * 			
 * 
 * */
