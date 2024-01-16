package com.school.sba.exception;

public class SchoolNotFoundByIdException extends RuntimeException {
	
	String message;

	public SchoolNotFoundByIdException(String message) {
		super(message);
		this.message = message;
	}
	

}
