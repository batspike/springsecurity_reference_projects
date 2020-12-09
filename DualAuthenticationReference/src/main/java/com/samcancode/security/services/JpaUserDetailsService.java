package com.samcancode.security.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.samcancode.security.entities.User;
import com.samcancode.security.models.SecurityUserDetails;
import com.samcancode.security.repositories.UserRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<User> optUser = userRepo.findUserByUsername(username);
		User user = optUser.orElseThrow(() -> new UsernameNotFoundException(username));
		
		return new SecurityUserDetails(user);
	}

}
