package com.school.sba.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.school.sba.service.AcademicProgramService;
import com.school.sba.service.SchoolService;
import com.school.sba.service.UserService;
import com.school.sba.serviceimpl.AcademicProgramServiceImpl;
import com.school.sba.serviceimpl.UserServiceImpl;

@Component
public class ScheduledJobs {
	
	
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AcademicProgramService academicProgramService;
	
	@Autowired
	private SchoolService schoolService;
	
	
	
//	@Scheduled(fixedDelay = 1000l*60)  // fixeddelay - > miliseconds(long type data)
//	void test() {
//		System.out.println("Scheduled Jobs");
//	}
	
	@Scheduled(fixedDelay = 1000l*60)
	void autoDeleteUser()
	{
		userService.deleteUserPermanently();
		
	}
	
	@Scheduled(fixedDelay = 1000l*60)
	void autoDeleteAcademicProgram() 
	{
		academicProgramService.deleteAcademicProgramPermanently();
	}
	
	@Scheduled(fixedDelay = 1000l*60)
	void autoDeleteSchool()
	{
		schoolService.deleteSchoolPermanently();
	}

}
