package com.chatApp.chatApp.dao;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.chatApp.chatApp.entity.ChatEntity;
import com.chatApp.chatApp.entity.MessageEntity;
import com.chatApp.chatApp.entity.UserEntity;
import com.chatApp.chatApp.errors.ApiException;
import com.chatApp.chatApp.errors.ApiResponse;

@Repository
public class ChatDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public void addChatAndMessage(long senderId , long receiverId , String message) throws Exception
	{
		Session session = null;
		Transaction tx = null;
		try
		{
			session = sessionFactory.openSession();
			tx = session.getTransaction();
			tx.begin();
			UserEntity sender = session.get(UserEntity.class, senderId);
			if(sender == null)
			{
				ApiResponse<String> res = new ApiResponse();
				res.setData(null);
				res.setHttpStatusCode(HttpStatus.BAD_REQUEST);
				res.setMessage("NO SUCH FIRST USER");
				ApiException ex = new ApiException();
				ex.setResponse(res);
				throw ex;
			}
			UserEntity receiver = session.get(UserEntity.class, receiverId);
			if(receiver == null)
			{
				ApiResponse<String> res = new ApiResponse();
				res.setData(null);
				res.setHttpStatusCode(HttpStatus.BAD_REQUEST);
				res.setMessage("NO SUCH SECOND USER");
				ApiException ex = new ApiException();
				ex.setResponse(res);
				throw ex;
			}
			String id = senderId + "_" + receiverId;
			String otherId = receiverId + "_" + senderId;
			ChatEntity newChat = null;
			ChatEntity chatFromDB = session.get(ChatEntity.class, id);
			ChatEntity chatFromDBOther = session.get(ChatEntity.class, otherId);
			if(chatFromDB == null && chatFromDBOther == null)
			{
				//chat does not exists so make a new chat
				newChat = new ChatEntity();
				newChat.setChatId(id);
				newChat.setSender(sender);
				newChat.setReceiver(receiver);
				chatFromDB = newChat;
				chatFromDBOther = newChat;
				session.persist(chatFromDB);
				MessageEntity messageEntity = new MessageEntity();
				messageEntity.setMessage(message);
				messageEntity.setChat(chatFromDB);
				messageEntity.setSender(sender);
				session.persist(messageEntity);
			}
			else if(chatFromDB != null )
			{
				MessageEntity messageEntity = new MessageEntity();
				messageEntity.setMessage(message);
				messageEntity.setChat(chatFromDB);
				session.persist(messageEntity);
				messageEntity.setSender(sender);
			}
			else {
				MessageEntity messageEntity = new MessageEntity();
				messageEntity.setMessage(message);
				messageEntity.setChat(chatFromDBOther);
				session.persist(messageEntity);
				messageEntity.setSender(sender);
			}
			tx.commit();
			
		}catch(Exception e)
		{
			System.out.println("line 78 in chatDAO");
			if(tx!=null && tx.isActive())
			{
				tx.rollback();
			}
			if(session!=null)
			{
				session.close();
			}
			throw e;
		}
	}
	
	public List<MessageEntity> getChatMessages(long senderId , long receiverId) throws Exception
	{
		Session session = null;
		Transaction tx = null;
		try
		{
			session = sessionFactory.openSession();
			tx = session.getTransaction();
			tx.begin();
			UserEntity firstUser = session.get(UserEntity.class, senderId);
			if(firstUser == null)
			{
				ApiResponse<String> res = new ApiResponse();
				res.setData(null);
				res.setHttpStatusCode(HttpStatus.BAD_REQUEST);
				res.setMessage("NO SUCH FIRST USER");
				ApiException ex = new ApiException();
				ex.setResponse(res);
				throw ex;
			}
			UserEntity secondUser = session.get(UserEntity.class, receiverId);
			if(secondUser == null)
			{
				ApiResponse<String> res = new ApiResponse();
				res.setData(null);
				res.setHttpStatusCode(HttpStatus.BAD_REQUEST);
				res.setMessage("NO SUCH SECOND USER");
				ApiException ex = new ApiException();
				ex.setResponse(res);
				throw ex;
			}
			String id = senderId + "_" + receiverId;
			String otherId = receiverId + "_" + senderId;
			ChatEntity newChat = null;
			ChatEntity chatFromDB = session.get(ChatEntity.class, id);
			ChatEntity chatFromDBOther = session.get(ChatEntity.class, otherId);
			if(chatFromDB == null && chatFromDBOther == null)
			{
				//chat does not exists so throw error
				ApiResponse<String> res = new ApiResponse();
				res.setData(null);
				res.setHttpStatusCode(HttpStatus.BAD_REQUEST);
				res.setMessage("NO SUCH CHAT PRESENT");
				ApiException ex = new ApiException();
				ex.setResponse(res);
				throw ex;
			}
			if(chatFromDB != null)
			{
				
				List<MessageEntity> messages = chatFromDB.getMessages();
				return messages;
			}
			
			
			List<MessageEntity> messages = chatFromDBOther.getMessages();
			
			return messages;
//			
			
		}catch(Exception e)
		{
			System.out.println("line 70 in chatDAO");
			System.out.println(e);
			if(tx!=null && tx.isActive())
			{
				tx.rollback();
			}
			if(session!=null)
			{
				session.close();
			}
			throw e;
		}
	}
	
}
