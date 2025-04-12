package com.chatApp.chatApp.errors;

import org.springframework.http.HttpStatus;

public class ApiResponse<V> {
	private String message;
	private HttpStatus httpStatusCode;
	private V data;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public HttpStatus getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(HttpStatus httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	public V getData() {
		return data;
	}

	public void setData(V data) {
		this.data = data;
	}

	
}
