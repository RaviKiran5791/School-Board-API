package com.school.sba.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.Subject;
import com.school.sba.exception.ProgramNotFoundByIdException;
import com.school.sba.repositary.AcademicProgramRepositary;
import com.school.sba.repositary.SubjectRepositary;
import com.school.sba.requestdto.SubjectRequest;
import com.school.sba.responsedto.AcademicProgramResponse;
import com.school.sba.service.SubjectService;
import com.school.sba.utility.ResponseStructure;
@Service
public class SubjectServiceImpl implements SubjectService{
	
	@Autowired
	private AcademicProgramRepositary programRepo;
	@Autowired
	private SubjectRepositary subjectRepo;
	
	@Autowired
	private AcademicProgramServiceImpl academicProgramServiceImpl;
	
	@Autowired
	private ResponseStructure<AcademicProgramResponse> structure;
	
	
	
	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> addSubjects(int programId,
			SubjectRequest subjectRequest) {
	
		return programRepo.findById(programId).map(program -> {
			List<Subject> subjects=new ArrayList<Subject>();
			subjectRequest.getSubjectNames().forEach(name -> {
				
				Subject subject=subjectRepo.findBySubjectName(name).map(s -> s).orElseGet(()->{
					Subject subject2=new Subject();
					subject2.setSubjectName(name);
					subjectRepo.save(subject2);
					
					return subject2;
				});
				subjects.add(subject);
			
		});
			program.setSubjects(subjects);
			programRepo.save(program);
			
			structure.setStatus(HttpStatus.CREATED.value());
			structure.setMessage("Update the subject list to Academic Program");
			structure.setData(academicProgramServiceImpl.mapToAcademicProgramResponse(program));
			
			return new ResponseEntity<ResponseStructure<AcademicProgramResponse>>(structure,HttpStatus.CREATED);
		
		}).orElseThrow(()->new ProgramNotFoundByIdException("Program not present for given id"));
		}		


}
