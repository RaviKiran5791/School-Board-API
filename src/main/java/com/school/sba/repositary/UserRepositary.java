package com.school.sba.repositary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.school.sba.entity.User;
import com.school.sba.enums.USERROLE;

public interface UserRepositary extends JpaRepository<User, Integer>{

	boolean existsByUserRole(USERROLE admin);




}
