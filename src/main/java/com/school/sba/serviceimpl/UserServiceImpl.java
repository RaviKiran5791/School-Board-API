package com.school.sba.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.School;
import com.school.sba.entity.Subject;
import com.school.sba.entity.User;
import com.school.sba.enums.USERROLE;
import com.school.sba.exception.AdminCannotBeAssignToAcademicProgramException;
import com.school.sba.exception.DataNotExistException;
import com.school.sba.exception.IllegalRequestException;
import com.school.sba.exception.InvalidUserRoleException;
import com.school.sba.exception.ProgramNotFoundByIdException;
import com.school.sba.exception.SchoolDataNotFoundException;
import com.school.sba.exception.UnAuthorisedUserException;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.repositary.AcademicProgramRepositary;
import com.school.sba.repositary.ClassHourRepositary;
import com.school.sba.repositary.SchoolRepositary;
import com.school.sba.repositary.SubjectRepositary;
import com.school.sba.repositary.UserRepositary;
import com.school.sba.requestdto.UserRequest;
import com.school.sba.responsedto.UserResponse;
import com.school.sba.service.UserService;
import com.school.sba.utility.ResponseStructure;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepositary userRepo;

	@Autowired
	private AcademicProgramRepositary programRepo;

	@Autowired
	private SchoolRepositary schoolRepo;

	@Autowired
	private SubjectRepositary subjectRepo;
	
	@Autowired
	private ClassHourRepositary classHourRepo;

	@Autowired
	private ResponseStructure<UserResponse> structure;

	private User mapToUser(UserRequest userRequest)
	{
		return new User().builder()
				.userName(userRequest.getUserName())
				.firstName(userRequest.getFirstName())
				.lastName(userRequest.getLastName())
				.email(userRequest.getEmail())
				.password(passwordEncoder.encode(userRequest.getPassword()))
				.contactNo(userRequest.getContactNo())
				.userRole(USERROLE.valueOf(userRequest.getUserRole()))
				.build();
	}

	public UserResponse mapToUserResponse(User user)
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
	public ResponseEntity<ResponseStructure<UserResponse>> registerAdmin(UserRequest userRequest) 
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
	public ResponseEntity<ResponseStructure<UserResponse>> addOtherUser(UserRequest userRequest) {

		String name = SecurityContextHolder.getContext().getAuthentication().getName();

		return userRepo.findByUserName(name).map(adminuser->{

			return	 schoolRepo.findById(adminuser.getSchool().getSchoolId()).map(school->{

				if(!userRequest.getUserRole().equals(USERROLE.ADMIN))
				{
					User user = mapToUser(userRequest);
					user.setSchool(school);
					userRepo.save(user);
					UserResponse userResponse = mapToUserResponse(user);

					structure.setStatus(HttpStatus.CREATED.value());
					structure.setMessage("User registerd Successfully");
					structure.setData(userResponse);

					return new ResponseEntity<ResponseStructure<UserResponse>>(structure,HttpStatus.CREATED);
				}
				else
					throw new InvalidUserRoleException("Unable to save user, Invalid user role");


			}).orElseThrow(()->new SchoolDataNotFoundException("School Not Present for a Admin"));

		}).orElseThrow(()->new UnAuthorisedUserException("User Not Authorised"));	

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

		if(!user.getUserRole().equals(USERROLE.ADMIN))
		{
			if(user.isDeleted()==false)
				user.setDeleted(true);

			User user2 = userRepo.save(user);


			UserResponse userResponse = mapToUserResponse(user2);
			structure.setStatus(HttpStatus.OK.value());
			structure.setMessage("deletion status updated successfully");
			structure.setData(userResponse);
			return new ResponseEntity<ResponseStructure<UserResponse>>(structure,HttpStatus.OK);
		}
		else
			throw new IllegalRequestException("We Cann't delete the user");
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> assignUsersToAcademicProgram(int programId, int userId) {

		User user = userRepo.findById(userId).orElseThrow(()-> new UserNotFoundByIdException("User Not Present for given user id"));

		AcademicProgram program = programRepo.findById(programId).orElseThrow(()-> new ProgramNotFoundByIdException("Program Not present for given  program id"));

		List<Subject> subjects = program.getSubjects();

		if(user.getSubject()!=null && user.getUserRole().equals(USERROLE.TEACHER) &&  subjects.contains(user.getSubject()))
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

		else if(user.getUserRole().equals(USERROLE.STUDENT))
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
			if(user.getUserRole().equals(USERROLE.TEACHER)&& user.getSubject()==null)
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

	@Override
	public ResponseEntity<ResponseStructure<List<UserResponse>>> findUserByRoleInProgram(int programId, USERROLE userRole) {

		if(userRole.equals(USERROLE.ADMIN))
			throw new IllegalRequestException("Illegle request User role is not valid");
		ResponseStructure<List<UserResponse>> structure=new ResponseStructure<>();

		return programRepo.findById(programId).map(program->{

			//			List<User> users = program.getUsers();
			//			List<User> userRolelist = users.stream().filter(user->user.getUserRole().equals(userRole)).toList();

			// or

			List<User> userList= userRepo.findByUserRoleAndAcademicPrograms_ProgramId(userRole, programId);

			if(!userList.isEmpty())
			{
				List<UserResponse> userResponsliste=new ArrayList<>();

				for(User user : userList)
				{
					UserResponse userResponse = mapToUserResponse(user);
					userResponsliste.add(userResponse);
				}

				structure.setStatus(HttpStatus.FOUND.value());
				structure.setMessage("found user list");
				structure.setData(userResponsliste);

				return new ResponseEntity<ResponseStructure<List<UserResponse>>>(structure,HttpStatus.FOUND);
			}
			else
				throw new DataNotExistException("User List not present for given Userrole");

		}).orElseThrow(()->new ProgramNotFoundByIdException("program not present for given id"));

	}
	@Transactional
	public void deleteUserPermanently()
	{
		List<User> listOfUsersToBeDeleted = userRepo.findByIsDeletedTrue();
		
	   if(!listOfUsersToBeDeleted.isEmpty())
	   {
		   listOfUsersToBeDeleted.forEach(user->{
			   user.getAcademicPrograms().forEach(program->{
				   program.getClassHours().forEach(classHour->{
					   if(classHour.getUser()==user)
					   {
						   classHour.setUser(null);
						   classHourRepo.save(classHour);
					   }
					   
				   });
				   program.getUsers().remove(user);
				   
				   programRepo.save(program);
			   });
			   userRepo.delete(user);
		   });
		   System.out.println("User Deleted");
	   }
	   else
		   System.out.println("Data Not Exist to Delete");
		
	}
}
