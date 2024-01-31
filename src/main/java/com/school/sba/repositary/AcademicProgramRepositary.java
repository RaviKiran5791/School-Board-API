package com.school.sba.repositary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.school.sba.entity.AcademicProgram;

public interface AcademicProgramRepositary extends JpaRepository<AcademicProgram, Integer>{
	
	List<AcademicProgram> findByIsDeleted(boolean isDeleted);

}
