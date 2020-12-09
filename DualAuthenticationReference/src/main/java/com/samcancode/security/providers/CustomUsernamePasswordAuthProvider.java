package com.samcancode.security.providers;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.samcancode.security.entities.Otp;
import com.samcancode.security.repositories.OtpRepository;
import com.samcancode.security.services.JpaUserDetailsService;

@Component
public class CustomUsernamePasswordAuthProvider implements AuthenticationProvider {
	
	@Autowired
	private JpaUserDetailsService userDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private OtpRepository otpRepo;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		System.out.println("\t... authenticate using "+ this.getClass().getName());
		
		String username = authentication.getName();
		String password = String.valueOf(authentication.getCredentials());
		
		UserDetails user = userDetailsService.loadUserByUsername(username);
		
		if (passwordEncoder.matches(password, user.getPassword())) {
			
			// at this point authentication is successful, so time to setup OTP
			String otpCode = String.valueOf(new Random().nextInt(9999)+1000);
			
			Optional<Otp> optionalOTP = otpRepo.findOtpByUsername(username);
			if(optionalOTP.isPresent()) { // if user already in otp table, then update the otp code
				Otp o = optionalOTP.get();
				o.setOtp(otpCode);
				otpRepo.save(o);
			}
			else { // else create new entry for the user in the otp table
				Otp o = new Otp();
				o.setUsername(username);
				o.setOtp(otpCode);
				otpRepo.save(o);
			}
			
			return new UsernamePasswordAuthenticationToken(username, password, user.getAuthorities());
		}
		
		throw new BadCredentialsException("Fail to authenticate!");
	}

	@Override
	public boolean supports(Class<?> authentication) {
		/*
		 * Authentication Manager will call this method to check for authentication token type is supported.
		 * If yes, then it will call the above authenticate method. 
		 * Here we check for support for UsernamePasswordAuthenticationToken type since this is the type 
		 * returned by authenticate method above if authenticate is successful.
		 */
		return UsernamePasswordAuthenticationToken.class.equals(authentication);
	}

}
