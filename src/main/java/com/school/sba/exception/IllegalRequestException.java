package com.school.sba.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class IllegalRequestException extends RuntimeException {
	
	private String meaasge;

}
