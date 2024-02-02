package com.school.sba.repositary;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.school.sba.entity.User;
import com.school.sba.enums.USERROLE;

public interface UserRepositary extends JpaRepository<User, Integer>{

	boolean existsByUserRole(USERROLE admin);

	Optional<User> findByUserName(String username);
	

//	@Query("SELECT u FROM User u JOIN u.academicPrograms ap WHERE ap.programId = :programId AND u.userRole = :userRole")
//	List<User> findUsersByRoleInProgram(int programId,  USERROLE userRole);


	List<User> findByUserRoleAndAcademicPrograms_ProgramId(USERROLE userRole, int programId);
	
	List<User> findByIsDeletedTrue();
	
	//or
//	List<User> findByIsDeleted(boolean isDeleted);
	
	List<User> findByUserRoleNot(USERROLE userRole);
	
	List<User> findByUserRole(USERROLE userRole);
	

}
