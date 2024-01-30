package com.school.sba.serviceimpl;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.Schedule;
import com.school.sba.exception.DataAlreadyExistException;
import com.school.sba.exception.ScheduleNotFoundByIdException;
import com.school.sba.exception.ScheduleNotFoundException;
import com.school.sba.exception.SchoolNotFoundByIdException;
import com.school.sba.repositary.ScheduleRepositary;
import com.school.sba.repositary.SchoolRepositary;
import com.school.sba.requestdto.ScheduleRequest;
import com.school.sba.responsedto.ScheduleResponse;
import com.school.sba.service.ScheduleService;
import com.school.sba.utility.ResponseStructure;
@Service
public class ScheduleServiceImpl implements ScheduleService{
	
	@Autowired
	private ScheduleRepositary scheduleRepo;
	
	@Autowired
	private SchoolRepositary schoolRepo;
	
	@Autowired 
	private ResponseStructure<ScheduleResponse> structure;
	
	private Schedule mapToSchedule(ScheduleRequest scheduleRequest) {
		return Schedule.builder().opensAt(scheduleRequest.getOpensAt()).closesAt(scheduleRequest.getClosesAt())
				.classHoursPerday(scheduleRequest.getClassHoursPerday())
				.classHoursLengthInMinutes(Duration.ofMinutes(scheduleRequest.getClassHoursLengthInMinutes()))
				.breaktime(scheduleRequest.getBreaktime())
				.breakeLengthInMinutes(Duration.ofMinutes(scheduleRequest.getBreakeLengthInMinutes()))
				.lunchTime(scheduleRequest.getLunchTime())
				.lunchBreakLengthInMinutes(Duration.ofMinutes(scheduleRequest.getLunchBreakLengthInMinutes())).build();
	}

	private ScheduleResponse mapToScheduleResponse(Schedule schedule) {
		return ScheduleResponse.builder().scheduleId(schedule.getScheduleId()).opensAt(schedule.getOpensAt())
				.classHoursLengthInMinutes((int) schedule.getClassHoursLengthInMinutes().toMinutes())
				.closesAt(schedule.getClosesAt()).classHoursPerday(schedule.getClassHoursPerday())
				.breaktime(schedule.getBreaktime())
				.breakeLengthInMinutes((int) schedule.getBreakeLengthInMinutes().toMinutes())
				.lunchTime(schedule.getLunchTime())
				.lunchBreakLengthInMinutes((int) schedule.getLunchBreakLengthInMinutes().toMinutes()).build();
	}

	private Schedule deleteSchedule(Schedule schedule)
	{
		scheduleRepo.delete(schedule);
		return schedule;
	}
	@Override
	public ResponseEntity<ResponseStructure<ScheduleResponse>> createSchedule(int schoolId,ScheduleRequest scheduleRequest) 
	{
		return schoolRepo.findById(schoolId).map(school->{
			
			if(school.getSchedule()==null)
			{
				Schedule schedule = mapToSchedule(scheduleRequest);
				schedule = scheduleRepo.save(schedule);
				school.setSchedule(schedule);
				schoolRepo.save(school);
				
				ScheduleResponse scheduleResponse = mapToScheduleResponse(schedule);
				
				structure.setStatus(HttpStatus.CREATED.value());
				structure.setMessage("Schedule Created for School");
				structure.setData(scheduleResponse);
				
				return new ResponseEntity<ResponseStructure<ScheduleResponse>>(structure,HttpStatus.CREATED);
			}
			else 
				throw new DataAlreadyExistException("Schedule of a school already exist");
			
		}).orElseThrow(()->new SchoolNotFoundByIdException("School Not Present for given school id"));
		
	}

	@Override
	public ResponseEntity<ResponseStructure<ScheduleResponse>> findScheduleBySchool(int schoolId) 
	{
		return schoolRepo.findById(schoolId).map(school->{
			
			if(school.getSchedule()!=null)
			{
				structure.setStatus(HttpStatus.FOUND.value());
				structure.setMessage("Schedule data found for given school");
				structure.setData(mapToScheduleResponse(school.getSchedule()));
				
				return new ResponseEntity<ResponseStructure<ScheduleResponse>>(structure,HttpStatus.FOUND);
			}
			else {
				throw new ScheduleNotFoundException("Schedule not created for given school");
			}
			
		}).orElseThrow(()-> new SchoolNotFoundByIdException("School not present for given school id"));		
	}

	@Override
	public ResponseEntity<ResponseStructure<ScheduleResponse>> updateScheduleById(int scheduleId,
			ScheduleRequest scheduleRequest) {
		return scheduleRepo.findById(scheduleId).map(schedule->{
			
			Schedule schedule2 = mapToSchedule(scheduleRequest);
			schedule2.setScheduleId(schedule.getScheduleId());
			
			 schedule2 = scheduleRepo.save(schedule2);
			 
			 structure.setStatus(HttpStatus.OK.value());
			 structure.setMessage("Schedule Updated Successfylly");
			 structure.setData(mapToScheduleResponse(schedule2));
			 
			 return new ResponseEntity<ResponseStructure<ScheduleResponse>>(structure,HttpStatus.OK);
			
		}).orElseThrow(()->new ScheduleNotFoundByIdException("schedule not present for given schedule id"));
	
	}

	

}
