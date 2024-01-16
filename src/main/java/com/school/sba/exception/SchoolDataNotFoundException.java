package com.school.sba.exception;

public class SchoolDataNotFoundException extends RuntimeException {
	
	String message;

	public SchoolDataNotFoundException(String message) {
		super(message);
		this.message = message;
	}
	

}
