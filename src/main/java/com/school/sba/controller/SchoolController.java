package com.school.sba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.requestdto.SchoolRequestDto;
import com.school.sba.responsedto.SchoolResponseDto;
import com.school.sba.service.SchoolService;
import com.school.sba.utility.ResponseStructure;

@RestController
public class SchoolController {
	
	@Autowired
	private SchoolService schoolService;
	
	@PostMapping("/schools")
	public ResponseEntity<ResponseStructure<SchoolResponseDto>> saveSchool(@RequestBody SchoolRequestDto schoolRequestDto){
		return schoolService.saveSchool(schoolRequestDto);
	}
	@GetMapping("/schools/{schoolId}")
	public ResponseEntity<ResponseStructure<SchoolResponseDto>> findSchoolById(@PathVariable int schoolId){
		return schoolService.findSchoolById(schoolId);
	}
	@DeleteMapping("/schools/{schoolId}")
	public ResponseEntity<ResponseStructure<SchoolResponseDto>> deleteSchoolById(@PathVariable int schoolId){
		return schoolService.deleteSchoolById(schoolId);
	}
	@PutMapping("/schools/{schoolId}")
	public ResponseEntity<ResponseStructure<SchoolResponseDto>> updateSchoolById(@RequestBody SchoolRequestDto schoolRequestDto,@PathVariable int schoolId){
		return schoolService.updateSchoolById(schoolRequestDto, schoolId);
	}
	@GetMapping("/schools")
	public ResponseEntity<ResponseStructure<List<SchoolResponseDto>>> findAllSchools(){
		return schoolService.findAllSchools();
	}

}
