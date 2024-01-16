package com.school.sba.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.School;
import com.school.sba.exception.SchoolDataNotFoundException;
import com.school.sba.exception.SchoolNotFoundByIdException;
import com.school.sba.repositary.SchoolRepositary;
import com.school.sba.requestdto.SchoolRequest;
import com.school.sba.responsedto.SchoolResponse;
import com.school.sba.service.SchoolService;
import com.school.sba.utility.ResponseStructure;
@Service
public class SchoolServiceImpl implements SchoolService{
	
	@Autowired
	SchoolRepositary schoolRepo;
	@Autowired
	ResponseStructure<SchoolResponse> structure;
	
	private School convertToSchool(SchoolRequest schoolRequestDto) {
		return School.builder()
				.schoolName(schoolRequestDto.getSchoolName())
				.contactNo(schoolRequestDto.getContactNo())
				.emailId(schoolRequestDto.getEmailId())
				.address(schoolRequestDto.getAddress())
				.build();
	}
	private SchoolResponse convertToSchoolResponseDto(School school) {
		
		return SchoolResponse.builder()
				.schoolId(school.getSchoolId())
				.schoolName(school.getSchoolName())
				.contactNo(school.getContactNo())
				.emailId(school.getEmailId())
				.address(school.getAddress())
				.build();
	}

	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> saveSchool(SchoolRequest schoolRequestDto) {
		School school=convertToSchool(schoolRequestDto);
		
		school = schoolRepo.save(school);
		
		SchoolResponse schoolResponseDto=convertToSchoolResponseDto(school);
		
		structure.setStatus(HttpStatus.CREATED.value());
		structure.setMessage("School Created Successfully..!!!");
		structure.setData(schoolResponseDto);
		
		
		return new ResponseEntity<ResponseStructure<SchoolResponse>>(structure,HttpStatus.CREATED);
	}


	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> findSchoolById(int schoolId) {
		Optional<School> optional = schoolRepo.findById(schoolId);
		
		if(optional.isPresent())
		{
			School school = optional.get();
			
			SchoolResponse schoolResponseDto = convertToSchoolResponseDto(school);
			
			structure.setStatus(HttpStatus.FOUND.value());
			structure.setMessage("School Data Found by id");
			structure.setData(schoolResponseDto);
			
			return new ResponseEntity<ResponseStructure<SchoolResponse>>(structure,HttpStatus.FOUND);
		}
		else
			throw new SchoolNotFoundByIdException("School not Found to fetch data for Given Id");
		
	}

	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> deleteSchoolById(int schoolId) {
		Optional<School> optional = schoolRepo.findById(schoolId);
		if(optional.isPresent())
		{
			School school = optional.get();
			
			schoolRepo.delete(school);
			
			SchoolResponse schoolResponseDto = convertToSchoolResponseDto(school);
			
			structure.setStatus(HttpStatus.OK.value());
			structure.setMessage("School Deleted Successfully");
			structure.setData(schoolResponseDto);
			
			return new ResponseEntity<ResponseStructure<SchoolResponse>>(structure,HttpStatus.OK);
		}
		else
			throw new SchoolNotFoundByIdException("School Not Found to delete for given Id");
		
	}

	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> updateSchoolById(SchoolRequest schoolRequestDto,int schoolId) {
		Optional<School> optional = schoolRepo.findById(schoolId);
		
		if(optional.isPresent())
		{
			School oldschool = optional.get();
			
			School school = convertToSchool(schoolRequestDto);
			school.setSchoolId(oldschool.getSchoolId());
			
			School school2 = schoolRepo.save(school);
			
			SchoolResponse schoolResponseDto = convertToSchoolResponseDto(school2);
			
			structure.setStatus(HttpStatus.ACCEPTED.value());
			structure.setMessage("School Data Updated Successfully");
			structure.setData(schoolResponseDto);
			
			return new ResponseEntity<ResponseStructure<SchoolResponse>>(structure,HttpStatus.ACCEPTED);
			
		}
		else
			throw new SchoolNotFoundByIdException("School Not Found to Update for given ID");
	}

	@Override
	public ResponseEntity<ResponseStructure<List<SchoolResponse>>> findAllSchools() {
		
		List<School> schholList = schoolRepo.findAll();
		if(!schholList.isEmpty())
		{
			List<SchoolResponse> list=new ArrayList<>();
			
			for(School school : schholList)
			{
				SchoolResponse schoolResponseDto = convertToSchoolResponseDto(school);
				list.add(schoolResponseDto);
			}
			ResponseStructure<List<SchoolResponse>> structure=new ResponseStructure<>();
			
			structure.setStatus(HttpStatus.FOUND.value());
			structure.setMessage("School List Found");
			structure.setData(list);
			
			return new ResponseEntity<ResponseStructure<List<SchoolResponse>>>(structure,HttpStatus.FOUND);
		}

		else
			throw new SchoolDataNotFoundException("No Schools Present To fetch the Schools Data");
	}

}
