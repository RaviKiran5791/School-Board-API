
package com.school.sba.exception;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {
	
	
	private ResponseEntity<Object> structure(HttpStatus status,String message,Object rootCause)
	{
		return new ResponseEntity<Object>(Map.of(
				"statua", status.value(),
				"message" ,message,
				"rootCause",rootCause),status);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request)
	{
		List<ObjectError> allErrors = ex.getAllErrors();
		
		Map<String, String> errors=new HashMap<>();
		
		allErrors.forEach(error->{
			FieldError fieldError=(FieldError)error;
			
			errors.put(fieldError.getField(), fieldError.getDefaultMessage());
		});
		
		return structure(HttpStatus.BAD_REQUEST, "Failed To save the date", errors);
	}
	
	@ExceptionHandler(SchoolNotFoundByIdException.class)
	public ResponseEntity<Object> schoolNotFoundById(SchoolNotFoundByIdException e)
	{
		return  structure(HttpStatus.NOT_FOUND, e.getMessage(), "School Data not present");
	}
	
	@ExceptionHandler(SchoolDataNotFoundException.class)
	public ResponseEntity<Object> schoolsNotFound(SchoolDataNotFoundException e)
	{
		return structure(HttpStatus.NOT_FOUND, e.getMessage(), "School list not present");
	}
	
	@ExceptionHandler(UserNotFoundByIdException.class)
	public ResponseEntity<Object> userNotFoundById(UserNotFoundByIdException e)
	{
		return structure(HttpStatus.NOT_FOUND, e.getMessage(), "User  not present");
	}
	@ExceptionHandler(UnAuthorisedUserException.class)
	public ResponseEntity<Object> unAuthorised(UnAuthorisedUserException e)
	{
		return structure(HttpStatus.BAD_REQUEST, e.getMessage(), "UnAuthorised..!!!");
	}
	
	@ExceptionHandler(DataAlreadyExistException.class)
	public ResponseEntity<Object> dataAlreadyExists(DataAlreadyExistException e)
	{
		return structure(HttpStatus.BAD_REQUEST, e.getMessage(), "Data Already Exist..!!!");
	}
	@ExceptionHandler(ScheduleNotFoundByIdException.class)
	public ResponseEntity<Object> scheduleNotFoundById(ScheduleNotFoundByIdException e)
	{
		return  structure(HttpStatus.NOT_FOUND, e.getMessage(), "Schedule Data not present for given id");
	}
	
	@ExceptionHandler(ScheduleNotFoundException.class)
	public ResponseEntity<Object> scheduleNotFound(ScheduleNotFoundException e)
	{
		return structure(HttpStatus.NOT_FOUND, e.getMessage(), "Schedule list not present");
	}
	@ExceptionHandler(DataNotExistException.class)
	public ResponseEntity<Object> dataNotExists(DataNotExistException e)
	{
		return structure(HttpStatus.NOT_FOUND, e.getMessage(), "Data  not present");
	}
	@ExceptionHandler(AdminCannotBeAssignToAcademicProgramException.class)
	public ResponseEntity<Object> adminCannotBeAssigned(AdminCannotBeAssignToAcademicProgramException e)
	{
		return structure(HttpStatus.NOT_FOUND, e.getMessage(), "Admin cannot be assigned to academic program");
	}
	@ExceptionHandler(ClassHourNotFoundByIdException.class)
	public ResponseEntity<Object> classHourNotFoundById(ClassHourNotFoundByIdException e)
	{
		return structure(HttpStatus.NOT_FOUND, e.getMessage(), "classhour  not present for given id");
	}
	@ExceptionHandler(IllegalRequestException.class)
	public ResponseEntity<Object> illegalRequest(IllegalRequestException e)
	{
		return structure(HttpStatus.NOT_FOUND, e.getMessage(), "Illegal Request ");
	}
	@ExceptionHandler(InvalidUserRoleException.class)
	public ResponseEntity<Object> invalidUserRole(InvalidUserRoleException e)
	{
		return structure(HttpStatus.NOT_FOUND, e.getMessage(), "Invalid User Role");
	}
	@ExceptionHandler(ProgramNotFoundByIdException.class)
	public ResponseEntity<Object> programNotFoundById(ProgramNotFoundByIdException e)
	{
		return structure(HttpStatus.NOT_FOUND, e.getMessage(), "Program not present for given id");
	}
	@ExceptionHandler(SubjectNotFoundByIdException.class)
	public ResponseEntity<Object> subjectNotFoundById(SubjectNotFoundByIdException e)
	{
		return structure(HttpStatus.NOT_FOUND, e.getMessage(), "Subject not present for given id");
	}
	

	
	
	
	
	

}
