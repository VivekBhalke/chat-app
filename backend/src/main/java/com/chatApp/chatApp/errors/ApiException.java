package com.chatApp.chatApp.errors;

import org.springframework.http.HttpStatus;


public class ApiException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private ApiResponse response;
	
	public ApiResponse getResponse() {
		return response;
	}
	public void setResponse(ApiResponse response) {
		this.response = response;
	}
	
	
	
}
