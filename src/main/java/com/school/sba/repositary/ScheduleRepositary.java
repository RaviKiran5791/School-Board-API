package com.school.sba.repositary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.school.sba.entity.Schedule;

public interface ScheduleRepositary extends JpaRepository<Schedule, Integer>{

}
