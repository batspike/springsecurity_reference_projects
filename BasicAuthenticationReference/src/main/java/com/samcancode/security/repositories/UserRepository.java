package com.samcancode.security.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samcancode.security.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	public Optional<User> findUserByUsername(String username);

}
