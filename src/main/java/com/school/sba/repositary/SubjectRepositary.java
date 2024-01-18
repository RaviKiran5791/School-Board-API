package com.school.sba.repositary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.Subject;

public interface SubjectRepositary extends JpaRepository<Subject, Integer>{

	Optional<Subject> findBySubjectName(String name);

}
