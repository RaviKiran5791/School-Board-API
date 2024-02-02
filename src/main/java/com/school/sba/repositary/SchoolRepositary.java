package com.school.sba.repositary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.school.sba.entity.School;

public interface SchoolRepositary extends JpaRepository<School, Integer>{
	
	List<School> findByIsDeleted(boolean isDeleted);

}
