package com.samcancode.security.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.samcancode.security.entities.User;
import com.samcancode.security.repositories.UserRepository;

public class JpaUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("Looking up UserDetails with JpaUserDetailsService...");
		
		Optional<User> user = userRepo.findUserByUsername(username);
		User u = user.orElseThrow(() -> new UsernameNotFoundException(username + " Not Found!"));
		
		return new SecurityUserDetails(u);
	}

}
