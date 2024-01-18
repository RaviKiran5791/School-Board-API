package com.school.sba.service;

import org.springframework.http.ResponseEntity;

import com.school.sba.requestdto.ScheduleRequest;
import com.school.sba.responsedto.ScheduleResponse;
import com.school.sba.utility.ResponseStructure;

public interface ScheduleService {

	ResponseEntity<ResponseStructure<ScheduleResponse>> createSchedule(int schoolId, ScheduleRequest scheduleRequest);

	ResponseEntity<ResponseStructure<ScheduleResponse>> findScheduleBySchool(int schoolId);

	ResponseEntity<ResponseStructure<ScheduleResponse>> updateScheduleById(int scheduleId,ScheduleRequest scheduleRequest);

}
