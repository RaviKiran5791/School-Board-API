package com.school.sba.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UnAuthorisedUserException extends RuntimeException {
	
	String meaasge;

}
