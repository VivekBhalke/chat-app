package com.chatApp.chatApp.middleware;

import java.security.Principal;

public class UserPrincipal implements Principal{
	
	private Long userId;
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return userId.toString();
	}

}
