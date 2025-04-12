package com.chatApp.chatApp.middleware;

import org.apache.commons.validator.EmailValidator;
import org.apache.commons.validator.routines.RegexValidator;
import org.springframework.stereotype.Component;


public class EmailPasswordValidator {
	private static EmailValidator validator = null;
	public static boolean isValidEmail(String email)
	{
		if(validator == null)
		{
			validator = EmailValidator.getInstance();
		}
		return validator.isValid(email);
	}
	private static RegexValidator 	passwordValidator = null;
	public static boolean isValidPassword(String password)
	{
		if(passwordValidator ==null)
		{
			passwordValidator = new RegexValidator("^(?=.*\\d)(?=.*[\\W_].*[\\W_].*[\\W_]).{8,}$");
		}
		
		return passwordValidator.isValid(password);
	}
}
