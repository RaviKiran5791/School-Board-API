package com.school.sba.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.Subject;
import com.school.sba.entity.User;
import com.school.sba.enums.USERROLE;
import com.school.sba.exception.AdminCannotBeAssignToAcademicProgramException;
import com.school.sba.exception.DataNotExistException;
import com.school.sba.exception.ProgramNotFoundByIdException;
import com.school.sba.exception.UnAuthorisedUserException;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.repositary.AcademicProgramRepositary;
import com.school.sba.repositary.SubjectRepositary;
import com.school.sba.repositary.UserRepositary;
import com.school.sba.requestdto.UserRequest;
import com.school.sba.responsedto.UserResponse;
import com.school.sba.service.UserService;
import com.school.sba.utility.ResponseStructure;
@Service
public class UserServiceImpl implements UserService {


	@Autowired
	private UserRepositary userRepo;
	
	@Autowired
	private AcademicProgramRepositary programRepo;
	
	@Autowired
	private SubjectRepositary subjectRepo;
	
	@Autowired
	private ResponseStructure<UserResponse> structure;

	private User mapToUser(UserRequest userRequest)
	{
		return new User().builder()
				.userName(userRequest.getUserName())
				.firstName(userRequest.getFirstName())
				.lastName(userRequest.getLastName())
				.email(userRequest.getEmail())
				.password(userRequest.getPassword())
				.contactNo(userRequest.getContactNo())
				.userRole(USERROLE.valueOf(userRequest.getUserRole()))
				.build();
	}

	private UserResponse mapToUserResponse(User user)
	{
		return new UserResponse().builder()
				.userId(user.getUserId())
				.userName(user.getUserName())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.email(user.getEmail())
				.contactNo(user.getContactNo())
				.userRole(user.getUserRole())
				.isDeleted(user.isDeleted())
				.build();
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> registerUser(UserRequest userRequest) 
	{
				User user = mapToUser(userRequest);
				if(user.getUserRole()==USERROLE.ADMIN && userRepo.existsByUserRole(USERROLE.ADMIN))
				{
					structure.setStatus(HttpStatus.BAD_REQUEST.value());
					structure.setMessage("There Should be only one ADMIN to the application");
					
					return new ResponseEntity<ResponseStructure<UserResponse>>(structure,HttpStatus.BAD_REQUEST);
					
				}
				userRepo.save(user);
				UserResponse userResponse = mapToUserResponse(user);

				structure.setStatus(HttpStatus.CREATED.value());
				structure.setMessage("User registerd Successfully");
				structure.setData(userResponse);

				return new ResponseEntity<ResponseStructure<UserResponse>>(structure,HttpStatus.CREATED);
			
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> findUserById(int userId) {

		User user = userRepo.findById(userId).orElseThrow(()->new UserNotFoundByIdException("User Not present for given id"));

		UserResponse userResponse = mapToUserResponse(user);

		structure.setStatus(HttpStatus.FOUND.value());
		structure.setMessage("User Found");
		structure.setData(userResponse);

		return new ResponseEntity<ResponseStructure<UserResponse>>(structure,HttpStatus.FOUND);
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> deleteUserById(int userId) {
		User user = userRepo.findById(userId).orElseThrow(()->new UserNotFoundByIdException("User Not present for given id"));
		
		if(user.isDeleted()==false)
			user.setDeleted(true);
		
		User user2 = userRepo.save(user);
		
		
		UserResponse userResponse = mapToUserResponse(user2);
		structure.setStatus(HttpStatus.OK.value());
		structure.setMessage("deletion status updated successfully");
		structure.setData(userResponse);
		return new ResponseEntity<ResponseStructure<UserResponse>>(structure,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> assignUsersToAcademicProgram(int programId, int userId) {
		
		User user = userRepo.findById(userId).orElseThrow(()-> new UserNotFoundByIdException("User Not Present for given user id"));
		
		AcademicProgram program = programRepo.findById(programId).orElseThrow(()-> new ProgramNotFoundByIdException("Program Not present for given  program id"));
		
		if(user.getUserRole()!=USERROLE.ADMIN)
		{
			user.getAcademicPrograms().add(program);
			userRepo.save(user);
			program.getUsers().add(user);
			programRepo.save(program);
			
			structure.setStatus(HttpStatus.OK.value());
			structure.setMessage("User added to Academic Programs");
			structure.setData(mapToUserResponse(user));
			
			return new ResponseEntity<ResponseStructure<UserResponse>>(structure,HttpStatus.OK);
		}
		else
			
		throw new AdminCannotBeAssignToAcademicProgramException("Admin cannot be assigned to any Academic Programs");
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> addSubjectToTeacher(int subjectId, int userId) {
	
		return userRepo.findById(userId).map(user ->{
			if(user.getUserRole().equals(USERROLE.TEACHER))
			{
				subjectRepo.findById(subjectId).map(subject ->{
					
					user.setSubject(subject);
					return userRepo.save(user);
					
				}).orElseThrow(()->new DataNotExistException("Subject Not Found for given subject id"));
				
				structure.setStatus(HttpStatus.OK.value());
				structure.setMessage("added subject to teacher");
				structure.setData(mapToUserResponse(user));
				
				return new ResponseEntity<ResponseStructure<UserResponse>>(structure,HttpStatus.OK);
			}
			else
				throw new UnAuthorisedUserException("Invalid User, we cant add");
		}).orElseThrow(()->new UserNotFoundByIdException("User Not Present for given user id"));
	}



}
