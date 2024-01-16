package com.school.sba.utility;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Setter
@Getter
public class ErrorStructre<T> {
	private int status;
	private String message;
	private T rootCause;

}
