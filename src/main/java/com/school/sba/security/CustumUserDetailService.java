package com.school.sba.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.school.sba.repositary.UserRepositary;
@Service
public class CustumUserDetailService implements UserDetailsService{
	
	@Autowired
	private UserRepositary userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepo.findByUserName(username).map(user->new CustumUserDetails(user))
				.orElseThrow(()->new UsernameNotFoundException("User Not Authenticated"));
	}

}
