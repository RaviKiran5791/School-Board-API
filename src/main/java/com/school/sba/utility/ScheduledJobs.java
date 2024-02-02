package com.school.sba.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.school.sba.service.AcademicProgramService;
import com.school.sba.service.ClassHourService;
import com.school.sba.service.SchoolService;
import com.school.sba.service.UserService;
import com.school.sba.serviceimpl.AcademicProgramServiceImpl;
import com.school.sba.serviceimpl.ClassHourServiceImpl;
import com.school.sba.serviceimpl.UserServiceImpl;

@Component
public class ScheduledJobs {
	
	
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AcademicProgramService academicProgramService;
	
	@Autowired
	private SchoolService schoolService;
	
	@Autowired
	private ClassHourService classHourService;
	
	
	
//	@Scheduled(fixedDelay = 1000l*60)  // fixeddelay - > miliseconds(long type data)
//	void test() {
//		System.out.println("Scheduled Jobs");
//	}
	
	@Scheduled(fixedDelay = 1000l*60*5)
	void autoDeleteOperations()
	{
		// for auto deleting user
		userService.deleteUserPermanently();
		// for auto deleting academic program
		academicProgramService.deleteAcademicProgramPermanently();
		// for auto deleting school
		schoolService.deleteSchoolPermanently();
	}
	
	/**
	 *  0  : Second (0-59)
	 *	0  : Minute (0-59)
	 *	0  : Hour (0-23)
	 *	?  : Day of the month (no specific value)
	 *  *  : Month (any)
	 *	MON: Day of the week (Monday)
	 */
	
	@Scheduled(cron = "0 0 0 ? * MON")
	public void autoRepeatSchedule() {
	    classHourService.generateWeeklyClassHours();
	}
	
	
	


}
