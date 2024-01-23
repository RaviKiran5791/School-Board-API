package com.school.sba.repositary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.school.sba.entity.User;
import com.school.sba.enums.USERROLE;

public interface UserRepositary extends JpaRepository<User, Integer>{

	boolean existsByUserRole(USERROLE admin);

	Optional<User> findByUserName(String username);




}
