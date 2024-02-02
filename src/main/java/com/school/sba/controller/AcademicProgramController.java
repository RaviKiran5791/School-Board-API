package com.school.sba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.enums.USERROLE;
import com.school.sba.requestdto.AcademicProgramRequest;
import com.school.sba.responsedto.AcademicProgramResponse;
import com.school.sba.responsedto.ClassHourResponse;
import com.school.sba.responsedto.UserResponse;
import com.school.sba.service.AcademicProgramService;
import com.school.sba.utility.ResponseStructure;

@RestController
public class AcademicProgramController {
	
	@Autowired 
	private AcademicProgramService programService;
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/schools/{schoolId}/academic-programs")
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>>
	                          addAcademicPrograms(@PathVariable int schoolId,@RequestBody AcademicProgramRequest programRequest )
	{
		return programService.addAcademicPrograms(schoolId,programRequest);
	}
	
	@GetMapping("/schools/{schoolId}/academic-programs")
	public ResponseEntity<ResponseStructure<List<AcademicProgramResponse>>> findAllAcademicPrograms(@PathVariable int schoolId)
	{
		return programService.findAllAcademicPrograms(schoolId);
	}
	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("/academic-programs/{programId}")
	public ResponseEntity<ResponseStructure<String>> deleteAcademicProgramById(@PathVariable int programId){
		return programService.deleteAcademicProgramById(programId);
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping("/academic-program/{programId}")
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> autoRepeatScheduleON(@PathVariable int programId,@RequestParam("auto-repeate-schedule")boolean autoRepeateScheduled){
		return programService.autoRepeatScheduleON(programId,autoRepeateScheduled);
	}

}
