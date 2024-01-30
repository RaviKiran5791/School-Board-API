package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.enums.USERROLE;
import com.school.sba.requestdto.UserRequest;
import com.school.sba.responsedto.UserResponse;
import com.school.sba.utility.ResponseStructure;

public interface UserService {
	
	public ResponseEntity<ResponseStructure<UserResponse>> registerAdmin(UserRequest userRequest);
	public ResponseEntity<ResponseStructure<UserResponse>> addOtherUser(UserRequest userRequest);
	public ResponseEntity<ResponseStructure<UserResponse>> findUserById(int userId);
	public ResponseEntity<ResponseStructure<UserResponse>> deleteUserById(int userId);
	public ResponseEntity<ResponseStructure<UserResponse>> assignUsersToAcademicProgram(int programId, int userId);
	public ResponseEntity<ResponseStructure<UserResponse>> addSubjectToTeacher(int subjectId, int userId);
	public ResponseEntity<ResponseStructure<List<UserResponse>>> findUserByRoleInProgram(int programId, USERROLE userRole);
	
	

}
