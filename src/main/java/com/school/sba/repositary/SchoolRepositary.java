package com.school.sba.repositary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.school.sba.entity.School;

import jakarta.persistence.criteria.CriteriaBuilder.In;

public interface SchoolRepositary extends JpaRepository<School, Integer>{

}
