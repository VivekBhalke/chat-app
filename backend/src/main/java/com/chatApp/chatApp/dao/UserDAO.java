package com.chatApp.chatApp.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.chatApp.chatApp.dto.ChatEntityDTO;
import com.chatApp.chatApp.dto.UserEntityDTO;
import com.chatApp.chatApp.entity.ChatEntity;
import com.chatApp.chatApp.entity.UserEntity;
import com.chatApp.chatApp.errors.ApiException;
import com.chatApp.chatApp.errors.ApiResponse;
import com.chatApp.chatApp.middleware.HashedPassword;


@Repository
public class UserDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private HashedPassword hashPassword;
	
	public long signup(UserEntityDTO userEntityDTO) throws Exception
	{
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			UserEntity userEntity = new UserEntity();
			userEntity.setEmail(userEntityDTO.getEmail());
			userEntity.setUsername(userEntityDTO.getUsername());
			String hasedPassword = hashPassword.hashPassword(userEntityDTO.getPassword());
			userEntity.setPassword(hasedPassword);
			Query<UserEntity> q = session.createQuery("SELECT u  FROM UserEntity u "
					+ "WHERE u.email=:EMAIL or u.username=:USERNAME");
			q.setParameter("EMAIL", userEntity.getEmail());
			q.setParameter("USERNAME", userEntity.getUsername());
			List<UserEntity> listOfUsers = q.getResultList();
			if(listOfUsers.size() != 0)
			{
				ApiResponse<String> response = new ApiResponse<String>();
				response.setHttpStatusCode(HttpStatus.BAD_GATEWAY);
				response.setData(null);
				response.setMessage("THE USER ALREADY EXISTS");
				ApiException exception = new ApiException();
				exception.setResponse(response);
				throw exception;
			}
			System.out.println("REACHED HERE and saved the user");
			session.persist(userEntity);
			tx.commit();
			long userId = q.getSingleResult().getUserId();
			System.out.println("user ID after signing in : " + userId);
			return userId;
		}catch(Exception e)
		{
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
	
	public long login(UserEntityDTO userEntityDTO) throws Exception{
			Session session = null;
			Transaction tx = null;
			try {
				session = sessionFactory.openSession();
				tx = session.beginTransaction();
				String  hashedPassword = hashPassword.hashPassword(userEntityDTO.getPassword());
				Query<UserEntity> q = session.createQuery("SELECT u  FROM UserEntity u "
						+ "WHERE u.email=:EMAIL or u.password=:PASSWORD");
				q.setParameter("EMAIL", userEntityDTO.getEmail());
				q.setParameter("PASSWORD", hashedPassword);
				List<UserEntity> listOfUsers = q.getResultList();
				if(listOfUsers.size() == 0)
				{
					ApiResponse<String> response = new ApiResponse<String>();
					response.setHttpStatusCode(HttpStatus.BAD_GATEWAY);
					response.setData(null);
					response.setMessage("INCORRECT EMAIL OR PASSWORD");
					ApiException exception = new ApiException();
					exception.setResponse(response);
					throw exception;
				}
				else if(listOfUsers.size()>  1) {
					ApiResponse<String> response = new ApiResponse<String>();
					response.setHttpStatusCode(HttpStatus.BAD_GATEWAY);
					response.setData(null);
					response.setMessage("MORE THAN ONE USER OF THIS EMAIL");
					ApiException exception = new ApiException();
					exception.setResponse(response);
					throw exception;
				}
				System.out.println("GOT THE AUTHERIZED USER");
				UserEntity user = listOfUsers.get(0);
				userEntityDTO.setUserId(user.getUserId());
				userEntityDTO.setPassword(null);
				userEntityDTO.setUsername(user.getUsername());
				return user.getUserId();
				
		}catch(Exception e)
		{
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
	
	public List<UserEntity> findUser(String username) throws Exception
	{
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			Query<UserEntity> q = session.createQuery("SELECT u FROM UserEntity u WHERE u.username LIKE :USERNAME", UserEntity.class);
			q.setParameter("USERNAME", "%" + username + "%"); // Adjust wildcard placement as needed
			List<UserEntity> users = q.getResultList();
			if(users == null || users.size() ==0)
			{
				ApiResponse<String> response = new ApiResponse<String>();
				response.setHttpStatusCode(HttpStatus.BAD_GATEWAY);
				response.setData(null);
				response.setMessage("NO SUCH USER");
				ApiException exception = new ApiException();
				exception.setResponse(response);
				throw exception;
			}
			return users;
		}catch(Exception e)
		{
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
	public UserEntity findUser(Long userId) throws Exception 
	{
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			UserEntity user = session.get(UserEntity.class, userId);
			if(user == null)
			{
				ApiResponse<String> response = new ApiResponse<String>();
				response.setHttpStatusCode(HttpStatus.BAD_GATEWAY);
				response.setData(null);
				response.setMessage("NO SUCH USER");
				ApiException exception = new ApiException();
				exception.setResponse(response);
				throw exception;
			}
			return user;
		}catch(Exception e)
		{
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
	public List<ChatEntityDTO> getUserChats(long userId)
	{
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			UserEntity user = session.get(UserEntity.class, userId);
			List<ChatEntity> chatsAsSender = user.getChatsAsSender();
			List<ChatEntity> chatsAsReceiver = user.getChatsAsReceiver();
			List<ChatEntityDTO> chats = new ArrayList<>();
			for(var chatEntity : chatsAsSender)
			{
				//userId = senderId
				var chatDTO = new ChatEntityDTO();
				chatDTO.setChatId(chatEntity.getChatId());
				chatDTO.setUserId(userId);
				chatDTO.setReceiverId(chatEntity.getReceiver().getUserId());
				chatDTO.setReceiverUsername(chatEntity.getReceiver().getUsername());
				chats.add(chatDTO);
			}
			for(var chatEntity : chatsAsReceiver)
			{
				//userId = senderId
				var chatDTO = new ChatEntityDTO();
				chatDTO.setChatId(chatEntity.getChatId());
				chatDTO.setUserId(userId);
				chatDTO.setReceiverId(chatEntity.getSender().getUserId());
				chatDTO.setReceiverUsername(chatEntity.getSender().getUsername());
				chats.add(chatDTO);
			}
			return chats;
		}catch(Exception e)
		{
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
