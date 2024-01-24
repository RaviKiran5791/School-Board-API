package com.school.sba.service;

import org.springframework.http.ResponseEntity;

import com.school.sba.requestdto.UserRequest;
import com.school.sba.responsedto.UserResponse;
import com.school.sba.utility.ResponseStructure;

import jakarta.validation.Valid;

public interface UserService {
	
	public ResponseEntity<ResponseStructure<UserResponse>> registerAdmin(UserRequest userRequest);
	public ResponseEntity<ResponseStructure<UserResponse>> addOtherUser(UserRequest userRequest);
	public ResponseEntity<ResponseStructure<UserResponse>> findUserById(int userId);
	public ResponseEntity<ResponseStructure<UserResponse>> deleteUserById(int userId);
	public ResponseEntity<ResponseStructure<UserResponse>> assignUsersToAcademicProgram(int programId, int userId);
	public ResponseEntity<ResponseStructure<UserResponse>> addSubjectToTeacher(int subjectId, int userId);
	
	

}
