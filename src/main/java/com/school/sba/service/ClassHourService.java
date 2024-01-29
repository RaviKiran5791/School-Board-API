package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.requestdto.ClassHourRequest;
import com.school.sba.responsedto.ClassHourResponse;
import com.school.sba.utility.ResponseStructure;

public interface ClassHourService {

	ResponseEntity<ResponseStructure<ClassHourResponse>> addClassHoursToAcademicProgram(int programId,
			ClassHourRequest classHourRequest);

	ResponseEntity<ResponseStructure<List<ClassHourResponse>>> updateClassHour(
			List<ClassHourRequest> classHourUpdateRequests);

}
