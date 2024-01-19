package com.school.sba.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.exception.SchoolNotFoundByIdException;
import com.school.sba.repositary.AcademicProgramRepositary;
import com.school.sba.repositary.SchoolRepositary;
import com.school.sba.requestdto.AcademicProgramRequest;
import com.school.sba.responsedto.AcademicProgramResponse;
import com.school.sba.service.AcademicProgramService;
import com.school.sba.utility.ResponseStructure;

@Service
public class AcademicProgramServiceImpl implements AcademicProgramService{
	@Autowired
	private AcademicProgramRepositary programRepo;
	@Autowired
	private SchoolRepositary schoolRepo;
	@Autowired
	private ResponseStructure<AcademicProgramResponse> structure;

	private AcademicProgram mapToAcademicProgram(AcademicProgramRequest programRequest)
	{
		return new AcademicProgram().builder()
				.programtype(programRequest.getProgramtype())
				.programName(programRequest.getProgramName())
				.beginsAt(programRequest.getBeginsAt())
				.endsAt(programRequest.getEndsAt())
				.build();
	}

	public AcademicProgramResponse mapToAcademicProgramResponse(AcademicProgram academicProgram)
	{
		List<String> subjectNames = new ArrayList<String>();
		if(academicProgram.getSubjects()!=null)
		{
			academicProgram.getSubjects().forEach(subject->{
				subjectNames.add(subject.getSubjectName());
			});
		}

		return new AcademicProgramResponse().builder()
				.programId(academicProgram.getProgramId())
				.programtype(academicProgram.getProgramtype())
				.programName(academicProgram.getProgramName())
				.beginsAt(academicProgram.getBeginsAt())
				.endsAt(academicProgram.getEndsAt())
				.subjects(subjectNames)
				.build();

	}

	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> addAcademicPrograms(int schoolId,
			AcademicProgramRequest programRequest) {
		return schoolRepo.findById(schoolId).map(school->{

			AcademicProgram academicProgram = mapToAcademicProgram(programRequest);
			academicProgram.setSchool(school);
			academicProgram = programRepo.save(academicProgram);
			school.getPrograms().add(academicProgram);
			AcademicProgramResponse academicProgramResponse = mapToAcademicProgramResponse(academicProgram);

			structure.setStatus(HttpStatus.CREATED.value());
			structure.setMessage("Academic program added to Scool");
			structure.setData(academicProgramResponse);


			return new ResponseEntity<ResponseStructure<AcademicProgramResponse>>(structure,HttpStatus.CREATED);

		}).orElseThrow(()->new SchoolNotFoundByIdException("School not present for given school id"));
	}

	@Override
	public ResponseEntity<ResponseStructure<List<AcademicProgramResponse>>> findAllAcademicPrograms(int schoolId) {
		return schoolRepo.findById(schoolId).map(school->{

			List<AcademicProgram> programs = programRepo.findAll();

			if(!programs.isEmpty())
			{
				List<AcademicProgramResponse> list=new ArrayList<>();

				for(AcademicProgram program : programs)
				{
					AcademicProgramResponse programResponse = mapToAcademicProgramResponse(program);
					list.add(programResponse);
				}
				ResponseStructure<List<AcademicProgramResponse>> structure=new ResponseStructure<>();
				structure.setStatus(HttpStatus.FOUND.value());
				structure.setMessage("Academic Program List found");
				structure.setData(list);

				return new ResponseEntity<ResponseStructure<List<AcademicProgramResponse>>>(structure,HttpStatus.FOUND);

			}
			else
				throw new com.school.sba.exception.DataNotExistException("No Academic Programs present for given school");

		}).orElseThrow(()->new SchoolNotFoundByIdException("School Not Present for given school id"));
	}

}
