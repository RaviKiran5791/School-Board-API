package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.requestdto.SchoolRequest;
import com.school.sba.responsedto.SchoolResponse;
import com.school.sba.utility.ResponseStructure;

public interface SchoolService {
	
	public ResponseEntity<ResponseStructure<SchoolResponse>> saveSchool(SchoolRequest schoolRequestDto);
	public ResponseEntity<ResponseStructure<SchoolResponse>> findSchoolById(int schoolId);
	public ResponseEntity<ResponseStructure<SchoolResponse>> deleteSchoolById(int schoolId);
	public ResponseEntity<ResponseStructure<SchoolResponse>> updateSchoolById(SchoolRequest schoolRequestDto,int schoolId);
	public ResponseEntity<ResponseStructure<List<SchoolResponse>>> findAllSchools();
	

}
