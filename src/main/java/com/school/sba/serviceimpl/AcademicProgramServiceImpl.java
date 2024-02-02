package com.school.sba.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.User;
import com.school.sba.enums.USERROLE;
import com.school.sba.exception.DataNotExistException;
import com.school.sba.exception.IllegalRequestException;
import com.school.sba.exception.ProgramNotFoundByIdException;
import com.school.sba.exception.SchoolNotFoundByIdException;
import com.school.sba.repositary.AcademicProgramRepositary;
import com.school.sba.repositary.ClassHourRepositary;
import com.school.sba.repositary.SchoolRepositary;
import com.school.sba.repositary.UserRepositary;
import com.school.sba.requestdto.AcademicProgramRequest;
import com.school.sba.responsedto.AcademicProgramResponse;
import com.school.sba.responsedto.UserResponse;
import com.school.sba.service.AcademicProgramService;
import com.school.sba.utility.ResponseStructure;

@Service
public class AcademicProgramServiceImpl implements AcademicProgramService{
	@Autowired
	private AcademicProgramRepositary programRepo;
	@Autowired
	private SchoolRepositary schoolRepo;
	@Autowired
	private UserRepositary userRepo;

	@Autowired
	private UserServiceImpl userServiceImpl;
	
	@Autowired
	private ClassHourRepositary classHourRepositary;

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
				.autoRepeateScheduled(academicProgram.isAutoRepeateScheduled())
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

	@Override
	public ResponseEntity<ResponseStructure<String>> deleteAcademicProgramById(int programId) {
		ResponseStructure<String> structure=new ResponseStructure<>();
		return programRepo.findById(programId).map(program->{

			if(!program.isDeleted())
			{
				program.setDeleted(true);
				programRepo.save(program);

				structure.setStatus(HttpStatus.OK.value());
				structure.setMessage("Deleted successfully");
				structure.setData("Program deleted successsfully for given id "+programId);

				return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.OK);
			}
			else
				throw new IllegalRequestException("Failed To Deletet Academic Program");

		}).orElseThrow(()->new ProgramNotFoundByIdException("Academic Program Not Present for id "+programId));
	}


	public void deleteAcademicProgramPermanently() {
		
		List<AcademicProgram> list = programRepo.findByIsDeleted(true);
		
		if(!list.isEmpty())
		{
			for(AcademicProgram program:list)
			{
				classHourRepositary.deleteAll(program.getClassHours());
				programRepo.delete(program);
			}
			System.out.println("Programs deleted Successfully");
		}
		else
			System.out.println("No programs present to delete");

	}

	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> autoRepeatScheduleON(int programId,boolean autoRepeateScheduled) {
		
		return programRepo.findById(programId).map(program->{
			
			if(!program.isDeleted()) {
				program.setAutoRepeateScheduled(autoRepeateScheduled);
				program.setProgramId(programId);
				programRepo.save(program);
				
				structure.setStatus(HttpStatus.OK.value());
				structure.setMessage("Auto Repeat Schedule: ON");
				structure.setData(mapToAcademicProgramResponse(program));
			
				return new ResponseEntity<ResponseStructure<AcademicProgramResponse>>(structure,HttpStatus.OK);  
			}
			throw new IllegalRequestException("Program Already DELETED");			
		}).orElseThrow(()->new ProgramNotFoundByIdException("Program not present for Given Id"));
	}


}
