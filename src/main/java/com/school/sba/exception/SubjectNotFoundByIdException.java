package com.school.sba.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class SubjectNotFoundByIdException extends RuntimeException{
	private String message;

}
