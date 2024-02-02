package com.school.sba.repositary;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.ClassHour;

public interface ClassHourRepositary extends JpaRepository<ClassHour, Integer>{
	
	
	boolean existsByBeginsAtBetweenAndRoomNo(LocalDateTime startsAt, LocalDateTime endsAt,int roomNo);


	@Query("SELECT ch FROM ClassHour ch WHERE ch.academicProgram = :academicProgram " +
		       "ORDER BY ch.classHourId DESC " +
		       "LIMIT :lastNrecords")
		List<ClassHour> findLastNRecordsByAcademicProgram( AcademicProgram academicProgram, int lastNrecords);



}
