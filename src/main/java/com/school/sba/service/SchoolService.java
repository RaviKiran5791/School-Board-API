package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.requestdto.SchoolRequestDto;
import com.school.sba.responsedto.SchoolResponseDto;
import com.school.sba.utility.ResponseStructure;

public interface SchoolService {
	
	public ResponseEntity<ResponseStructure<SchoolResponseDto>> saveSchool(SchoolRequestDto schoolRequestDto);
	public ResponseEntity<ResponseStructure<SchoolResponseDto>> findSchoolById(int schoolId);
	public ResponseEntity<ResponseStructure<SchoolResponseDto>> deleteSchoolById(int schoolId);
	public ResponseEntity<ResponseStructure<SchoolResponseDto>> updateSchoolById(SchoolRequestDto schoolRequestDto,int schoolId);
	public ResponseEntity<ResponseStructure<List<SchoolResponseDto>>> findAllSchools();
	

}
