package com.school.sba.repositary;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.school.sba.entity.ClassHour;

public interface ClassHourRepositary extends JpaRepository<ClassHour, Integer>{
	
	
	boolean existsByBeginsAtBetweenAndRoomNo(LocalDateTime startsAt, LocalDateTime endsAt,int roomNo);

}
