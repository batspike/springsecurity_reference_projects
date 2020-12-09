package com.samcancode.security.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samcancode.security.entities.Otp;

public interface OtpRepository extends JpaRepository<Otp, Integer> {
	Optional<Otp> findOtpByUsername(String username);
}
