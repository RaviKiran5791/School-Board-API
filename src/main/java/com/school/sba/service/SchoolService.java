package com.school.sba.service;

import org.springframework.http.ResponseEntity;

import com.school.sba.requestdto.SchoolRequest;
import com.school.sba.responsedto.SchoolResponse;
import com.school.sba.utility.ResponseStructure;

public interface SchoolService {

	ResponseEntity<ResponseStructure<SchoolResponse>> registerSchool(SchoolRequest schoolRequest);

	ResponseEntity<ResponseStructure<String>> deleteSchoolById(int schoolId);
	
	public void deleteSchoolPermanently();
}
