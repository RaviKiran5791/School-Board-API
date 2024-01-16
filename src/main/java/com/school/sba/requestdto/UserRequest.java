package com.school.sba.requestdto;

import com.school.sba.enums.USERROLE;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRequest {

	@NotEmpty(message = "UserName is Required!!")
	@Size(min = 3, max=20,message = "User Name must be between 3 and 20 characters")
	@Pattern(regexp = "^[a-zA-Z0-9]+$",message = "User Name can only contain alphabets and numbers no special symbols")
	private String userName;
	
	@NotEmpty(message = "UserName is Required!!")
	@Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
	message = "Password must"+ " contain at least one letter, one number, one special character")
	private String password;
	
	@NotEmpty(message = "firstname cannot be empty")
	@Pattern(regexp = "^[A-Z][a-z]*(\\s[A-Z][a-z]*)?$", message = "Username should follow initcap format")
	private String firstName;
	
	@NotEmpty(message = "lastname cannot be empty")
	@Pattern(regexp = "^[A-Z][a-z]*(\\s[A-Z][a-z]*)?$", message = "Username should follow initcap format")
	private String lastName;
	@Min(value = 6000000000l, message = " phone number must be valid")
	@Max(value = 9999999999l, message = " phone number must be valid")
	private long contactNo;
	
	@NotEmpty(message = "email cannot be not null & not blank")
	@Email(regexp = "[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\\.[a-z]{2,}", message = "invalid email")
	private String email;
	
	@NotEmpty(message = "User role cannot be empty")
	@Pattern(regexp = "^(ADMIN|TEACHER|STUDENT)$",message = "Plese mention ADMIN or STUDENT or TEACHER")
	private String userRole;

}
