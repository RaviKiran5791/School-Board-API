package com.school.sba.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.school.sba.entity.School;
import com.school.sba.enums.USERROLE;
import com.school.sba.exception.DataAlreadyExistException;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.repositary.SchoolRepositary;
import com.school.sba.repositary.UserRepositary;
import com.school.sba.requestdto.SchoolRequest;
import com.school.sba.responsedto.SchoolResponse;
import com.school.sba.service.SchoolService;
import com.school.sba.utility.ResponseStructure;

@Service
public class SchoolServiceImpl implements SchoolService {

	@Autowired
	SchoolRepositary schoolRepo;
	@Autowired
	UserRepositary userRepo;

	@Autowired
	ResponseStructure<SchoolResponse> structure;

	private School mapToSchool(SchoolRequest schoolRequest) {
		return School.builder().schoolName(schoolRequest.getSchoolName()).contactNo(schoolRequest.getContactNo())
				.emailId(schoolRequest.getEmailId()).address(schoolRequest.getAddress()).build();
	}

	private SchoolResponse mapToSchoolResponse(School school) {

		return SchoolResponse.builder().schoolId(school.getSchoolId()).schoolName(school.getSchoolName())
				.contactNo(school.getContactNo()).emailId(school.getEmailId()).address(school.getAddress()).build();
	}

	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> registerSchool(SchoolRequest schoolRequest)
	{
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();

		return userRepo.findByUserName(userName).map(u -> {
			if (u.getUserRole().equals(USERROLE.ADMIN)) {
				if (u.getSchool() == null) {
					School school = mapToSchool(schoolRequest);
					schoolRepo.save(school);
					SchoolResponse schoolResponse = mapToSchoolResponse(school);
					u.setSchool(school);
					userRepo.save(u);

					structure.setStatus(HttpStatus.CREATED.value());
					structure.setMessage("School Created Successfully");
					structure.setData(schoolResponse);

					return new ResponseEntity<ResponseStructure<SchoolResponse>>(structure, HttpStatus.CREATED);
				} else {
					throw new DataAlreadyExistException("School Already Created by Admin");
				}
			} else {
				structure.setStatus(HttpStatus.BAD_REQUEST.value());
				structure.setMessage("Only ADMIN can create School...!");
				return new ResponseEntity<ResponseStructure<SchoolResponse>>(structure, HttpStatus.BAD_REQUEST);
			}

		}).orElseThrow(() -> new UserNotFoundByIdException("User Not Present for given Id"));

	}

}
