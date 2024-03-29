package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.enums.USERROLE;
import com.school.sba.requestdto.AcademicProgramRequest;
import com.school.sba.responsedto.AcademicProgramResponse;
import com.school.sba.responsedto.ClassHourResponse;
import com.school.sba.responsedto.UserResponse;
import com.school.sba.utility.ResponseStructure;

public interface AcademicProgramService {

	ResponseEntity<ResponseStructure<AcademicProgramResponse>> addAcademicPrograms(int schoolId,
			AcademicProgramRequest programRequest);

	ResponseEntity<ResponseStructure<List<AcademicProgramResponse>>> findAllAcademicPrograms(int schoolId);

	ResponseEntity<ResponseStructure<String>> deleteAcademicProgramById(int programId);

	public void deleteAcademicProgramPermanently();

	ResponseEntity<ResponseStructure<AcademicProgramResponse>> autoRepeatScheduleON(int programId,boolean autoRepeateScheduled);


}
