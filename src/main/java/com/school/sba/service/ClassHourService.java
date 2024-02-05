package com.school.sba.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.school.sba.requestdto.ClassHourRequest;
import com.school.sba.requestdto.ExcelRequestDto;
import com.school.sba.responsedto.ClassHourResponse;
import com.school.sba.utility.ResponseStructure;

public interface ClassHourService {

	ResponseEntity<ResponseStructure<ClassHourResponse>> addClassHoursToAcademicProgram(int programId,
			ClassHourRequest classHourRequest);

	ResponseEntity<ResponseStructure<List<ClassHourResponse>>> updateClassHour(
			List<ClassHourRequest> classHourUpdateRequests);
	
	public void generateWeeklyClassHours();

	ResponseEntity<ResponseStructure<String>> writeIntoXlSheet(int programId,ExcelRequestDto excelRequest);

	ResponseEntity<?> writeToXlSheet(int programId, LocalDate fromDate, LocalDate toDate, MultipartFile multipartFile);


}
